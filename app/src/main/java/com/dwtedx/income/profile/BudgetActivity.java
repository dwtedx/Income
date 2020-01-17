package com.dwtedx.income.profile;

import android.os.Bundle;
import androidx.annotation.NonNull;

import android.text.InputType;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.ProgressDialog;
import com.dwtedx.income.entity.DiBudget;
import com.dwtedx.income.profile.adapter.DiBudgetAdapter;
import com.dwtedx.income.provider.BudgetSharedPreferences;
import com.dwtedx.income.sqliteservice.DIBudgetService;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.AppTitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/26 下午4:51.
 * Company 路之遥网络科技有限公司
 * Description 月预算
 */
public class BudgetActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {

    private AppTitleBar mAppTitleBar;
    private PullToRefreshListView mListView;
    private List<DiBudget> mDiBudgetList;
    private DiBudgetAdapter mAcapter;

    private ProgressDialog mProgressDialog;

    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        mAppTitleBar = (AppTitleBar) findViewById(R.id.app_title);
        mAppTitleBar.setOnTitleClickListener(this);

        mListView = (PullToRefreshListView) findViewById(R.id.listView);
        mDiBudgetList = new ArrayList<>();
        mAcapter = new DiBudgetAdapter(this, mDiBudgetList);
        mListView.setAdapter(mAcapter);
        mListView.setOnRefreshListener(this);

        BudgetSharedPreferences.init(BudgetActivity.this);
    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                setMonth();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDiIncomeItems();
    }

    private void getDiIncomeItems() {
        showProgressDialog();

        mDiBudgetList.clear();
        mDiBudgetList.addAll(DIBudgetService.getInstance(BudgetActivity.this).findAll());
        mAcapter.notifyDataSetChanged();
        mListView.onRefreshComplete();

        cancelProgressDialog();
    }

    public void showProgressDialog() {
        mProgressDialog = getProgressDialog();
        mProgressDialog.show();
    }

    public void cancelProgressDialog() {
        mProgressDialog.cancel();
    }

    private void setMonth() {
            MaterialDialog md = new MaterialDialog.Builder(BudgetActivity.this)
                .title(R.string.budget_month_set)
                .inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .input("0.00", CommonUtility.twoPlaces(BudgetSharedPreferences.getBudget()), false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        text = input.toString();
                        if (text.contains(".")) {
                            int index = text.indexOf(".");
                            if (index + 3 < text.length()) {
                                text = text.substring(0, index + 3);
                            }
                        }

                    }
                }).onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //NEGATIVE   POSITIVE
                        if(which.name().equals("POSITIVE")){
                            float montyNew = Float.parseFloat(text);
                            BudgetSharedPreferences.setBudget(montyNew);
                            DiBudget mDiBudget = DIBudgetService.getInstance(BudgetActivity.this).findLastRow();
                            double monthNewMath = mDiBudget.getMoneysum() - montyNew;
                            double monthNewLast = mDiBudget.getMoneylast() - monthNewMath;
                            mDiBudget.setMoneysum(montyNew);
                            mDiBudget.setMoneylast(monthNewLast);
                            DIBudgetService.getInstance(BudgetActivity.this).update(mDiBudget);
                            //BudgetActivity.this.finish();
                            getDiIncomeItems();
                        }
                    }
                }).show();

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDiIncomeItems();
            }
        }, 500);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDiIncomeItems();

            }
        }, 500);
    }

}
