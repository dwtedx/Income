package com.dwtedx.income.topic;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.DiTopic;
import com.dwtedx.income.profile.LoginV2Activity;
import com.dwtedx.income.service.TopicService;
import com.dwtedx.income.topic.adapter.TopicRecyclerAdapter;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.ToastUtil;
import com.dwtedx.income.widget.RecycleViewDivider;
import com.dwtedx.income.widget.swiperecyclerview.SwipeRecyclerView;
import com.previewlibrary.ZoomMediaLoader;

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

    public static boolean isRefresh;

    @BindView(R.id.home_item_layout)
    LinearLayout mRightLayout;
    @BindView(R.id.m_recyclerview)
    SwipeRecyclerView mRecyclerView;
    @BindView(R.id.m_progress_bar_view)
    ProgressBar mProgressBarView;
    RotateAnimation mRotateUpAnim;

    List<DiTopic> mDiTopicList;
    TopicRecyclerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_topic, container, false);
        ButterKnife.bind(this, rootView);

        ZoomMediaLoader.getInstance().init(new TopicImageLoader());

        mRightLayout.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mFragmentContext);
        mRecyclerView.getRecyclerView().setLayoutManager(layoutManager);

        mRecyclerView.setOnLoadListener(this);
        //自定义分割线的样式
        mRecyclerView.getRecyclerView().addItemDecoration(new RecycleViewDivider(mFragmentContext, LinearLayoutManager.HORIZONTAL, 32, ContextCompat.getColor(mFragmentContext, R.color.common_background_color)));
        mDiTopicList = new ArrayList<>();
        mAdapter = new TopicRecyclerAdapter(mFragmentContext, mDiTopicList);
        mRecyclerView.setAdapter(mAdapter);

        getTopicItemInfo(true, true);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isRefresh){
            isRefresh = false;
            getTopicItemInfo(true, true);
        }
        int visible = mProgressBarView.getVisibility();
         if(null != mRotateUpAnim &&  View.VISIBLE == visible) {
            mProgressBarView.startAnimation(mRotateUpAnim);
        }
    }

    private void getTopicItemInfo(final boolean isShow, final boolean isClear) {
        SaDataProccessHandler<Void, Void, List<DiTopic>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<DiTopic>>(mFragmentContext) {
            @Override
            public void onSuccess(List<DiTopic> data) {
                if(isShow){
                    hideLoading();
                }
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
                    View nodataview = LayoutInflater.from(mFragmentContext).inflate(R.layout.layout_nodate_view, null, false);
                    mRecyclerView.setEmptyView(nodataview);
                }
            }

            @Override
            public void onPreExecute() {
                //super.onPreExecute();
                if(isShow){
                    showLoading();
                }
            }

            @Override
            public void handlerError(SaException e) {
                //super.handlerError(e);
                //没有数据的时候
                View nodataview = LayoutInflater.from(mFragmentContext).inflate(R.layout.layout_nodate_view, null, false);
                mRecyclerView.setEmptyView(nodataview);
                mRecyclerView.complete();
                if(isShow){
                    hideLoading();
                }
            }
        };

        TopicService.getInstance().getTopicinfo(isClear ? 0 : mDiTopicList.size(), CommonConstants.PAGE_LENGTH_NUMBER, dataVerHandler);
    }

    private void showLoading(){
        if(isAdded()) {
            if(null == mRotateUpAnim) {
                mRotateUpAnim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                mRotateUpAnim.setInterpolator(new LinearInterpolator());
                mRotateUpAnim.setRepeatCount(Integer.MAX_VALUE);
                mRotateUpAnim.setDuration(600);
                mRotateUpAnim.setFillAfter(true);
            }
            mProgressBarView.startAnimation(mRotateUpAnim);
            mProgressBarView.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoading() {
        if(isAdded() && null != mRotateUpAnim) {
            mProgressBarView.clearAnimation();
            mProgressBarView.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.home_item_layout:
                if(!isLogin()){
                    ToastUtil.toastShow(R.string.topic_add_send_top);
                    startActivity(new Intent(mFragmentContext, LoginV2Activity.class));
                    return;
                }
                startActivity(new Intent(mFragmentContext, AddTopicActivity.class));
                break;
        }
    }


}
