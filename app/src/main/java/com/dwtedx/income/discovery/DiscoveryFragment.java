package com.dwtedx.income.discovery;

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
import com.bumptech.glide.util.Util;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.discovery.adapter.DiscoveryHeaderRecyclerAdapter;
import com.dwtedx.income.discovery.adapter.DiscoveryRecyclerAdapter;
import com.dwtedx.income.entity.TaobaoActivityInfo;
import com.dwtedx.income.entity.TaobaoItemInfo;
import com.dwtedx.income.service.TaobaoService;
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
public class DiscoveryFragment extends BaseFragment implements SwipeRecyclerView.OnLoadListener, View.OnClickListener {

    @BindView(R.id.m_recyclerview)
    SwipeRecyclerView mRecyclerView;
    List<TaobaoItemInfo> mTaobaoItemInfoList;
    DiscoveryRecyclerAdapter mAdapter;

    ImageView mTaobaoActivityImages;
    RecyclerView mHeaderRecyclerView;
    DiscoveryHeaderRecyclerAdapter mDiscoveryHeaderRecyclerAdapter;

    @BindView(R.id.home_item_layout)
    LinearLayout mRightLayout;
    @BindView(R.id.m_discovery_scarch_view)
    LinearLayout mScarchView;

    @BindView(R.id.title_back_btn)
    LinearLayout mTitleBackBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_discovery, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        mRightLayout.setOnClickListener(this);
        mScarchView.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.getRecyclerView().setLayoutManager(layoutManager);

        mRecyclerView.setOnLoadListener(this);
        //自定义分割线的样式
        mRecyclerView.getRecyclerView().addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL, 0, ContextCompat.getColor(getActivity(), R.color.common_division_line)));
        mTaobaoItemInfoList = new ArrayList<>();
        mAdapter = new DiscoveryRecyclerAdapter(getActivity(), mTaobaoItemInfoList, mRecyclerView);

        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_discovery_header_view, mRecyclerView, false);
        mAdapter.setHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);

        //活动
        mTaobaoActivityImages = (ImageView) headerView.findViewById(R.id.m_taobao_activity_images);
        getTaobaoActivityInfo(false);

        //分类
        mHeaderRecyclerView = (RecyclerView) headerView.findViewById(R.id.m_recyclerview);
        RecyclerView.LayoutManager layoutManagerHeader = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mHeaderRecyclerView.setLayoutManager(layoutManagerHeader);

        //自定义分割线的样式
        mHeaderRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(getActivity(), R.color.common_division_line)));
        mHeaderRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(getActivity(), R.color.common_division_line)));

        mDiscoveryHeaderRecyclerAdapter = new DiscoveryHeaderRecyclerAdapter(getActivity());
        mHeaderRecyclerView.setAdapter(mDiscoveryHeaderRecyclerAdapter);


        //getTopTaobaoItemInfo(false, true);
        //refreshTaobaoIteminfo();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getTaobaoActivityInfo(final boolean isShow) {

        SaDataProccessHandler<Void, Void, TaobaoActivityInfo> dataVerHandler = new SaDataProccessHandler<Void, Void, TaobaoActivityInfo>((BaseActivity) getActivity()) {
            @Override
            public void onSuccess(final TaobaoActivityInfo data) {
                if(null == data){
                    return;
                }
                //if (Util.isOnMainThread())
                Glide.with(getActivity()).load(data.getImgUlr()).placeholder(R.mipmap.imageloader_default_width).error(R.mipmap.imageloader_default_width).into(mTaobaoActivityImages);
                mTaobaoActivityImages.setVisibility(View.VISIBLE);
                mTaobaoActivityImages.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TaobaoTradeUtility.showTaobaonActivityUrl(getActivity(), data.getPathUrl());
                    }
                });

            }

            @Override
            public void onPreExecute() {
                if (isShow) {
                    super.onPreExecute();
                }
            }

        };

        TaobaoService.getInstance().getTaobaoActivityInfo(dataVerHandler);
    }

    private void getTopTaobaoItemInfo(final boolean isShow, final boolean isClear) {

        SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>((BaseActivity) getActivity()) {
            @Override
            public void onSuccess(List<TaobaoItemInfo> data) {

                //第一次请求才缓存
                if (isClear) {
                    mTaobaoItemInfoList.clear();
                }

                mTaobaoItemInfoList.addAll(data);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.complete();

                //加载完成时
                if (data.size() == 0 || data.size() < CommonConstants.PAGE_LENGTH_NUMBER) {
                    mRecyclerView.onNoMore(getString(R.string.no_more));
                }

                //没有数据的时候
                if(isClear && data.size() == 0){
                    View nodataview = LayoutInflater.from(getActivity()).inflate(R.layout.layout_nodate_view, null, false);
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

        TaobaoService.getInstance().topTaobaoItem(isClear ? 0 : mTaobaoItemInfoList.size(), CommonConstants.PAGE_LENGTH_NUMBER, dataVerHandler);
    }


    private void refreshTaobaoIteminfo() {

        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>((BaseActivity) getActivity()) {

            @Override
            public void onSuccess(Void data) {

            }
        };

        TaobaoService.getInstance().refreshTaobaoIteminfo(dataVerHandler);
    }

    @Override
    public void onRefresh() {
        getTopTaobaoItemInfo(false, true);
    }

    @Override
    public void onLoadMore() {
        getTopTaobaoItemInfo(false, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.m_discovery_scarch_view:
                startActivity(new Intent(getActivity(), SearchTaobaoActivity.class));
                break;

            case R.id.home_item_layout:
                startActivity(new Intent(getActivity(), ItemCategoryTopActivity.class));
                break;
        }
    }
}
