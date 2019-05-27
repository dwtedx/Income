package com.dwtedx.income.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.dwtedx.income.R;
import com.dwtedx.income.addrecord.AddRecordActivity;
import com.dwtedx.income.addrecord.IncomeDetailActivity;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.home.adapter.IncomeListAdapter;
import com.dwtedx.income.provider.ScanTipSharedPreferences;
import com.dwtedx.income.scan.ScanDetailActivity;
import com.dwtedx.income.scan.ScanResultActivity;
import com.dwtedx.income.sqliteservice.DlIncomeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.ParseJsonToObject;
import com.dwtedx.income.widget.AppTitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class IncomeListActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener, View.OnClickListener, AppTitleBar.OnTitleClickListener {

    private AppTitleBar mAppTitleBar;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatMonth = new SimpleDateFormat("yyyy-MM");
    private SimpleDateFormat formatMonthStr = new SimpleDateFormat("yyyy年MM月");
    private List<DiIncome> diIncomes;
    private RelativeLayout mHeaderRelativeLayout;
    private TextView mHeaderMoneyView;
    private TextView mHeaderMoneyTypeView;
    private TextView mHeaderIncomeView;
    private TextView mHeaderPayingView;
    private TextView mHeaderIncomeTipView;
    private TextView mHeaderPayingTipView;
    private int mShowType;

    private PullToRefreshListView mPullListView;
    private TextView mTextViewTip;
    private List<DiIncome> mDiIncomeItems;
    private IncomeListAdapter mAdapter;

    private Calendar mTimeCalendar;
    private String mStartTime;
    private Calendar mEndTimeCalendar;
    private String mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_list);
        mAppTitleBar = (AppTitleBar) findViewById(R.id.app_title);
        mAppTitleBar.setOnTitleClickListener(this);


        mPullListView = (PullToRefreshListView) findViewById(R.id.listView);
        mTextViewTip = (TextView) findViewById(R.id.listView_tip);
        mTextViewTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IncomeListActivity.this, AddRecordActivity.class));
            }
        });

        //添加header
        View headerView = LayoutInflater.from(this).inflate(R.layout.activity_income_list_header, mPullListView.getRefreshableView(), false);
        mHeaderIncomeView = (TextView) headerView.findViewById(R.id.account_month_income);
        mHeaderPayingView = (TextView) headerView.findViewById(R.id.account_month_paying);
        mHeaderIncomeTipView = (TextView) headerView.findViewById(R.id.account_month_income_tip);
        mHeaderPayingTipView = (TextView) headerView.findViewById(R.id.account_month_paying_tip);
        mHeaderMoneyView = (TextView) headerView.findViewById(R.id.account_money);
        mHeaderRelativeLayout = (RelativeLayout) headerView.findViewById(R.id.nav_header_view);
        mHeaderRelativeLayout.setOnClickListener(this);
        mHeaderMoneyTypeView = (TextView) headerView.findViewById(R.id.account_money_type);
        //添加header
        mPullListView.getRefreshableView().addHeaderView(headerView);
        diIncomes = new ArrayList<>();

        // 设置适配器
        mDiIncomeItems = new ArrayList<>();
        mAdapter = new IncomeListAdapter(this, mDiIncomeItems);
        mPullListView.setAdapter(mAdapter);
        mPullListView.setOnRefreshListener(this);
        mPullListView.setOnItemClickListener(this);

        mTimeCalendar = Calendar.getInstance();
        mEndTimeCalendar = Calendar.getInstance();
        mEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    @Override
    protected void onResume() {
        super.onResume();
        showIncome();
    }

    public void showIncome() {
        mStartTime = formatMonth.format(mTimeCalendar.getTime()) + "-01 00:00:00";
        mEndTime = format.format(mEndTimeCalendar.getTime()) + "24:00:00";

        mHeaderIncomeView.setText(CommonUtility.doubleFormat(DlIncomeService.getInstance(IncomeListActivity.this)
                .getSumMoneyByData(CommonConstants.INCOME_ROLE_INCOME, mStartTime, mEndTime).getMoneysum()));
        mHeaderPayingView.setText(CommonUtility.doubleFormat(DlIncomeService.getInstance(IncomeListActivity.this)
                .getSumMoneyByData(CommonConstants.INCOME_ROLE_PAYING, mStartTime, mEndTime).getMoneysum()));
        mHeaderIncomeTipView.setText(formatMonthStr.format(mTimeCalendar.getTime()) + getString(R.string.record_income));
        mHeaderPayingTipView.setText(formatMonthStr.format(mTimeCalendar.getTime()) + getString(R.string.record_pay));
        //按类型显示
        diIncomes.clear();
        diIncomes.addAll(DlIncomeService.getInstance(IncomeListActivity.this).getByTypeSumMoneyData(CommonConstants.INCOME_ROLE_PAYING, mStartTime, mEndTime));
        diIncomes.addAll(DlIncomeService.getInstance(IncomeListActivity.this).getByTypeSumMoneyData(CommonConstants.INCOME_ROLE_INCOME, mStartTime, mEndTime));
        if (diIncomes.size() > 0) {
            mHeaderMoneyView.setText(CommonUtility.doubleFormat(diIncomes.get(0).getMoneysum()));
            String type = diIncomes.get(0).getRole() == CommonConstants.INCOME_ROLE_PAYING ? getString(R.string.record_pay) : getString(R.string.record_income);
            mHeaderMoneyTypeView.setText(diIncomes.get(0).getType() + type);
            //颜色
            String[] colorArr = null != diIncomes.get(0).getColor() ? diIncomes.get(0).getColor().split(",") : CommonConstants.PAYTYPE_DIY_COLOR.split(",");
            int color = Color.rgb(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2]));
            mHeaderRelativeLayout.setBackgroundColor(color);
            mShowType = 0;

            //引导
            showGuide();
        }else {
            mHeaderMoneyView.setText("0.00");
            mHeaderMoneyTypeView.setText("");
        }

        //数据
        mDiIncomeItems.clear();
        mDiIncomeItems.addAll(DlIncomeService.getInstance(IncomeListActivity.this).getScrollData(mAdapter.getCount(), CommonConstants.PAGE_LENGTH_NUMBER));
        mTextViewTip.setVisibility((mDiIncomeItems.size() == 0) ? View.VISIBLE : View.INVISIBLE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                Calendar today = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                mTimeCalendar.set(selectedYear, selectedMonth, 1);
                                mEndTimeCalendar.set(selectedYear, selectedMonth, 1);
                                mEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                showIncome();
                            }
                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builder.setMinYear(1991)
                        .setTitle(getStringFromResources(R.string.report_date_select))
                        .setActivatedMonth(mTimeCalendar.get(Calendar.MONTH))
                        .setActivatedYear(mTimeCalendar.get(Calendar.YEAR))
                        .build()
                        .show();
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        mPullListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDiIncomeItems.clear();
                mDiIncomeItems.addAll(DlIncomeService.getInstance(IncomeListActivity.this).getScrollData(mAdapter.getCount(), CommonConstants.PAGE_LENGTH_NUMBER));
                mTextViewTip.setVisibility((mDiIncomeItems.size() == 0) ? View.VISIBLE : View.INVISIBLE);
                mAdapter.notifyDataSetChanged();
                mPullListView.onRefreshComplete();
            }
        }, 500);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        mPullListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDiIncomeItems.addAll(DlIncomeService.getInstance(IncomeListActivity.this).getScrollData(mAdapter.getCount(), CommonConstants.PAGE_LENGTH_NUMBER));
                mTextViewTip.setVisibility((mDiIncomeItems.size() == 0) ? View.VISIBLE : View.INVISIBLE);
                mAdapter.notifyDataSetChanged();
                mPullListView.onRefreshComplete();
            }
        }, 500);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 1) {
            return;
        }
        final DiIncome diIncome = mDiIncomeItems.get((position - 2));
        if (diIncome.getRole() == CommonConstants.INCOME_ROLE_START) {
            return;
        }
        if (CommonConstants.INCOME_RECORD_TYPE_1 == diIncome.getRecordtype()) {
            new MaterialDialog.Builder(this)
                    .items(R.array.income_detail_mode)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (which == 0) {
                                Intent intent = new Intent(IncomeListActivity.this, ScanDetailActivity.class);
                                intent.putExtra("income", ParseJsonToObject.getJsonFromObj(diIncome).toString());
                                startActivity(intent);
                            } else if (which == 1) {
                                Intent intent = new Intent(IncomeListActivity.this, IncomeDetailActivity.class);
                                intent.putExtra("income", ParseJsonToObject.getJsonFromObj(diIncome).toString());
                                startActivity(intent);
                            }
                        }
                    }).show();
        } else {
            Intent intent = new Intent(this, IncomeDetailActivity.class);
            intent.putExtra("income", ParseJsonToObject.getJsonFromObj(diIncome).toString());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header_view:
                if (diIncomes.size() > 0) {
                    if (mShowType < (diIncomes.size() - 1)) {
                        mShowType++;
                        mHeaderMoneyView.setText(CommonUtility.doubleFormat(diIncomes.get(mShowType).getMoneysum()));
                        String type = diIncomes.get(mShowType).getRole() == CommonConstants.INCOME_ROLE_PAYING ? getString(R.string.record_pay) : getString(R.string.record_income);
                        mHeaderMoneyTypeView.setText(diIncomes.get(mShowType).getType() + type);
                        //颜色
                        String[] colorArr = null != diIncomes.get(mShowType).getColor() ? diIncomes.get(mShowType).getColor().split(",") : CommonConstants.PAYTYPE_DIY_COLOR.split(",");
                        int color = Color.rgb(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2]));
                        mHeaderRelativeLayout.setBackgroundColor(color);
                        mHeaderMoneyView.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
                        mHeaderMoneyTypeView.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
                    } else {
                        mShowType = 0;
                        mHeaderMoneyView.setText(CommonUtility.doubleFormat(diIncomes.get(mShowType).getMoneysum()));
                        String type = diIncomes.get(mShowType).getRole() == CommonConstants.INCOME_ROLE_PAYING ? getString(R.string.record_pay) : getString(R.string.record_income);
                        mHeaderMoneyTypeView.setText(diIncomes.get(mShowType).getType() + type);
                        //颜色
                        String[] colorArr = null != diIncomes.get(mShowType).getColor() ? diIncomes.get(mShowType).getColor().split(",") : CommonConstants.PAYTYPE_DIY_COLOR.split(",");
                        int color = Color.rgb(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2]));
                        mHeaderRelativeLayout.setBackgroundColor(color);
                        mHeaderMoneyView.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
                        mHeaderMoneyTypeView.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
                    }
                }
                break;
        }
    }

    private void showGuide() {
        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(600);
        enterAnimation.setFillAfter(true);
        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(300);
        exitAnimation.setFillAfter(true);
        NewbieGuide.with(this)
                .setLabel("income_detail_guide")
                .alwaysShow(false)//总是显示，调试时可以打开
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(mHeaderRelativeLayout, HighLight.Shape.RECTANGLE, 5)
                        .setLayoutRes(R.layout.activity_income_list_header_tip)
                        .setEnterAnimation(enterAnimation)//进入动画
                        .setExitAnimation(exitAnimation))//退出动画
                .show();
    }

}
