package com.dwtedx.income.discovery;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.discovery.adapter.DiscoveryRecommendRecyclerAdapter;
import com.dwtedx.income.entity.TaobaoItemInfo;
import com.dwtedx.income.entity.TaobaoSearchItemInfo;
import com.dwtedx.income.service.TaobaoService;
import com.dwtedx.income.utility.ParseJsonToObject;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.RecycleViewDivider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ItemRecommendActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener  {

    private AppTitleBar mAppTitleBar;
    private RecyclerView mRecyclerview;

    TaobaoSearchItemInfo mTaobaoSearchItemInfo;

    List<TaobaoSearchItemInfo> mTaobaoSearchItemInfoList;
    DiscoveryRecommendRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_recommend);

        mAppTitleBar = (AppTitleBar) findViewById(R.id.app_title);
        mAppTitleBar.setOnTitleClickListener(this);

        try {
            TaobaoItemInfo taobaoItemInfo = ParseJsonToObject.getObject(TaobaoItemInfo.class, new JSONObject(getIntent().getStringExtra("itemInfo")));

            String title = taobaoItemInfo.getStoreName();
            title = title.replace("旗舰", "");
            title = title.replace("店", "");
            mAppTitleBar.setTitleText(title + getString(R.string.discovery_recommend_title));

            mTaobaoSearchItemInfo = new  TaobaoSearchItemInfo();
            mTaobaoSearchItemInfo.setNum_iid(taobaoItemInfo.getNumIid());
            mTaobaoSearchItemInfo.setPict_url(taobaoItemInfo.getPictUrl());
            mTaobaoSearchItemInfo.setTitle(taobaoItemInfo.getTitle());
            mTaobaoSearchItemInfo.setReserve_price(taobaoItemInfo.getZkFinalPrice());
            mTaobaoSearchItemInfo.setZk_final_price(taobaoItemInfo.getPrice());
            mTaobaoSearchItemInfo.setUser_type(taobaoItemInfo.getUserType());
            mTaobaoSearchItemInfo.setTag_content(getString(R.string.discovery_tag_item));
            mTaobaoSearchItemInfo.setVolume(taobaoItemInfo.getVolume());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerview = (RecyclerView) findViewById(R.id.m_recyclerview);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerview.setLayoutManager(layoutManager);

        //自定义分割线的样式
        mRecyclerview.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 0, ContextCompat.getColor(this, R.color.common_division_line)));
        mTaobaoSearchItemInfoList = new ArrayList<>();
        mTaobaoSearchItemInfoList.add(mTaobaoSearchItemInfo);
        mAdapter = new DiscoveryRecommendRecyclerAdapter(this, mTaobaoSearchItemInfoList);
        mRecyclerview.setAdapter(mAdapter);

        taobaoItemRecommend(true, false);

    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
        }
    }


    private void taobaoItemRecommend(final boolean isShow, final boolean isClear) {

        SaDataProccessHandler<Void, Void, List<TaobaoSearchItemInfo>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<TaobaoSearchItemInfo>>(this) {
            @Override
            public void onSuccess(List<TaobaoSearchItemInfo> data) {

                //第一次请求才缓存
                if (isClear) {
                    mTaobaoSearchItemInfoList.clear();
                }

                mTaobaoSearchItemInfoList.addAll(data);
                mAdapter.notifyDataSetChanged();
            }

        };

        TaobaoService.getInstance().taobaoItemRecommend(mTaobaoSearchItemInfo.getNum_iid(), dataVerHandler);
    }



}
