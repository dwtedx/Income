package com.dwtedx.income.topic;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.DiTopic;
import com.dwtedx.income.service.TopicService;
import com.dwtedx.income.topic.adapter.TopicRecyclerAdapter;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.RecycleViewDivider;
import com.dwtedx.income.widget.swiperecyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyTopicActivity extends BaseActivity implements SwipeRecyclerView.OnLoadListener, AppTitleBar.OnTitleClickListener {

    public static boolean isRefresh;

    @BindView(R.id.m_app_title)
    AppTitleBar mAppTitle;
    @BindView(R.id.m_recyclerview)
    SwipeRecyclerView mRecyclerView;

    List<DiTopic> mDiTopicList;
    TopicRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_topic);
        ButterKnife.bind(this);

        mAppTitle.setOnTitleClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.getRecyclerView().setLayoutManager(layoutManager);

        mRecyclerView.setOnLoadListener(this);
        //自定义分割线的样式
        mRecyclerView.getRecyclerView().addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 32, ContextCompat.getColor(this, R.color.common_background_color)));
        mDiTopicList = new ArrayList<>();
        mAdapter = new TopicRecyclerAdapter(this, mDiTopicList);
        mRecyclerView.setAdapter(mAdapter);

        getTopicItemInfo(true, true);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(isRefresh){
            isRefresh = false;
            getTopicItemInfo(true, true);
        }
    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:

                break;
        }
    }

    @Override
    public void onRefresh() {
        getTopicItemInfo(false, true);
    }

    @Override
    public void onLoadMore() {
        getTopicItemInfo(false, false);
    }

    private void getTopicItemInfo(final boolean isShow, final boolean isClear) {

        SaDataProccessHandler<Void, Void, List<DiTopic>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<DiTopic>>(this) {
            @Override
            public void onSuccess(List<DiTopic> data) {

                //第一次请求才缓存
                if (isClear) {
                    mDiTopicList.clear();
                    mRecyclerView.setLoadMoreEnable(true);
                }

                mDiTopicList.addAll(data);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.complete();

                //加载完成时
                if (data.size() == 0 || data.size() < CommonConstants.PAGE_LENGTH_NUMBER) {
                    mRecyclerView.onNoMore(getString(R.string.no_more));
                    mRecyclerView.setLoadMoreEnable(false);
                }

                //没有数据的时候
                if(isClear && data.size() == 0){
                    View nodataview = LayoutInflater.from(getContext()).inflate(R.layout.layout_nodate_view, null, false);
                    mRecyclerView.setEmptyView(nodataview);
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
                mRecyclerView.complete();
            }
        };

        TopicService.getInstance().getMyTopicinfo(isClear ? 0 : mDiTopicList.size(), CommonConstants.PAGE_LENGTH_NUMBER, dataVerHandler);
    }

}
