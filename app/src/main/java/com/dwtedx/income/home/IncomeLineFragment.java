package com.dwtedx.income.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;
import com.dwtedx.income.addrecord.AddRecordActivity;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiBudget;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.entity.DiScan;
import com.dwtedx.income.home.adapter.DiIncomeLineAdapter;
import com.dwtedx.income.profile.LoginV2Activity;
import com.dwtedx.income.service.IncomeService;
import com.dwtedx.income.sqliteservice.DIBudgetService;
import com.dwtedx.income.sqliteservice.DIScanService;
import com.dwtedx.income.sqliteservice.DlIncomeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.RecycleViewDivider;
import com.dwtedx.income.widget.springheader.RotationFooter;
import com.dwtedx.income.widget.springheader.RotationHeader;
import com.liaoinstan.springview.widget.SpringView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/16.
 * Company 路之遥网络科技有限公司
 * Description TODO(这里用一句话描述这个类的作用)
 */
public class IncomeLineFragment extends BaseFragment implements SpringView.OnFreshListener, View.OnClickListener {

    public static boolean mLoadIncome;
    private DlIncomeService mDlIncomeService;

    private View mView;
    private SpringView mSpringView;
    private RecyclerView mRecyclerView;
    //private View headerView;
    //private View footerView;
    private TextView mTextViewTip;
    private List<DiIncome> mDiIncomeItems;
    private DiIncomeLineAdapter mDiIncomeMoneyLineAdapter;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private int mSynchronizeCount;
    private boolean mIsAutoSynchronize;

