package com.dwtedx.income.discovery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.discovery.adapter.DiscoveryRecyclerAdapter;
import com.dwtedx.income.entity.TaobaoItemInfo;
import com.dwtedx.income.service.TaobaoService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.widget.RecycleViewDivider;
import com.dwtedx.income.widget.swiperecyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LuggageFragment extends BaseFragment implements SwipeRecyclerView.OnLoadListener {

    @BindView(R.id.m_recyclerview)
    SwipeRecyclerView mRecyclerview;

    List<TaobaoItemInfo> mTaobaoItemInfoList;
    DiscoveryRecyclerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_live_luggage, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerview.getRecyclerView().setLayoutManager(layoutManager);

        mRecyclerview.setOnLoadListener(this);
        mRecyclerview.setRefreshing(true);
        //自定义分割线的样式
        mRecyclerview.getRecyclerView().addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 0, ContextCompat.getColor(getContext(), R.color.common_division_line)));
        mTaobaoItemInfoList = new ArrayList<>();
        mAdapter = new DiscoveryRecyclerAdapter(getContext(), mTaobaoItemInfoList, mRecyclerview);
        mRecyclerview.setAdapter(mAdapter);

    }

    private void getTaobaoItem(final boolean isShow, final boolean isClear) {

        SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>((BaseActivity) getActivity()) {
            @Override
            public void onSuccess(List<TaobaoItemInfo> data) {

                //第一次请求才缓存
                if (isClear) {
                    mTaobaoItemInfoList.clear();
                }

                mTaobaoItemInfoList.addAll(data);
                mAdapter.notifyDataSetChanged();
                mRecyclerview.complete();

                //加载完成时
                if (data.size() == 0 || data.size() < CommonConstants.PAGE_LENGTH_NUMBER) {
                    mRecyclerview.onNoMore(getString(R.string.no_more));
                }

                //没有数据的时候
                if (isClear && data.size() == 0) {
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

        TaobaoService.getInstance().getLuggageItem(isClear ? 0 : mTaobaoItemInfoList.size(), CommonConstants.PAGE_LENGTH_NUMBER, dataVerHandler);
    }


    @Override
    public void onRefresh() {
        getTaobaoItem(false, true);
    }

    @Override
    public void onLoadMore() {
        getTaobaoItem(false, false);
    }

}
