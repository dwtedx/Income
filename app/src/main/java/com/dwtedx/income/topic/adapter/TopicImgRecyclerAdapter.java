package com.dwtedx.income.topic.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dwtedx.income.R;
import com.dwtedx.income.discovery.ItemCategoryActivity;
import com.dwtedx.income.discovery.ItemCategoryTopActivity;
import com.dwtedx.income.discovery.ItemNineNineActivity;
import com.dwtedx.income.entity.DiTopicimg;
import com.dwtedx.income.entity.DiscoveryHeaderInfo;
import com.dwtedx.income.topic.TopicImageLoader;
import com.previewlibrary.GPreviewBuilder;

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

    private RecyclerView mRecyclerView;

    public TopicImgRecyclerAdapter(Context mContext, List<DiTopicimg> topicimgs, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.mList = topicimgs;
        this.mRecyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_topic_img_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DiTopicimg data = mList.get(position);
        TopicImageLoader.loadImage(holder.mItemIconView.getContext(), data, holder.mItemIconView);
        holder.mItemIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                computeBoundsBackward(position);
                GPreviewBuilder.from((Activity) mContext)
                        .setData(mList)
                        .setCurrentIndex(position)
                        .setSingleFling(true)
                        .setType(GPreviewBuilder.IndicatorType.Dot)
                        .start();//启动
            }
        });
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

    /**
     * 查找信息
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
        for (int i = 0; i < mList.size(); i++) {
            View itemView = mRecyclerView.getChildAt(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                //需要显示过度控件
                ImageView thumbView = (ImageView) itemView.findViewById(R.id.m_item_icon_view);
                //拿到在控件屏幕可见中控件显示为矩形Rect信息
                thumbView.getGlobalVisibleRect(bounds);
            }
            mList.get(i).setBounds(bounds);
        }
    }
}
