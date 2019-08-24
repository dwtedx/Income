package com.dwtedx.income.topic.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.entity.DiTopicimg;
import com.dwtedx.income.entity.DiTopicvote;
import com.dwtedx.income.topic.TopicImageLoader;
import com.previewlibrary.GPreviewBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class TopicVoteRecyclerAdapter extends RecyclerView.Adapter<TopicVoteRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<DiTopicvote> mList;

    private RecyclerView mRecyclerView;

    public TopicVoteRecyclerAdapter(Context mContext, List<DiTopicvote> topicimgs, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.mList = topicimgs;
        this.mRecyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_topic_vote_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DiTopicvote data = mList.get(position);
        holder.mItemTextView.setText(data.getName());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.m_item_text_view)
        TextView mItemTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
