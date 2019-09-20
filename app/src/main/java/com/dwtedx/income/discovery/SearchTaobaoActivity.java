package com.dwtedx.income.discovery;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.discovery.adapter.DiscoverySearchRecyclerAdapter;
import com.dwtedx.income.entity.TaobaoSearchItemInfo;
import com.dwtedx.income.home.adapter.SearchHistoryAdapter;
import com.dwtedx.income.service.TaobaoService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.HorizontialListView;
import com.dwtedx.income.widget.RecycleViewDivider;
import com.dwtedx.income.widget.swiperecyclerview.SwipeRecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈连杰 on 2016/2/16.
 * 热门搜索
 */
public class SearchTaobaoActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, View.OnClickListener, AdapterView.OnItemClickListener, SwipeRecyclerView.OnLoadListener, View.OnFocusChangeListener, TextView.OnEditorActionListener  {
    private AppTitleBar mAppTitleBar;
    private ImageView mButSearch;
    private EditText mEtSearch;
    private SwipeRecyclerView mRecyclerview;
    private TextView mClearHistory;
    private HorizontialListView mSearchHistory;
    private LinearLayout mLinearHistory;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<TaobaoSearchItemInfo> mTaobaoItemInfoList;
    private List<String> searchHistoryList;
    private DiscoverySearchRecyclerAdapter searchListAdapter;
    private SearchHistoryAdapter adapterHistory;
    private String keyword;//输入框内的字

    //是否还要继续加载
    private boolean mIsLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_taobao);
        initView();
        sharedPreferences = getSharedPreferences("config_toabao", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        searchHistoryList = getBlocklist();
        adapterHistory = new SearchHistoryAdapter(this, searchHistoryList);
        mSearchHistory.setAdapter(adapterHistory);
        isShowHistory();


        //搜索列表
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerview.getRecyclerView().setLayoutManager(layoutManager);

        mRecyclerview.setOnLoadListener(this);
        //自定义分割线的样式
        mRecyclerview.getRecyclerView().addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 0, ContextCompat.getColor(this, R.color.common_division_line)));
        mTaobaoItemInfoList = new ArrayList<TaobaoSearchItemInfo>();
        searchListAdapter = new DiscoverySearchRecyclerAdapter(this, mTaobaoItemInfoList, mRecyclerview);

        mRecyclerview.setAdapter(searchListAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_search:
                searchTaobaoItem();
                break;
            case R.id.search_content:
                mLinearHistory.setVisibility(View.VISIBLE);
                mClearHistory.setVisibility(View.VISIBLE);
                mRecyclerview.setVisibility(View.GONE);
                break;
            case R.id.clear_history:
                editor.clear();
                editor.commit();
                searchHistoryList.clear();
                adapterNotifyData();
                break;
        }

    }

    private void searchTaobaoItem() {
        keyword = mEtSearch.getText().toString().trim();
        if (CommonUtility.isEmpty(keyword)) {
            Toast.makeText(this, getString(R.string.please_enter_query_tip), Toast.LENGTH_SHORT).show();
            //判断输入的内容是否含有特殊字符
        } else if (CommonUtility.hasSpecialCharacter(keyword)) {
            Toast.makeText(this, getString(R.string.special_characters), Toast.LENGTH_SHORT).show();
        } else {
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
        }
    }

    //点击搜索的网络请求
    private void getSearchDetail(final String word, final boolean isShow, final boolean isClear) {
        mEtSearch.clearFocus();
        SaDataProccessHandler<Void, Void, List<TaobaoSearchItemInfo>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<TaobaoSearchItemInfo>>(this) {
            @Override
            public void onSuccess(List<TaobaoSearchItemInfo> data) {

                //第一次请求才缓存
                if (isClear) {
                    mTaobaoItemInfoList.clear();
                }

                mTaobaoItemInfoList.addAll(data);
                searchListAdapter.notifyDataSetChanged();
                mRecyclerview.complete();

                mClearHistory.setVisibility(View.GONE);
                mLinearHistory.setVisibility(View.GONE);
                mRecyclerview.setVisibility(View.VISIBLE);

                //加载完成时
                if (data.size() == 0 || data.size() < CommonConstants.PAGE_LENGTH_NUMBER) {
                    mRecyclerview.onNoMore(getString(R.string.no_more));
                }

                //没有数据的时候
                if(isClear && data.size() == 0){
                    View nodataview = LayoutInflater.from(getContext()).inflate(R.layout.layout_nodate_view, null, false);
                    mRecyclerview.setEmptyView(nodataview);
                }
            }

            @Override
            public void onPreExecute() {
                if (isShow) {
                    super.onPreExecute();
                }
            }

            @Override
            public void handlerError(SaException e) {
                super.handlerError(e);
                mRecyclerview.complete();
            }

        };

        TaobaoService.getInstance().taobaoItemSearch(word, isClear ? 0 : mTaobaoItemInfoList.size(), CommonConstants.PAGE_LENGTH_NUMBER, dataVerHandler);
    }

    //初始化控件
    private void initView() {
        mAppTitleBar = (AppTitleBar) findViewById(R.id.common_title);
        mAppTitleBar.setOnTitleClickListener(this);
        mButSearch = (ImageView) findViewById(R.id.but_search);
        mButSearch.setOnClickListener(this);
        mEtSearch = (EditText) findViewById(R.id.search_content);
        mEtSearch.setOnFocusChangeListener(this);
        mEtSearch.setOnEditorActionListener(this);
        mClearHistory = (TextView) findViewById(R.id.clear_history);
        mClearHistory.setVisibility(View.GONE);
        mClearHistory.setOnClickListener(this);
        mLinearHistory = (LinearLayout) findViewById(R.id.linear_history);
        //搜索后列表
        mRecyclerview = (SwipeRecyclerView) findViewById(R.id.listview_search);

        //搜索记录
        mSearchHistory = (HorizontialListView) findViewById(R.id.horizontial_search_history);
        mSearchHistory.setOnItemClickListener(this);

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
        List<String> list = new ArrayList<String>();
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
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.search_content:
                if (hasFocus) {
                    isShowHistory();
                    mRecyclerview.setVisibility(View.GONE);
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

    @Override
    public void onRefresh() {
        getSearchDetail(keyword, false, true);
    }

    @Override
    public void onLoadMore() {
        getSearchDetail(keyword, false, false);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        /*判断是否是“搜索”键*/
        if(actionId == EditorInfo.IME_ACTION_SEARCH){
            //  下面就是大家的业务逻辑
            searchTaobaoItem();
            //  这里记得一定要将键盘隐藏了
            return false;//返回true，保留软键盘。false，隐藏软键盘
        }
        return false;
    }
}