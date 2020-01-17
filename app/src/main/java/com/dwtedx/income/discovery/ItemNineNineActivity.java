package com.dwtedx.income.discovery;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.discovery.adapter.DiscoveryRecyclerAdapter;
import com.dwtedx.income.entity.TaobaoItemInfo;
import com.dwtedx.income.service.TaobaoService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.RecycleViewDivider;
import com.dwtedx.income.widget.swiperecyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ItemNineNineActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, SwipeRecyclerView.OnLoadListener {

    @BindView(R.id.m_app_title_bar)
    AppTitleBar mAppTitleBar;
    @BindView(R.id.m_recyclerview)
    SwipeRecyclerView mRecyclerview;

    List<TaobaoItemInfo> mTaobaoItemInfoList;
    DiscoveryRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_ninenine);
        ButterKnife.bind(this);

        mAppTitleBar.setOnTitleClickListener(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerview.getRecyclerView().setLayoutManager(layoutManager);

        mRecyclerview.setOnLoadListener(this);
        //自定义分割线的样式
        mRecyclerview.getRecyclerView().addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 0, ContextCompat.getColor(this, R.color.common_division_line)));
        mTaobaoItemInfoList = new ArrayList<>();
        mAdapter = new DiscoveryRecyclerAdapter(this, mTaobaoItemInfoList, mRecyclerview);

        View headerView = LayoutInflater.from(this).inflate(R.layout.fragment_ninenine_header_view, mRecyclerview, false);
        mAdapter.setHeaderView(headerView);

        mRecyclerview.setAdapter(mAdapter);

        //taobaoItemNineNine(true, false);

    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
        }
    }


    private void taobaoItemNineNine(final boolean isShow, final boolean isClear) {

        SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>(this) {
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

        TaobaoService.getInstance().getNineNineItem(isClear ? 0 : mTaobaoItemInfoList.size(), CommonConstants.PAGE_LENGTH_NUMBER, dataVerHandler);
    }

    @Override
    public void onRefresh() {
        taobaoItemNineNine(false, true);
    }

    @Override
    public void onLoadMore() {
        taobaoItemNineNine(false, false);
    }

}
