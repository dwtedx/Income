package com.dwtedx.income.topic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.discovery.ItemCategoryTopActivity;
import com.dwtedx.income.discovery.SearchTaobaoActivity;
import com.dwtedx.income.discovery.TaobaoTradeUtility;
import com.dwtedx.income.discovery.adapter.DiscoveryHeaderRecyclerAdapter;
import com.dwtedx.income.discovery.adapter.DiscoveryRecyclerAdapter;
import com.dwtedx.income.entity.DiTopic;
import com.dwtedx.income.entity.TaobaoActivityInfo;
import com.dwtedx.income.entity.TaobaoItemInfo;
import com.dwtedx.income.service.TaobaoService;
import com.dwtedx.income.service.TopicService;
import com.dwtedx.income.topic.adapter.TopicRecyclerAdapter;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.widget.RecycleViewDivider;
import com.dwtedx.income.widget.swiperecyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/16.
 * Company 路之遥网络科技有限公司
 * Description TODO(这里用一句话描述这个类的作用)
 */
public class TopicFragment extends BaseFragment implements SwipeRecyclerView.OnLoadListener, View.OnClickListener {

    @BindView(R.id.home_item_layout)
    LinearLayout mRightLayout;
    @BindView(R.id.m_recyclerview)
    SwipeRecyclerView mRecyclerView;

    List<DiTopic> mDiTopicList;
    TopicRecyclerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_topic, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRightLayout.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.getRecyclerView().setLayoutManager(layoutManager);

        mRecyclerView.setOnLoadListener(this);
        //自定义分割线的样式
        mRecyclerView.getRecyclerView().addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 32, ContextCompat.getColor(getContext(), R.color.common_background_color)));
        mDiTopicList = new ArrayList<>();
        mAdapter = new TopicRecyclerAdapter(getContext(), mDiTopicList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        getTopicItemInfo(true, true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getTopicItemInfo(final boolean isShow, final boolean isClear) {

        SaDataProccessHandler<Void, Void, List<DiTopic>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<DiTopic>>((BaseActivity) getContext()) {
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

        TopicService.getInstance().getTopicinfo(isClear ? 0 : mDiTopicList.size(), CommonConstants.PAGE_LENGTH_NUMBER, dataVerHandler);
    }

    @Override
    public void onRefresh() {
        getTopicItemInfo(false, true);
    }

    @Override
    public void onLoadMore() {
        getTopicItemInfo(false, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.home_item_layout:
                startActivity(new Intent(getContext(), ItemCategoryTopActivity.class));
                break;
        }
    }
}
