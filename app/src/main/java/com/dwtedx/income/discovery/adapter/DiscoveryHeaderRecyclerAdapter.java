package com.dwtedx.income.discovery.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dwtedx.income.R;
import com.dwtedx.income.discovery.ItemCategoryTopActivity;
import com.dwtedx.income.discovery.ItemCategoryActivity;
import com.dwtedx.income.discovery.ItemNineNineActivity;
import com.dwtedx.income.entity.DiscoveryHeaderInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class DiscoveryHeaderRecyclerAdapter extends RecyclerView.Adapter<DiscoveryHeaderRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<DiscoveryHeaderInfo> mDiscoveryHeaderInfoList;

    public DiscoveryHeaderRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        this.mDiscoveryHeaderInfoList = new ArrayList<DiscoveryHeaderInfo>();
        this.mDiscoveryHeaderInfoList.add(new DiscoveryHeaderInfo(R.mipmap.womens_clothing, R.string.discovery_womens_clothing, R.string.discovery_womens_clothing_tip, 1, ItemCategoryActivity.class));
        this.mDiscoveryHeaderInfoList.add(new DiscoveryHeaderInfo(R.mipmap.special_offer, R.string.discovery_special_offer, R.string.discovery_special_offer_tip, 0, ItemCategoryActivity.class));
        this.mDiscoveryHeaderInfoList.add(new DiscoveryHeaderInfo(R.mipmap.ninenine, R.string.discovery_ninenine, R.string.discovery_ninenine_tip, -1, ItemCategoryActivity.class));
        this.mDiscoveryHeaderInfoList.add(new DiscoveryHeaderInfo(R.mipmap.man_clothing, R.string.discovery_man_clothing, R.string.discovery_man_clothing_tip, 4, ItemCategoryActivity.class));
        this.mDiscoveryHeaderInfoList.add(new DiscoveryHeaderInfo(R.mipmap.snacks, R.string.discovery_snacks, R.string.discovery_snacks_tip, 2, ItemCategoryActivity.class));
        this.mDiscoveryHeaderInfoList.add(new DiscoveryHeaderInfo(R.mipmap.makeups, R.string.discovery_makeups, R.string.discovery_makeups_tip, 5, ItemCategoryActivity.class));
        this.mDiscoveryHeaderInfoList.add(new DiscoveryHeaderInfo(R.mipmap.item_more, R.string.discovery_item_more, R.string.discovery_item_more_tip, -2, ItemCategoryActivity.class));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_header_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DiscoveryHeaderInfo data = mDiscoveryHeaderInfoList.get(position);

        holder.mItemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //-1是9.9包邮 -2是分类
                if(data.getChose() == -1) {
                    Intent intent = new Intent(mContext, ItemNineNineActivity.class);
                    mContext.startActivity(intent);
                }else if(data.getChose() == -2){
                    Intent intent = new Intent(mContext, ItemCategoryTopActivity.class);
                    mContext.startActivity(intent);
                }else{
                    Intent intent = new Intent(mContext, data.getClassz());
                    intent.putExtra("cheose", data.getChose());
                    mContext.startActivity(intent);
                }
            }
        });

        Glide.with(holder.mItemIconView.getContext()).load(data.getIcon()).into(holder.mItemIconView);
        holder.mItemTitleView.setText(data.getTitle());
        holder.mItemDescView.setText(data.getDesc());

    }

    @Override
    public int getItemCount() {
        return mDiscoveryHeaderInfoList != null ? mDiscoveryHeaderInfoList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.m_item_icon_view)
        ImageView mItemIconView;
        @BindView(R.id.m_item_title_view)
        TextView mItemTitleView;
        @BindView(R.id.m_item_desc_view)
        TextView mItemDescView;
        @BindView(R.id.m_item_layout_view)
        LinearLayout mItemLayoutView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
