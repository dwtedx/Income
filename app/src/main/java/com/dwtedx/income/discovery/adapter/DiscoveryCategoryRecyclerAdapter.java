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
import com.dwtedx.income.discovery.ItemByCategoryActivity;
import com.dwtedx.income.entity.TbCategoryInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class DiscoveryCategoryRecyclerAdapter extends RecyclerView.Adapter<DiscoveryCategoryRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<TbCategoryInfo> mTbCategoryInfoList;

    public DiscoveryCategoryRecyclerAdapter(Context mContext, List<TbCategoryInfo> customerInfoList) {
        this.mContext = mContext;
        this.mTbCategoryInfoList = customerInfoList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_discovery_category_top_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final TbCategoryInfo data = mTbCategoryInfoList.get(position);

        Glide.with(mContext).load(data.getCategoryIcon()).fitCenter().placeholder(R.mipmap.loading_discovery_catergoy).error(R.mipmap.loading_discovery_catergoy).into(holder.mItemIconView);
        String title = null;
        try {
            title = data.getCategoryName().substring(0, data.getCategoryName().indexOf('/'));
            if(title.equals("尿片")){
                title = mContext.getString(R.string.discovery_category_motherbaby);
            }
        }catch (Exception e){
            e.printStackTrace();
            title = data.getCategoryName();
        }
        holder.mItemTitleView.setText(title);
        holder.mItemDescView.setText(data.getDescription());
        final String finalTitle = title;
        holder.mItemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ItemByCategoryActivity.class);
                intent.putExtra("cateId", data.getId());
                intent.putExtra("title", finalTitle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTbCategoryInfoList != null ? mTbCategoryInfoList.size() : 0;
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