    private LinearLayout mLeftLayout;
    private LinearLayout mRightLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null != mView){
            return mView;
        }
        mLoadIncome = true;
        mView = inflater.inflate(R.layout.fragment_money_line, container, false);

        mDlIncomeService = DlIncomeService.getInstance(mFragmentContext);

        mLeftLayout = (LinearLayout) mView.findViewById(R.id.home_list_layout);
        mLeftLayout.setOnClickListener(this);
        mRightLayout = (LinearLayout) mView.findViewById(R.id.home_item_layout);
        mRightLayout.setOnClickListener(this);

        mSpringView = (SpringView) mView.findViewById(R.id.springview);
        mSpringView.setListener(this);
        mSpringView.setHeader(new RotationHeader(mFragmentContext));
        mSpringView.setFooter(new RotationFooter(mFragmentContext));
        mSpringView.setType(SpringView.Type.OVERLAP);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.listView);
        mTextViewTip = (TextView) mView.findViewById(R.id.listView_tip);
        mTextViewTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mFragmentContext, AddRecordActivity.class));
            }
        });

        // 使用重写后的线性布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(super.mFragmentContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mFragmentContext, LinearLayoutManager.VERTICAL, 0, 0));
        // 添加头部和脚部，如果不添加就使用默认的头部和脚部

        // 设置适配器
        mDiIncomeItems = new ArrayList<>();
        mDiIncomeMoneyLineAdapter = new DiIncomeLineAdapter(mFragmentContext, mDiIncomeItems, mDlIncomeService);
        mRecyclerView.setAdapter(mDiIncomeMoneyLineAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(null != mDiIncomeMoneyLineAdapter.getmDisplayEditView() && View.VISIBLE == mDiIncomeMoneyLineAdapter.getmDisplayEditView().getVisibility()) {
                    //添加动画
                    Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(mFragmentContext, R.anim.slide_out_top);
                    mDiIncomeMoneyLineAdapter.getmDisplayEditView().startAnimation(hyperspaceJumpAnimation);
                    mDiIncomeMoneyLineAdapter.getmDisplayEditView().setVisibility(View.GONE);
                }
            }
        });

        //开启云服务器数据同步确保数据安全
        showTip();

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mLoadIncome){
            mLoadIncome = false;
            showIncome();
        }
    }

    public void showIncome() {
        mDiIncomeItems.clear();
        //添加当前月收入与支出
        //获取当前月第一天：
        Calendar mStartTimeCalendar = Calendar.getInstance();
        mStartTimeCalendar.set(Calendar.DAY_OF_MONTH, 1);
        //设置为1号,当前日期既为本月第一天
        String startTime = format.format(mStartTimeCalendar.getTime()) + " 00:00:00";
        //获取当前月最后一天
        Calendar mEndTimeCalendar = Calendar.getInstance();
        mEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endTime = format.format(mEndTimeCalendar.getTime()) + " 24:00:00";

        //添加数据
        List<DiIncome> diIncomes = mDlIncomeService.getScrollData(0, CommonConstants.PAGE_LENGTH_NUMBER);
        if (diIncomes.size() > 0) {
            double leftMoney = mDlIncomeService.getSumMoneyByData(CommonConstants.INCOME_ROLE_INCOME, startTime, endTime).getMoneysum();
            double rightMoney = mDlIncomeService.getSumMoneyByData(CommonConstants.INCOME_ROLE_PAYING, startTime, endTime).getMoneysum();
            //预算
            DiBudget budget = DIBudgetService.getInstance(mFragmentContext).findLastRow();
            mDiIncomeItems.add(new DiIncome(CommonConstants.INCOME_ROLE_POOL, CommonUtility.twoPlaces(leftMoney), CommonUtility.twoPlaces(rightMoney), budget.getMoneylast(), (mStartTimeCalendar.get(mStartTimeCalendar.MONTH) + 1) + "月"));
            mDiIncomeItems.addAll(diIncomes);
        }
        mTextViewTip.setVisibility((mDiIncomeItems.size() == 0) ? View.VISIBLE : View.INVISIBLE);
        mRecyclerView.getAdapter().notifyDataSetChanged();

        //同步记录
        mSynchronizeCount = 0;
        // 刷新 (登录了自动同步云服务器)
        if (isLogin() && mDiIncomeItems.size() > 0 && !mIsAutoSynchronize) {
            mIsAutoSynchronize = true;
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSpringView.callFresh();
                }}, 500);
        }
    }

    @Override
    public void onRefresh() {
        if (!isLogin()) {
            new MaterialDialog.Builder(mFragmentContext)
                    .title(R.string.tip)
                    .content(R.string.synchronize_tip)
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            //NEGATIVE   POSITIVE
                            if (which.name().equals("POSITIVE")) {
                                startActivity(new Intent(mFragmentContext, LoginV2Activity.class));
                            }
                        }
                    })
                    .show();
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSpringView.onFinishFreshAndLoad();
                }}, 500);
            return;
        }
        synchronize();
    }

    @Override
    public void onLoadmore() {
        //加载更多
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int startNom = 0;
                if (mDiIncomeItems.size() > 0) {
                    startNom = mDiIncomeItems.size() - 1;
                }
                mDiIncomeItems.addAll(mDlIncomeService.getScrollData(startNom, CommonConstants.PAGE_LENGTH_NUMBER));
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mSpringView.onFinishFreshAndLoad();
            }}, 500);
    }

    private void synchronize() {
        final List<DiIncome> mDiIncomeList = mDlIncomeService.findNotUpdate();
        if (mDiIncomeList.size() == 0) {
            synchronizeBudget();
            return;
        }
        //添加用户名和id
        for (DiIncome mDiIncome : mDiIncomeList) {
            mDiIncome.setUserid(ApplicationData.mDiUserInfo.getId());
            mDiIncome.setUsername(ApplicationData.mDiUserInfo.getName());
        }
        SaDataProccessHandler<Void, Void, List<DiIncome>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<DiIncome>>((BaseActivity) mFragmentContext) {
            @Override
            public void onSuccess(List<DiIncome> data) {
                for (DiIncome mDiIncome : data) {
                    mDiIncome.setIsupdate(CommonConstants.INCOME_RECORD_UPDATEED);
                    mDlIncomeService.update(mDiIncome);
                    //保存扫单
                    for (DiScan diScan : mDiIncome.getScanList()) {
                        diScan.setIsupdate(CommonConstants.INCOME_RECORD_UPDATEED);
                        DIScanService.getInstance(mFragmentContext).update(diScan);
                    }

                }
                mSynchronizeCount += mDiIncomeList.size();
                //Snackbar.make(mRecyclerView, mDiIncomeList.size() + mFragmentContext.getString(R.string.synchronize_done), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //mRecyclerView.refreshComplate();
                //mSpringView.onFinishFreshAndLoad();
                synchronize();
            }

            @Override
            public void onPreExecute() {
            }

            @Override
            public void handlerError(SaException e) {
                super.handlerError(e);
                mSpringView.onFinishFreshAndLoad();
            }
        };

        IncomeService.getInstance().incomeCynchronize(mDiIncomeList, dataVerHandler);
    }


    private void synchronizeBudget() {
        Calendar calendar = Calendar.getInstance();
        final List<DiBudget> mDiBudgetList = DIBudgetService.getInstance(mFragmentContext).findNotUpdate(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1));
        if (mDiBudgetList.size() == 0) {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mSynchronizeCount > 0) {
                        Toast.makeText(mFragmentContext, mSynchronizeCount + mFragmentContext.getString(R.string.synchronize_done), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mFragmentContext, R.string.synchronize_done_tip, Toast.LENGTH_SHORT).show();
                    }
                    mSpringView.onFinishFreshAndLoad();
                }}, 1000);
            return;
        }
        //添加用户名和id
        for (DiBudget mDiBudget : mDiBudgetList) {
            mDiBudget.setUserid(ApplicationData.mDiUserInfo.getId());
            mDiBudget.setUsername(ApplicationData.mDiUserInfo.getName());
        }
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(mFragmentContext) {
            @Override
            public void onSuccess(Void data) {
                for (DiBudget mDiBudget : mDiBudgetList) {
                    mDiBudget.setIsupdate(CommonConstants.INCOME_RECORD_UPDATEED);
                    DIBudgetService.getInstance(mFragmentContext).update(mDiBudget);
                }
                if (mSynchronizeCount > 0) {
                    Toast.makeText(mFragmentContext, mSynchronizeCount + mFragmentContext.getString(R.string.synchronize_done), Toast.LENGTH_SHORT).show();
                    mSynchronizeCount = 0;
                } else {
                    Toast.makeText(mFragmentContext, R.string.synchronize_done_tip, Toast.LENGTH_SHORT).show();
                }
                mSpringView.onFinishFreshAndLoad();
            }

            @Override
            public void onPreExecute() {
            }

            @Override
            public void handlerError(SaException e) {
                super.handlerError(e);
                mSpringView.onFinishFreshAndLoad();
            }
        };

        IncomeService.getInstance().budgetCynchronize(mDiBudgetList, dataVerHandler);
    }

    private void showTip() {
        int x = (int) (Math.random() * 100);
        if (x > 97) {
            if (isLogin()) {
                return;
            }
            new MaterialDialog.Builder(mFragmentContext)
                    .title(R.string.tip)
                    .content(R.string.show_synchronize_tip)
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            //NEGATIVE   POSITIVE
                            if (which.name().equals("POSITIVE")) {
                                startActivity(new Intent(mFragmentContext, LoginV2Activity.class));
                            }
                        }
                    })
                    .show();
        } else if (x > 95 && x <= 97) {
            new MaterialDialog.Builder(mFragmentContext)
                    .title(R.string.tip)
                    .content(R.string.show_score_tip)
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            //NEGATIVE   POSITIVE
                            if (which.name().equals("POSITIVE")) {
                                Uri uri = Uri.parse("market://details?id=" + mFragmentContext.getPackageName());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_list_layout:
                startActivity(new Intent(mFragmentContext, IncomeListActivity.class));
                break;

            case R.id.home_item_layout:
                startActivity(new Intent(mFragmentContext, SearchActivity.class));
                break;
        }
    }
}
