package com.dwtedx.income.topic.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.entity.DiTopic;
import com.dwtedx.income.topic.TopicImageLoader;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.RelativeDateFormat;
import com.dwtedx.income.widget.CircleImageView;
import com.dwtedx.income.widget.swiperecyclerview.SwipeRecyclerView;
import com.previewlibrary.GPreviewBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class TopicRecyclerAdapter extends RecyclerView.Adapter<TopicRecyclerAdapter.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private View mHeaderView;

    private Context mContext;
    private List<DiTopic> mList;
    private SwipeRecyclerView mRecyclerView;

    public TopicRecyclerAdapter(Context mContext, List<DiTopic> customerInfoList, SwipeRecyclerView recyclerView) {
        this.mContext = mContext;
        this.mList = customerInfoList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_topic_list_item, parent, false);
        return new ViewHolder(view, TYPE_NORMAL);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;

        final int pos = getRealPosition(holder);
        final DiTopic data = mList.get(pos);

        TopicImageLoader.loadImageUser(mContext, data.getUserpath(), holder.mUserImageView);
        holder.mUserNameView.setText(data.getUsername());
        holder.mTimeView.setText(RelativeDateFormat.format(data.getCreatetimestr()));
        holder.mItemDescView.setText(data.getDescription());
        //定位
        if (CommonUtility.isEmpty(data.getLocation())) {
            holder.mTopicLocationLayoutView.setVisibility(View.GONE);
        } else {
            holder.mTopicLocationView.setText(data.getLocation());
            holder.mTopicLocationLayoutView.setVisibility(View.VISIBLE);
        }
        holder.mItemShareView.setText(data.getShared() + mContext.getString(R.string.topic_share_text));
        holder.mItemTalkView.setText(data.getTalkcount() + mContext.getString(R.string.topic_talk_text));
        holder.mItemLikedView.setText(data.getLiked() + mContext.getString(R.string.topic_liked_text));
        if (CommonConstants.TOPIC_TYPE_VOTE == data.getType()) {
            holder.mTopicTypeView.setText(R.string.topic_type_vate);
            holder.mTopicTypeView.setVisibility(View.VISIBLE);
        } else {
            holder.mTopicTypeView.setVisibility(View.GONE);
        }
        //图片处理
        if (null != data.getTopicimg() && data.getTopicimg().size() > 0) {
            //自定义分割线的样式
            switch (data.getTopicimg().size()) {
                case 1:
                    holder.mRecyclerView.setVisibility(View.GONE);
                    holder.mImageView.setVisibility(View.VISIBLE);
                    TopicImageLoader.loadImage(mContext, data.getTopicimg().get(0), holder.mImageView);
                    break;

                case 4:
                    RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) holder.mRecyclerView.getLayoutParams();
                    params4.width = CommonUtility.dip2px(mContext, 222);
                    params4.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                    holder.mRecyclerView.setLayoutParams(params4);
                    holder.mRecyclerView.setVisibility(View.VISIBLE);
                    holder.mImageView.setVisibility(View.GONE);
                    RecyclerView.LayoutManager layoutManagerHeader4 = new GridLayoutManager(mContext, 2) {
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    };
                    holder.mRecyclerView.setLayoutManager(layoutManagerHeader4);

                    holder.mRecyclerView.setAdapter(new TopicImgRecyclerAdapter(mContext, data.getTopicimg(), holder.mRecyclerView));
                    break;

                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mRecyclerView.getLayoutParams();
                    params.width = RecyclerView.LayoutParams.MATCH_PARENT;
                    params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                    holder.mRecyclerView.setLayoutParams(params);
                    holder.mRecyclerView.setVisibility(View.VISIBLE);
                    holder.mImageView.setVisibility(View.GONE);
                    RecyclerView.LayoutManager layoutManagerHeader = new GridLayoutManager(mContext, 3) {
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    };
                    holder.mRecyclerView.setLayoutManager(layoutManagerHeader);

                    holder.mRecyclerView.setAdapter(new TopicImgRecyclerAdapter(mContext, data.getTopicimg(), holder.mRecyclerView));
                    break;
            }
        } else {
            holder.mRecyclerView.setVisibility(View.GONE);
            holder.mImageView.setVisibility(View.GONE);
        }

        //投票显示
        if(null != data.getTopicvote() && data.getTopicvote().size() > 0){
            holder.mVoteRecyclerLayoutView.setVisibility(View.VISIBLE);

        }else{
            holder.mVoteRecyclerLayoutView.setVisibility(View.GONE);
        }

        //事件
        holder.mItemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rect bounds = new Rect();
                holder.mImageView.getGlobalVisibleRect(bounds);
                data.getTopicimg().get(0).setBounds(bounds);
                GPreviewBuilder.from((Activity) mContext)
                        .setSingleData(data.getTopicimg().get(0))
                        .setCurrentIndex(0)
                        .setSingleShowType(false)
                        .setSingleFling(true)
                        .setType(GPreviewBuilder.IndicatorType.Dot)
                        .start();
            }
        });

    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mList.size() : mList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.m_user_image_view)
        CircleImageView mUserImageView;
        @BindView(R.id.m_user_name_view)
        TextView mUserNameView;
        @BindView(R.id.m_time_view)
        TextView mTimeView;
        @BindView(R.id.m_item_desc_view)
        TextView mItemDescView;
        @BindView(R.id.m_recycler_view)
        RecyclerView mRecyclerView;
        @BindView(R.id.m_image_view)
        ImageView mImageView;
        @BindView(R.id.m_topic_location_layout_view)
        LinearLayout mTopicLocationLayoutView;
        @BindView(R.id.m_topic_location_view)
        TextView mTopicLocationView;
        @BindView(R.id.m_item_share_view)
        TextView mItemShareView;
        @BindView(R.id.m_item_talk_view)
        TextView mItemTalkView;
        @BindView(R.id.m_item_liked_view)
        TextView mItemLikedView;
        @BindView(R.id.m_item_layout_view)
        LinearLayout mItemLayoutView;
        @BindView(R.id.m_topic_type_view)
        TextView mTopicTypeView;
        @BindView(R.id.m_vote_recycler_view)
        RecyclerView mVoteRecyclerView;
        @BindView(R.id.m_vote_recycler_layout_view)
        LinearLayout mVoteRecyclerLayoutView;

        public ViewHolder(View itemView, int type) {
            super(itemView);
            if (type != TYPE_HEADER) ButterKnife.bind(this, itemView);
        }
    }
}
