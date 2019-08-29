package com.dwtedx.income.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.sch.rfview.AnimRFRecyclerView;
import com.sch.rfview.manager.AnimRFLinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/16.
 * Company 路之遥网络科技有限公司
 * Description TODO(这里用一句话描述这个类的作用)
 */
public class IncomeLineFragment extends BaseFragment implements AnimRFRecyclerView.LoadDataListener, View.OnClickListener {

    private DlIncomeService mDlIncomeService;

    private AnimRFRecyclerView mRecyclerView;
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
        View mView = inflater.inflate(R.layout.fragment_money_line, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDlIncomeService = DlIncomeService.getInstance(getContext());

        mLeftLayout = (LinearLayout) view.findViewById(R.id.home_list_layout);
        mLeftLayout.setOnClickListener(this);
        mRightLayout = (LinearLayout) view.findViewById(R.id.home_item_layout);
        mRightLayout.setOnClickListener(this);

        mRecyclerView = (AnimRFRecyclerView) view.findViewById(R.id.listView);
        mTextViewTip = (TextView) view.findViewById(R.id.listView_tip);
        mTextViewTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddRecordActivity.class));
            }
        });

        // 头部
        //headerView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_money_line_header_view, null);

        // 脚部
        //footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_load_more, null);

        // 使用重写后的线性布局管理器
        AnimRFLinearLayoutManager manager = new AnimRFLinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, 0, 0));
        //// 添加头部和脚部，如果不添加就使用默认的头部和脚部
        //mRecyclerView.addHeaderView(headerView);
        //// 设置头部的最大拉伸倍率，默认1.5f，必须写在setHeaderImage()之前
        //mRecyclerView.setScaleRatio(1.7f);
        //// 设置下拉时拉伸的图片，不设置就使用默认的
        //mRecyclerView.setHeaderImage((ImageView) headerView.findViewById(R.id.iv_hander));
        //mRecyclerView.addFootView(footerView);
        // 设置刷新动画的颜色
        mRecyclerView.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent), ContextCompat.getColor(getContext(), R.color.colorPrimary));
        // 设置头部恢复动画的执行时间，默认500毫秒
        mRecyclerView.setHeaderImageDurationMillis(300);
        // 设置拉伸到最高时头部的透明度，默认0.5f
        mRecyclerView.setHeaderImageMinAlpha(0.6f);

        // 设置适配器
        mDiIncomeItems = new ArrayList<>();
        mDiIncomeMoneyLineAdapter = new DiIncomeLineAdapter(getContext(), mDiIncomeItems, mDlIncomeService);
        mRecyclerView.setAdapter(mDiIncomeMoneyLineAdapter);

        // 设置刷新和加载更多数据的监听，分别在onRefresh()和onLoadMore()方法中执行刷新和加载更多操作
        mRecyclerView.setLoadDataListener(this);

        //mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView){
        //    @Override
        //    public void onLongClick(RecyclerView.ViewHolder vh) {
        //
        //    }
        //    @Override
        //    public void onItemClick(RecyclerView.ViewHolder vh) {
        //        DiIncome item = mDiIncomeItems.get(vh.getLayoutPosition());
        //        Toast.makeText(getActivity(),item.getId()+" "+item.getType(),Toast.LENGTH_SHORT).show();
        //
        //    }
        //});

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
                    Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_top);
                    mDiIncomeMoneyLineAdapter.getmDisplayEditView().startAnimation(hyperspaceJumpAnimation);
                    mDiIncomeMoneyLineAdapter.getmDisplayEditView().setVisibility(View.GONE);
                }
            }
        });

        //开启云服务器数据同步确保数据安全
        showTip();
    }

    @Override
    public void onResume() {
        super.onResume();
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
            DiBudget budget = DIBudgetService.getInstance(getContext()).findLastRow();
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
            mRecyclerView.setRefresh(true);
        }
    }

    @Override
    public void onRefresh() {
        if (!isLogin()) {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.tip)
                    .content(R.string.synchronize_tip)
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            //NEGATIVE   POSITIVE
                            if (which.name().equals("POSITIVE")) {
                                startActivity(new Intent(getContext(), LoginV2Activity.class));
                            }
                        }
                    })
                    .show();
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.refreshComplate();
                }
            }, 500);
            return;
        }
        synchronize();
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
        SaDataProccessHandler<Void, Void, List<DiIncome>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<DiIncome>>((BaseActivity) getActivity()) {
            @Override
            public void onSuccess(List<DiIncome> data) {
                for (DiIncome mDiIncome : data) {
                    mDiIncome.setIsupdate(CommonConstants.INCOME_RECORD_UPDATEED);
                    mDlIncomeService.update(mDiIncome);
                    //保存扫单
                    for (DiScan diScan : mDiIncome.getScanList()) {
                        diScan.setIsupdate(CommonConstants.INCOME_RECORD_UPDATEED);
                        DIScanService.getInstance(getContext()).update(diScan);
                    }

                }
                mSynchronizeCount += mDiIncomeList.size();
                //Snackbar.make(mRecyclerView, mDiIncomeList.size() + getString(R.string.synchronize_done), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //mRecyclerView.refreshComplate();
                synchronize();
            }

            @Override
            public void onPreExecute() {
            }

            @Override
            public void handlerError(SaException e) {
                super.handlerError(e);
                mRecyclerView.refreshComplate();
            }
        };

        IncomeService.getInstance().incomeCynchronize(mDiIncomeList, dataVerHandler);
    }


    private void synchronizeBudget() {
        Calendar calendar = Calendar.getInstance();
        final List<DiBudget> mDiBudgetList = DIBudgetService.getInstance(getContext()).findNotUpdate(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1));
        if (mDiBudgetList.size() == 0) {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mSynchronizeCount > 0) {
                            //Snackbar.make(mRecyclerView, mSynchronizeCount + getString(R.string.synchronize_done), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            Toast.makeText(getContext(), mSynchronizeCount + getString(R.string.synchronize_done), Toast.LENGTH_SHORT).show();
                        } else {
                            //Snackbar.make(mRecyclerView, mSynchronizeCount + getString(R.string.synchronize_done), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            Toast.makeText(getContext(), R.string.synchronize_done_tip, Toast.LENGTH_SHORT).show();
                        }
                        mRecyclerView.refreshComplate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1500);
            return;
        }
        //添加用户名和id
        for (DiBudget mDiBudget : mDiBudgetList) {
            mDiBudget.setUserid(ApplicationData.mDiUserInfo.getId());
            mDiBudget.setUsername(ApplicationData.mDiUserInfo.getName());
        }
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>((BaseActivity) getActivity()) {
            @Override
            public void onSuccess(Void data) {
                for (DiBudget mDiBudget : mDiBudgetList) {
                    mDiBudget.setIsupdate(CommonConstants.INCOME_RECORD_UPDATEED);
                    DIBudgetService.getInstance(getContext()).update(mDiBudget);
                }
                if (mSynchronizeCount > 0) {
                    //Snackbar.make(mRecyclerView, mSynchronizeCount + getString(R.string.synchronize_done), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Toast.makeText(getContext(), mSynchronizeCount + getString(R.string.synchronize_done), Toast.LENGTH_SHORT).show();
                    mSynchronizeCount = 0;
                } else {
                    //Snackbar.make(mRecyclerView, getString(R.string.synchronize_done_tip), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Toast.makeText(getContext(), R.string.synchronize_done_tip, Toast.LENGTH_SHORT).show();
                }
                mRecyclerView.refreshComplate();
            }

            @Override
            public void onPreExecute() {
            }

            @Override
            public void handlerError(SaException e) {
                super.handlerError(e);
                mRecyclerView.refreshComplate();
            }
        };

        IncomeService.getInstance().budgetCynchronize(mDiBudgetList, dataVerHandler);
    }

    @Override
    public void onLoadMore() {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int startNom = 0;
                if (mDiIncomeItems.size() > 0) {
                    startNom = mDiIncomeItems.size() - 1;
                }
                mDiIncomeItems.addAll(mDlIncomeService.getScrollData(startNom, CommonConstants.PAGE_LENGTH_NUMBER));
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mRecyclerView.loadMoreComplate();
            }
        }, 0);
    }

    private void showTip() {
        int x = (int) (Math.random() * 100);
        if (x > 97) {
            if (isLogin()) {
                return;
            }
            new MaterialDialog.Builder(getContext())
                    .title(R.string.tip)
                    .content(R.string.show_synchronize_tip)
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            //NEGATIVE   POSITIVE
                            if (which.name().equals("POSITIVE")) {
                                startActivity(new Intent(getContext(), LoginV2Activity.class));
                            }
                        }
                    })
                    .show();
        } else if (x > 95 && x <= 97) {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.tip)
                    .content(R.string.show_score_tip)
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            //NEGATIVE   POSITIVE
                            if (which.name().equals("POSITIVE")) {
                                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
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
                startActivity(new Intent(getContext(), IncomeListActivity.class));
                break;

            case R.id.home_item_layout:
                startActivity(new Intent(getContext(), SearchActivity.class));
                break;
        }
    }
}
