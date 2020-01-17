package com.dwtedx.income.topic.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiTopic;
import com.dwtedx.income.entity.DiTopicvote;
import com.dwtedx.income.profile.LoginV2Activity;
import com.dwtedx.income.service.TopicService;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.ProgressView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class TopicVoteRecyclerAdapter extends RecyclerView.Adapter<TopicVoteRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private DiTopic mDiTopic;

    private RecyclerView mRecyclerView;

    public TopicVoteRecyclerAdapter(Context mContext, DiTopic topic, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.mDiTopic = topic;
        this.mRecyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_topic_vote_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DiTopicvote data = mDiTopic.getTopicvote().get(position);
        holder.mItemTextView.setText(data.getName());
        holder.mItemPersionTextView.setText(data.getPersonnum() + "人");
        holder.mItemPersionTextView.setVisibility(View.GONE);
        //是否投票
        if(mDiTopic.isVoted()){
            changeVote(holder);
        }
        holder.mItemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null == ApplicationData.mDiUserInfo || ApplicationData.mDiUserInfo.getId() == 0){
                    Toast.makeText(mContext, "投票需要先登录哦", Toast.LENGTH_SHORT).show();
                    mContext.startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                //暂时不支持取消投票功能
                if(mDiTopic.isVoted()){
                    Toast.makeText(mContext, "每人只有一票哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                SaDataProccessHandler<Void, Void, List<DiTopicvote>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<DiTopicvote>>((BaseActivity) mContext) {
                            @Override
                            public void onSuccess(List<DiTopicvote> data) {
                                mDiTopic.setVoted(true);
                                mDiTopic.setTopicvote(data);
                                //changeVote(holder);
                                TopicVoteRecyclerAdapter.this.notifyDataSetChanged();
                            }
                        };
                TopicService.getInstance().seveVoteResult(mDiTopic.getId(), data.getId(), ApplicationData.mDiUserInfo.getId(), ApplicationData.mDiUserInfo.getName(), dataVerHandler);
            }
        });
    }

    private void changeVote(final ViewHolder holder) {
        holder.mItemProgressView.post(new Runnable() {
            @Override
            public void run() {
                int width = holder.mItemProgressView.getMeasuredWidth();
                for (int i = 0; i < mDiTopic.getTopicvote().size(); i++) {
                    View itemView = mRecyclerView.getChildAt(i);
                    ProgressView progressView = itemView.findViewById(R.id.m_item_progress_view);
                    TextView persionTextView = itemView.findViewById(R.id.m_item_persion_text_view);
                    persionTextView.setVisibility(View.VISIBLE);
                    //设置颜色
                    if(mDiTopic.getTopicvote().get(i).isChecked()) {
                        progressView.setColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                    }else{
                        progressView.setColor(mContext.getResources().getColor(R.color.common_division_line));
                    }
                    //设置圆角   默认无圆角
                    progressView.setRadius(CommonUtility.dip2px(mContext, 5));
                    //设置进度条长度    默认px
                    float persiont = Float.parseFloat(mDiTopic.getTopicvote().get(i).getPercent()) / 100;
                    float progress = persiont * width;
                    progressView.setProgress(progress);
                    //设置动画时间
                    progressView.setDuration(500);
                    progressView.startAnim();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDiTopic.getTopicvote() != null ? mDiTopic.getTopicvote().size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.m_item_text_view)
        TextView mItemTextView;
        @BindView(R.id.m_item_persion_text_view)
        TextView mItemPersionTextView;
        @BindView(R.id.m_item_progress_view)
        ProgressView mItemProgressView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
