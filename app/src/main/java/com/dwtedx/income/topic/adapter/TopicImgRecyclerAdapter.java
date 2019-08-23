package com.dwtedx.income.topic.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dwtedx.income.R;
import com.dwtedx.income.discovery.ItemCategoryActivity;
import com.dwtedx.income.discovery.ItemCategoryTopActivity;
import com.dwtedx.income.discovery.ItemNineNineActivity;
import com.dwtedx.income.entity.DiTopicimg;
import com.dwtedx.income.entity.DiscoveryHeaderInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class TopicImgRecyclerAdapter extends RecyclerView.Adapter<TopicImgRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<DiTopicimg> mList;

    public TopicImgRecyclerAdapter(Context mContext, List<DiTopicimg> topicimgs) {
        this.mContext = mContext;
        this.mList = topicimgs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_topic_img_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DiTopicimg data = mList.get(position);
        Glide.with(mContext).load(data.getFullpath()).into(holder.mItemIconView);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.m_item_icon_view)
        ImageView mItemIconView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
