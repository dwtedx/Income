package com.dwtedx.income.discovery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.discovery.adapter.DiscoveryCategoryRecyclerAdapter;
import com.dwtedx.income.discovery.adapter.DiscoveryRandRecyclerAdapter;
import com.dwtedx.income.entity.TaobaoItemInfo;
import com.dwtedx.income.entity.TbCategoryInfo;
import com.dwtedx.income.service.TaobaoService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ItemCategoryTopActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener {

    @BindView(R.id.m_app_title_bar)
    AppTitleBar mAppTitleBar;
    @BindView(R.id.m_recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.m_recyclerview_like)
    RecyclerView mRecyclerviewLike;
    @BindView(R.id.m_textview_like)
    TextView mTextviewLike;
    @BindView(R.id.m_discovery_scarch_view)
    LinearLayout mDiscoveryScarchView;

    List<TbCategoryInfo> mTaobaoCategoryInfoList;
    DiscoveryCategoryRecyclerAdapter mAdapterCategory;
    List<TaobaoItemInfo> mTaobaoItemInfoList;
    DiscoveryRandRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_category_top);
        ButterKnife.bind(this);

        mAppTitleBar.setOnTitleClickListener(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerview.setLayoutManager(layoutManager);

        //自定义分割线的样式
        mRecyclerview.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 0, ContextCompat.getColor(this, R.color.common_division_line)));
        mTaobaoCategoryInfoList = new ArrayList<>();
        mAdapterCategory = new DiscoveryCategoryRecyclerAdapter(this, mTaobaoCategoryInfoList);
        mRecyclerview.setAdapter(mAdapterCategory);

        //推荐
        RecyclerView.LayoutManager layoutManagerLike = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerviewLike.setLayoutManager(layoutManagerLike);

        //自定义分割线的样式
        mRecyclerviewLike.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 0, ContextCompat.getColor(this, R.color.common_division_line)));
        mTaobaoItemInfoList = new ArrayList<>();
        mAdapter = new DiscoveryRandRecyclerAdapter(this, mTaobaoItemInfoList);
        mRecyclerviewLike.setAdapter(mAdapter);

        mDiscoveryScarchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemCategoryTopActivity.this, SearchTaobaoActivity.class);
                startActivity(intent);
            }
        });

        getCategoryTop(true, false);

    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
        }
    }


    private void getCategoryTop(final boolean isShow, final boolean isClear) {

        SaDataProccessHandler<Void, Void, List<TbCategoryInfo>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<TbCategoryInfo>>(this) {
            @Override
            public void onSuccess(List<TbCategoryInfo> data) {

                //第一次请求才缓存
                if (isClear) {
                    mTaobaoItemInfoList.clear();
                }

                mTaobaoCategoryInfoList.addAll(data);
                mAdapterCategory.notifyDataSetChanged();
                mTextviewLike.setVisibility(View.VISIBLE);

                taobaoItemRank(false, false);

            }

            @Override
            public void onPreExecute() {
                if (isShow) {
                    super.onPreExecute();
                }
            }

        };

        TaobaoService.getInstance().getCategoryTop(dataVerHandler);
    }

    private void taobaoItemRank(final boolean isShow, final boolean isClear) {

        SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>(this) {
            @Override
            public void onSuccess(List<TaobaoItemInfo> data) {

                //第一次请求才缓存
                if (isClear) {
                    mTaobaoItemInfoList.clear();
                }

                mTaobaoItemInfoList.addAll(data);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onPreExecute() {
                if (isShow) {
                    super.onPreExecute();
                }
            }

        };

        TaobaoService.getInstance().getRandItem(isClear ? 0 : mTaobaoItemInfoList.size(), CommonConstants.PAGE_LENGTH_NUMBER, dataVerHandler);
    }

}
