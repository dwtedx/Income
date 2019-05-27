package com.dwtedx.income.account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.home.adapter.AccountListAdapter;
import com.dwtedx.income.profile.LoginV2Activity;
import com.dwtedx.income.sqliteservice.DIAccountService;
import com.dwtedx.income.sqliteservice.DlIncomeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.AppTitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AccountListActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener, AppTitleBar.OnTitleClickListener  {

    private AppTitleBar mAppTitleBar;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatMonth = new SimpleDateFormat("yyyy-MM");
    private int mAccountId;
    private String mAccountColor;
    private String mAccountIcon;
    private double mAccountmoney;

    private TextView mHeaderMonthView;
    private TextView mHeaderIncomeView;
    private TextView mHeaderPayingView;
    private TextView mAccountMoney;
    private Calendar mTimeCalendar;
    private Calendar mEndTimeCalendar;
    private String mStartTime;
    private String mEndTime;

    private PullToRefreshListView mPullListView;
    private TextView mTextViewTip;
    private List<DiIncome> mDiIncomeItems;
    private AccountListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);

        mAppTitleBar = (AppTitleBar) findViewById(R.id.app_title);
        mAppTitleBar.setOnTitleClickListener(this);

        mAccountId = getIntent().getIntExtra("ACCOUNTID", 1);
        mAccountColor = getIntent().getStringExtra("ACCOUNTCOLOR");
        mAccountIcon = getIntent().getStringExtra("ACCOUNTICON");
        mAccountmoney = getIntent().getDoubleExtra("ACCOUNTMONTY", 0);

        mPullListView = (PullToRefreshListView) findViewById(R.id.listView);

        //添加header
        View headerView =  LayoutInflater.from(this).inflate(R.layout.activity_account_list_header, mPullListView.getRefreshableView(), false);
        String[] rgb = mAccountColor.split(",");
        int color = Color.rgb(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
        headerView.findViewById(R.id.nav_header_view).setBackgroundColor(color);
        ImageView mAccountImage = (ImageView) headerView.findViewById(R.id.account_money_image);
        //图片
        Glide.with(this).load(CommonUtility.getImageIdByName(mAccountIcon)).into(mAccountImage);
        mAccountMoney = (TextView) headerView.findViewById(R.id.account_money);
        mAccountMoney.setText(CommonUtility.doubleFormat(mAccountmoney));
        headerView.findViewById(R.id.account_month_money_view).setOnClickListener(this);
        headerView.findViewById(R.id.account_money_view).setOnClickListener(this);
        mHeaderMonthView = (TextView) headerView.findViewById(R.id.account_month_money);
        mHeaderIncomeView = (TextView) headerView.findViewById(R.id.account_month_income);
        mHeaderPayingView = (TextView) headerView.findViewById(R.id.account_month_paying);
        //日期相关
        mTimeCalendar = Calendar.getInstance();
        mStartTime = formatMonth.format(mTimeCalendar.getTime()) + "-01 00:00:00";
        mEndTimeCalendar = Calendar.getInstance();
        mEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        mEndTime = format.format(mEndTimeCalendar.getTime()) + "24:00:00";
        mHeaderMonthView.setText(formatMonth.format(mTimeCalendar.getTime()).split("-")[1]);

        mPullListView.getRefreshableView().addHeaderView(headerView);
        //添加header

        mTextViewTip = (TextView) findViewById(R.id.listView_tip);

        // 设置适配器
        mDiIncomeItems = new ArrayList<>();
        mAdapter = new AccountListAdapter(this, mDiIncomeItems);
        mPullListView.setAdapter(mAdapter);
        mTextViewTip.setVisibility((mDiIncomeItems.size() == 0) ? View.VISIBLE : View.INVISIBLE);
        mPullListView.setOnRefreshListener(this);
        //mPullListView.setOnItemClickListener(this);

    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                Intent intent;
                if(!isLogin()) {
                    intent = new Intent(AccountListActivity.this, LoginV2Activity.class);
                    startActivity(intent);
                    break;
                }
                showTransfer();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDiIncomeItems.clear();
        getIncomes();
    }

    private void getIncomes() {
        mDiIncomeItems.addAll(DlIncomeService.getInstance(AccountListActivity.this).getScrollDataByAccountId(mAccountId, mStartTime, mEndTime, mAdapter.getCount(), CommonConstants.PAGE_LENGTH_NUMBER));
        mTextViewTip.setVisibility((mDiIncomeItems.size() == 0) ? View.VISIBLE : View.INVISIBLE);
        mAdapter.notifyDataSetChanged();
        mPullListView.onRefreshComplete();

        mHeaderIncomeView.setText(CommonUtility.doubleFormat(DlIncomeService.getInstance(AccountListActivity.this)
                .getSumMoneyByDataAccount(CommonConstants.INCOME_ROLE_INCOME, mAccountId, mStartTime, mEndTime).getMoneysum()));
        mHeaderPayingView.setText(CommonUtility.doubleFormat(DlIncomeService.getInstance(AccountListActivity.this)
                .getSumMoneyByDataAccount(CommonConstants.INCOME_ROLE_PAYING, mAccountId, mStartTime, mEndTime).getMoneysum()));
        mAccountMoney.setText(CommonUtility.doubleFormat(DIAccountService.getInstance(AccountListActivity.this)
                .find(mAccountId).getMoneysum()));
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        mPullListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDiIncomeItems.clear();
                getIncomes();
            }
        }, 500);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        mPullListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                getIncomes();
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_month_money_view:
                Calendar today = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                mHeaderMonthView.setText(String.valueOf(selectedMonth + 1));
                                mTimeCalendar.set(selectedYear, selectedMonth, 1);
                                mStartTime = formatMonth.format(mTimeCalendar.getTime()) + "-01 00:00:00";

                                mEndTimeCalendar.set(selectedYear, selectedMonth, 1);
                                mEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                mEndTime = format.format(mEndTimeCalendar.getTime()) + "24:00:00";
                                mDiIncomeItems.clear();
                                getIncomes();
                            }
                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builder.setMinYear(1991)
                        .setTitle(getStringFromResources(R.string.report_date_select))
                        .setActivatedMonth(mTimeCalendar.get(Calendar.MONTH))
                        .setActivatedYear(mTimeCalendar.get(Calendar.YEAR))
                        .build()
                        .show();
                break;

            case R.id.account_money_view:
                showAccount();
                break;

        }
    }

    private void showAccount() {
        final DiAccount diAccount = DIAccountService.getInstance(AccountListActivity.this).find(mAccountId);

        if (diAccount.getUserid() == 0) {
            MaterialDialog md = new MaterialDialog.Builder(AccountListActivity.this)
                    .title(R.string.account_money)
                    .inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL)
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .input("0.00", CommonUtility.doubleFormat(diAccount.getMoneysum()), false, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            String text = input.toString();
                            if (text.contains(".")) {
                                int index = text.indexOf(".");
                                if (index + 3 < text.length()) {
                                    text = text.substring(0, index + 3);
                                }
                            }
                            diAccount.setMoneysum(Double.parseDouble(text));
                            DIAccountService.getInstance(AccountListActivity.this).update(diAccount);
                            mAccountMoney.setText(text);
                        }
                    }).show();
        }else{
            //自定义账户可以修改
            Intent intent = new Intent(this, AddAccountActivity.class);
            intent.putExtra("ACCOUNTID", mAccountId);
            startActivity(intent);
            finish();
        }
    }

    public void showTransfer() {
        Intent intent = new Intent(this, AccountTransferActivity.class);
        intent.putExtra("ACCOUNTID", mAccountId);
        startActivity(intent);
    }

}
