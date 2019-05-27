package com.dwtedx.income.discovery.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.bumptech.glide.Glide;
import com.dwtedx.income.R;
import com.dwtedx.income.discovery.ItemRecommendActivity;
import com.dwtedx.income.discovery.TaobaoTradeCallback;
import com.dwtedx.income.entity.TaobaoItemInfo;
import com.dwtedx.income.entity.TaobaoSearchItemInfo;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.ParseJsonToObject;
import com.dwtedx.income.widget.swiperecyclerview.SwipeRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class DiscoveryRandRecyclerAdapter extends RecyclerView.Adapter<DiscoveryRandRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<TaobaoItemInfo> mTaobaoItemInfoList;

    public DiscoveryRandRecyclerAdapter(Context mContext, List<TaobaoItemInfo> customerInfoList) {
        this.mContext = mContext;
        this.mTaobaoItemInfoList = customerInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_discovery_rand_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final TaobaoItemInfo data = mTaobaoItemInfoList.get(position);

        Glide.with(mContext).load(data.getPictUrl()).placeholder(R.mipmap.imageloader_default).error(R.mipmap.imageloader_default).into(holder.mItemImageView);
        if (CommonConstants.TAOBAO_TYPE_TIANMAO == data.getUserType()) {
            Glide.with(mContext).load(R.mipmap.tianmao).placeholder(R.mipmap.tianmao).error(R.mipmap.tianmao).into(holder.mItemTypeImageView);
        } else {
            Glide.with(mContext).load(R.mipmap.taobao).placeholder(R.mipmap.taobao).error(R.mipmap.taobao).into(holder.mItemTypeImageView);
        }
        holder.mItemTitleView.setText(data.getTitle());

        //计算价格
        String price = null;
        double pricedouble = (Double.parseDouble(data.getZkFinalPrice()) - Double.parseDouble(data.getCoupon()));
        if(pricedouble > 0){
            price = CommonUtility.twoPlaces(pricedouble);
        }else{
            price = data.getZkFinalPrice();
        }
        data.setPrice(price);
        holder.mItemPriceView.setText(mContext.getString(R.string.rmb) + price);
        holder.mItemPriceDelView.setText(mContext.getString(R.string.rmb) + data.getZkFinalPrice());
        holder.mItemPriceDelView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        String tag = CommonUtility.isEmpty(data.getCoupon())?mContext.getString(R.string.discovery_tag):mContext.getString(R.string.discovery_tag_item);
        holder.mItemTagView.setText(tag);
        holder.mItemVolumeView.setText(mContext.getString(R.string.discovery_volume) + data.getVolume() + mContext.getString(R.string.discovery_volume_un));

        holder.mItemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ItemRecommendActivity.class);
                intent.putExtra("itemInfo", ParseJsonToObject.getJsonFromObj(data).toString());
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mTaobaoItemInfoList != null ? mTaobaoItemInfoList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.m_item_image_view)
        ImageView mItemImageView;
        @BindView(R.id.m_item_type_image_view)
        ImageView mItemTypeImageView;
        @BindView(R.id.m_item_title_view)
        TextView mItemTitleView;
        @BindView(R.id.m_item_price_view)
        TextView mItemPriceView;
        @BindView(R.id.m_item_volume_view)
        TextView mItemVolumeView;
        @BindView(R.id.m_item_layout_view)
        LinearLayout mItemLayoutView;
        @BindView(R.id.m_item_price_del_view)
        TextView mItemPriceDelView;
        @BindView(R.id.m_item_tag_view)
        TextView mItemTagView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
