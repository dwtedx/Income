package com.dwtedx.income.account;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.ProgressDialog;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.profile.LoginV2Activity;
import com.dwtedx.income.account.adapter.DiAccountProfileAdapter;
import com.dwtedx.income.sqliteservice.DIAccountService;
import com.dwtedx.income.widget.AppTitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/26 下午4:51.
 * Company 路之遥网络科技有限公司
 * Description 帐户管理
 */
public class AccountActivity extends BaseActivity implements AdapterView.OnItemClickListener, AppTitleBar.OnTitleClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {

    private AppTitleBar mAppTitleBar;
    private PullToRefreshListView mListView;
    private List<DiAccount> mDiAccountItems;
    private DiAccountProfileAdapter mDiAccountAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAppTitleBar = (AppTitleBar) findViewById(R.id.app_title);
        mAppTitleBar.setOnTitleClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLogin()){
                    startActivity(new Intent(AccountActivity.this, LoginV2Activity.class));
                    return;
                }
                startActivity(new Intent(AccountActivity.this, AddAccountActivity.class));
            }
        });

        mListView = (PullToRefreshListView) findViewById(R.id.listView);
        mDiAccountItems = new ArrayList<>();
        mDiAccountAdapter = new DiAccountProfileAdapter(this, mDiAccountItems);
        mListView.setAdapter(mDiAccountAdapter);
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
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
                    intent = new Intent(AccountActivity.this, LoginV2Activity.class);
                    startActivity(intent);
                    break;
                }
                intent = new Intent(AccountActivity.this, AddAccountActivity.class);
                startActivity(intent);
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

        mDiAccountItems.clear();
        mDiAccountItems.addAll(DIAccountService.getInstance(AccountActivity.this).findAll());
        mDiAccountAdapter.notifyDataSetChanged();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //showAccount(mDiAccountItems.get(position - 1));
        Intent intent = new Intent(this, AccountListActivity.class);
        intent.putExtra("ACCOUNTID", mDiAccountItems.get(position - 1).getId());
        intent.putExtra("ACCOUNTCOLOR", mDiAccountItems.get(position - 1).getColor());
        intent.putExtra("ACCOUNTICON", mDiAccountItems.get(position - 1).getIcon());
        intent.putExtra("ACCOUNTMONTY", mDiAccountItems.get(position - 1).getMoneysum());
        startActivity(intent);
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
