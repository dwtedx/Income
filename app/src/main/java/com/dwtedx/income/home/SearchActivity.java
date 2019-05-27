package com.dwtedx.income.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dwtedx.income.R;
import com.dwtedx.income.addrecord.IncomeDetailActivity;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.ProgressDialog;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.home.adapter.SearchHistoryAdapter;
import com.dwtedx.income.home.adapter.SearchListAdapter;
import com.dwtedx.income.sqliteservice.DlIncomeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.ParseJsonToObject;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.HorizontialListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈连杰 on 2016/2/16.
 * 热门搜索
 */
public class SearchActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, View.OnClickListener, PullToRefreshBase
        .OnRefreshListener2<ListView>, AdapterView.OnItemClickListener, View.OnFocusChangeListener, TextView.OnEditorActionListener {
    private AppTitleBar mCommonTitle;
    private ImageView mButSearch;
    private EditText mEtSearch;
    private PullToRefreshListView mListView;
    private TextView mClearHistory;
    private TextView mTextViewTip;
    private HorizontialListView mSearchHistory;
    private LinearLayout mLinearHistory;
    private RelativeLayout mListViewLinear;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<DiIncome> mDiIncomeItems;
    private List<String> searchHistoryList;

    private SearchListAdapter searchListAdapter;
    private SearchHistoryAdapter adapterHistory;
    private String keyword;//输入框内的字

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        searchHistoryList = getBlocklist();
        adapterHistory = new SearchHistoryAdapter(this, searchHistoryList);
        mSearchHistory.setAdapter(adapterHistory);
        isShowHistory();

        //搜索列表
        mDiIncomeItems = new ArrayList<>();
        searchListAdapter = new SearchListAdapter(this, mDiIncomeItems);
        mListView.setAdapter(searchListAdapter);
        mListView.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_search:
                searchIncome();
                break;
            case R.id.clear_history:
                editor.clear();
                editor.commit();
                searchHistoryList.clear();
                adapterNotifyData();
                break;
        }

    }

    private void searchIncome() {
        keyword = mEtSearch.getText().toString().trim();
        if (CommonUtility.isEmpty(keyword)) {
            Toast.makeText(this, getString(R.string.please_enter_query), Toast.LENGTH_SHORT).show();
            //判断输入的内容是否含有特殊字符
        } else if (CommonUtility.hasSpecialCharacter(keyword)) {
            Toast.makeText(this, getString(R.string.special_characters), Toast.LENGTH_SHORT).show();
        } else {
            mDiIncomeItems.clear();
            getSearchDetail(keyword, true, true);
            searchHistoryList.add(0, keyword);
            editor.putString(keyword, keyword);
        }
        editor.commit();
        adapterNotifyData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            //搜索记录
            case R.id.horizontial_search_history:
                String content = (String) adapterHistory.getItem(position);
                mEtSearch.setText(content);
                keyword = content;
                getSearchDetail(content, true, true);
                break;
            //搜索的列表
            case R.id.listview_search:
                DiIncome diIncome = mDiIncomeItems.get((position - 1));
                if(diIncome.getRole() == CommonConstants.INCOME_ROLE_START){
                    return;
                }
                Intent intent = new Intent(this, IncomeDetailActivity.class);
                intent.putExtra("income", ParseJsonToObject.getJsonFromObj(diIncome).toString());
                startActivity(intent);
                break;
        }
    }

    private void getSearchDetail(final String content, final boolean isClear, final boolean isShow) {
        mEtSearch.clearFocus();
        mClearHistory.setVisibility(View.GONE);
        mLinearHistory.setVisibility(View.GONE);
        mListViewLinear.setVisibility(View.VISIBLE);

        if(isShow) {
            showProgressDialog();
        }

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isClear) {
                    mDiIncomeItems.clear();
                }

                mDiIncomeItems.clear();
                mDiIncomeItems.addAll(DlIncomeService.getInstance(SearchActivity.this).query(content, searchListAdapter.getCount(), CommonConstants.PAGE_LENGTH_NUMBER));
                mTextViewTip.setVisibility((mDiIncomeItems.size() == 0) ? View.VISIBLE : View.INVISIBLE);
                searchListAdapter.notifyDataSetChanged();
                mListView.onRefreshComplete();

                if(isShow) {
                    cancelProgressDialog();
                }
            }
        }, 500);
    }

    //初始化控件
    private void initView() {
        mCommonTitle = (AppTitleBar) findViewById(R.id.common_title);
        mCommonTitle.setOnTitleClickListener(this);
        mButSearch = (ImageView) findViewById(R.id.but_search);
        mButSearch.setOnClickListener(this);
        mEtSearch = (EditText) findViewById(R.id.search_content);
        //mEtSearch.setOnClickListener(this);
        mEtSearch.setOnFocusChangeListener(this);
        mEtSearch.setOnEditorActionListener(this);
        mClearHistory = (TextView) findViewById(R.id.clear_history);
        mClearHistory.setVisibility(View.GONE);
        mClearHistory.setOnClickListener(this);
        mLinearHistory = (LinearLayout) findViewById(R.id.linear_history);
        //搜索后列表
        mListView = (PullToRefreshListView) findViewById(R.id.listview_search);
        mListViewLinear = (RelativeLayout) findViewById(R.id.linear_listview_search);
        mListView.setOnRefreshListener(this);
        //搜索记录
        mSearchHistory = (HorizontialListView) findViewById(R.id.horizontial_search_history);
        mSearchHistory.setOnItemClickListener(this);
        mTextViewTip = (TextView) findViewById(R.id.listView_tip);

    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
        }

    }

    /**
     * 获取SharedPreferences中的全部数据，放到List集合中。形成适配器的数据源
     */
    private List<String> getBlocklist() {
        List<String> list = new ArrayList<>();
        Map<String, ?> map = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    /**
     * 填充ListView控件，实现刷新显示数据的效果
     */
    private void adapterNotifyData() {
        //searchHistoryList.clear();
        //searchHistoryList.addAll(getBlocklist());
        adapterHistory.notifyDataSetChanged();
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        getSearchDetail(keyword, true, false);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        getSearchDetail(keyword, false, false);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.search_content:
                if (hasFocus) {
                    isShowHistory();
                    mListViewLinear.setVisibility(View.GONE);
                } else {

                }
                break;
        }
    }

    private void isShowHistory() {
        if (searchHistoryList != null && searchHistoryList.size() != 0) {
            mLinearHistory.setVisibility(View.VISIBLE);
            mClearHistory.setVisibility(View.VISIBLE);
            adapterNotifyData();
        } else {
            mClearHistory.setVisibility(View.GONE);
            mLinearHistory.setVisibility(View.GONE);
        }
    }

    public void showProgressDialog() {
        mProgressDialog = getProgressDialog();
        mProgressDialog.show();
    }

    public void cancelProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        /*判断是否是“搜索”键*/
        if(actionId == EditorInfo.IME_ACTION_SEARCH){
            //  下面就是大家的业务逻辑
            searchIncome();
            //  这里记得一定要将键盘隐藏了
            return false;//返回true，保留软键盘。false，隐藏软键盘
        }
        return false;
    }
}