package com.dwtedx.income.discovery.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.dwtedx.income.discovery.TaobaoTradeCallback;
import com.dwtedx.income.discovery.TaobaoTradeUtility;
import com.dwtedx.income.entity.TaobaoSearchItemInfo;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.swiperecyclerview.SwipeRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class DiscoverySearchRecyclerAdapter extends RecyclerView.Adapter<DiscoverySearchRecyclerAdapter.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private View mHeaderView;

    private Context mContext;
    private List<TaobaoSearchItemInfo> mTaobaoSearchItemInfoList;
    private SwipeRecyclerView mRecyclerView;

    public DiscoverySearchRecyclerAdapter(Context mContext, List<TaobaoSearchItemInfo> customerInfoList, SwipeRecyclerView recyclerView) {
        this.mContext = mContext;
        this.mTaobaoSearchItemInfoList = customerInfoList;
        this.mRecyclerView = recyclerView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {   // 布局是GridLayoutManager所管理
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果是Header、Footer的对象则占据spanCount的位置，否则就只占用1个位置
                    return (TYPE_HEADER == getItemViewType(position)) || isLoadMoreItem(position) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    public boolean isLoadMoreItem(int position) {
        return position == getItemCount();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new ViewHolder(mHeaderView, TYPE_HEADER);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_discovery_search_list_item, parent, false);
        return new ViewHolder(view, TYPE_NORMAL);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;

        final int pos = getRealPosition(holder);
        final TaobaoSearchItemInfo data = mTaobaoSearchItemInfoList.get(pos);

        Glide.with(mContext).load(data.getPict_url()).placeholder(R.mipmap.imageloader_default).error(R.mipmap.imageloader_default).into(holder.mItemImageView);
        if (CommonConstants.TAOBAO_TYPE_TIANMAO == data.getUser_type()) {
            Glide.with(mContext).load(R.mipmap.tianmao).placeholder(R.mipmap.tianmao).error(R.mipmap.tianmao).into(holder.mItemTypeImageView);
        } else {
            Glide.with(mContext).load(R.mipmap.taobao).placeholder(R.mipmap.taobao).error(R.mipmap.taobao).into(holder.mItemTypeImageView);
        }
        holder.mItemTitleView.setText(data.getTitle());
        holder.mItemPriceView.setText(mContext.getString(R.string.rmb) + data.getZk_final_price());
        holder.mItemPriceDelView.setText(mContext.getString(R.string.rmb) + data.getReserve_price());
        holder.mItemPriceDelView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        String tag = CommonUtility.isEmpty(data.getTag_content())?mContext.getString(R.string.discovery_tag):data.getTag_content();
        holder.mItemTagView.setText(tag);
        holder.mItemVolumeView.setText(mContext.getString(R.string.discovery_volume) + data.getVolume() + mContext.getString(R.string.discovery_volume_un));

        holder.mItemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaobaoTradeUtility.showTaobaonNumId((Activity) mContext, data.getNum_iid());
            }
        });

    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mTaobaoSearchItemInfoList.size() : mTaobaoSearchItemInfoList.size() + 1;
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

        public ViewHolder(View itemView, int type) {
            super(itemView);
            if (type != TYPE_HEADER) ButterKnife.bind(this, itemView);
        }
    }
}
