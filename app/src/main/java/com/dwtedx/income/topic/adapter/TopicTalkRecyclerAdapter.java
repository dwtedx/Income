package com.dwtedx.income.topic.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.entity.DiTopic;
import com.dwtedx.income.entity.DiTopictalk;
import com.dwtedx.income.topic.TopicImageLoader;
import com.dwtedx.income.utility.RelativeDateFormat;
import com.dwtedx.income.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class TopicTalkRecyclerAdapter extends RecyclerView.Adapter<TopicTalkRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private DiTopic mDiTopic;

    public TopicTalkRecyclerAdapter(Context mContext, DiTopic topic) {
        this.mContext = mContext;
        this.mDiTopic = topic;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_topic_talk_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DiTopictalk data = mDiTopic.getTopictalk().get(position);
        TopicImageLoader.loadImageUser(mContext, data.getRemark(),holder.mUserImageView);
        holder.mUserNameView.setText(data.getName());
        holder.mTimeTextView.setText(RelativeDateFormat.format(data.getCreatetimestr()));
        holder.mTalkTextView.setText(data.getContent());

        holder.mItemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDiTopic.getTopictalk() != null ? mDiTopic.getTopictalk().size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.m_user_image_view)
        CircleImageView mUserImageView;
        @BindView(R.id.m_user_name_view)
        TextView mUserNameView;
        @BindView(R.id.m_time_text_view)
        TextView mTimeTextView;
        @BindView(R.id.m_talk_text_view)
        TextView mTalkTextView;
        @BindView(R.id.m_item_layout_view)
        RelativeLayout mItemLayoutView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
