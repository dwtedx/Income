package com.dwtedx.income.discovery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dwtedx.income.R;
import com.dwtedx.income.discovery.ItemRecommendActivity;
import com.dwtedx.income.entity.TaobaoItemInfo;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.ParseJsonToObject;
import com.dwtedx.income.widget.swiperecyclerview.SwipeRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class DiscoveryRecyclerAdapter extends RecyclerView.Adapter<DiscoveryRecyclerAdapter.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private View mHeaderView;

    private Context mContext;
    private List<TaobaoItemInfo> mTaobaoItemInfoList;
    private SwipeRecyclerView mRecyclerView;

    public DiscoveryRecyclerAdapter(Context mContext, List<TaobaoItemInfo> customerInfoList, SwipeRecyclerView recyclerView) {
        this.mContext = mContext;
        this.mTaobaoItemInfoList = customerInfoList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_discovery_list_item, parent, false);
        return new ViewHolder(view, TYPE_NORMAL);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;

        final int pos = getRealPosition(holder);
        final TaobaoItemInfo data = mTaobaoItemInfoList.get(pos);

        Glide.with(holder.mItemImageView.getContext()).load(data.getPictUrl()).placeholder(R.mipmap.imageloader_default).error(R.mipmap.imageloader_default).into(holder.mItemImageView);
        if (CommonConstants.TAOBAO_TYPE_TIANMAO == data.getUserType()) {
            Glide.with(holder.mItemTypeImageView.getContext()).load(R.mipmap.tianmao).placeholder(R.mipmap.tianmao).error(R.mipmap.tianmao).into(holder.mItemTypeImageView);
        } else {
            Glide.with(holder.mItemTypeImageView.getContext()).load(R.mipmap.taobao).placeholder(R.mipmap.taobao).error(R.mipmap.taobao).into(holder.mItemTypeImageView);
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

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mTaobaoItemInfoList.size() : mTaobaoItemInfoList.size() + 1;
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
