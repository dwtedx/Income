package com.dwtedx.income.topic.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiTopic;
import com.dwtedx.income.profile.LoginV2Activity;
import com.dwtedx.income.profile.SetupActivity;
import com.dwtedx.income.service.TopicService;
import com.dwtedx.income.topic.TopicDetailActivity;
import com.dwtedx.income.topic.TopicImageLoader;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.RelativeDateFormat;
import com.dwtedx.income.widget.CircleImageView;
import com.previewlibrary.GPreviewBuilder;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

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

    private int mTopicShareId;

    public TopicRecyclerAdapter(Context mContext, List<DiTopic> customerInfoList) {
        this.mContext = mContext;
        this.mList = customerInfoList;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;

        final int pos = getRealPosition(holder);
        final DiTopic data = mList.get(pos);

        TopicImageLoader.loadImageUser(holder.mUserImageView.getContext(), data.getUserpath(), holder.mUserImageView);
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
                    //计算视图大小
                    if(data.getTopicimg().get(0).getHeight() > 0) {
                        ViewGroup.LayoutParams para = holder.mImageView.getLayoutParams();
                        int maxWidth = CommonUtility.dip2px(mContext, 218);
                        int maxHeigth = CommonUtility.dip2px(mContext, 230);
                        //按照最长边计算
                        if (data.getTopicimg().get(0).getWidth() > data.getTopicimg().get(0).getHeight()){
                            if (data.getTopicimg().get(0).getWidth() > maxWidth) {
                                float abc = ((float) maxWidth) / data.getTopicimg().get(0).getWidth();
                                para.width = maxWidth;
                                para.height = (int) (data.getTopicimg().get(0).getHeight() * abc);
                            } else {
                                para.width = data.getTopicimg().get(0).getWidth();
                                para.height = data.getTopicimg().get(0).getHeight();
                            }
                        } else {
                            //高比宽长
                            if (data.getTopicimg().get(0).getHeight() > maxHeigth) {
                                float abc = ((float) maxHeigth) / data.getTopicimg().get(0).getHeight();
                                para.width = (int) (data.getTopicimg().get(0).getWidth() * abc);
                                para.height = maxHeigth;
                            } else {
                                para.width = data.getTopicimg().get(0).getWidth();
                                para.height = data.getTopicimg().get(0).getHeight();
                            }
                        }
                        holder.mImageView.setLayoutParams(para);

                    }
                    TopicImageLoader.loadImage(holder.mImageView.getContext(), data.getTopicimg().get(0), holder.mImageView);
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
        if (null != data.getTopicvote() && data.getTopicvote().size() > 0) {
            holder.mVoteRecyclerLayoutView.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager layoutManagerHeader = new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            holder.mVoteRecyclerView.setLayoutManager(layoutManagerHeader);

            holder.mVoteRecyclerView.setAdapter(new TopicVoteRecyclerAdapter(mContext, data, holder.mVoteRecyclerView));
        } else {
            holder.mVoteRecyclerLayoutView.setVisibility(View.GONE);
        }

        //事件
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

        holder.mItemLayoutView.setTag(position);
        holder.mItemShareLayoutView.setTag(position);
        holder.mItemTalkLayoutView.setTag(position);
        holder.mItemLikedLayoutView.setTag(position);
        holder.mItemLayoutView.setOnClickListener(onClickListener);
        holder.mItemShareLayoutView.setOnClickListener(onClickListener);
        holder.mItemTalkLayoutView.setOnClickListener(onClickListener);
        holder.mItemLikedLayoutView.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            DiTopic topic = mList.get((int)v.getTag());
            switch (v.getId()){

                case R.id.m_item_layout_view:
                case R.id.m_item_talk_layout_view:
                    Intent intent = new Intent(mContext, TopicDetailActivity.class);
                    intent.putExtra("topicId", topic.getId());
                    mContext.startActivity(intent);
                    break;

                case R.id.m_item_share_layout_view:
                    openShare(topic);
                    break;

                case R.id.m_item_liked_layout_view:
                    //点赞
                    topicLiked(v, topic);
                    break;
            }
        }
    };

    private void topicLiked(View v, DiTopic topic) {
        if(null == ApplicationData.mDiUserInfo || ApplicationData.mDiUserInfo.getId() == 0){
            Toast.makeText(mContext, "点赞需要先登录哦", Toast.LENGTH_SHORT).show();
            mContext.startActivity(new Intent(mContext, LoginV2Activity.class));
            return;
        }
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>((BaseActivity) mContext) {
            @Override
            public void onSuccess(Void dataVode) {
                topic.setLiked(topic.getLiked() + 1);
                ((TextView)v.findViewById(R.id.m_item_liked_view)).setText(topic.getLiked() + mContext.getString(R.string.topic_liked_text));
                Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handlerError(SaException e) {
                //super.handlerError(e);
                release();
                Toast.makeText(mContext, e.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        TopicService.getInstance().topicLicked(topic.getId(), dataVerHandler);
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mList.size() : mList.size() + 1;
    }

    // 用来配置各个平台的SDKF
    private void openShare(DiTopic topic) {
        mTopicShareId = topic.getId();

        System.out.println("话题id=========================" + topic.getId());
        String descId = Base64.encodeToString(String.valueOf(topic.getId()).getBytes(), Base64.DEFAULT);
        System.out.println("加密话题id=========================" + descId);

        // 解码
        //byte[] decodeByte = Base64.decode(descId .getBytes(), Base64.DEFAULT);
        //String decode = new String(decodeByte);
        //System.out.println("解密" + decode);

        String desc = topic.getDescription();
        if(topic.getDescription().length() > 17){
            desc = topic.getDescription().substring(0, 17) + "...";
        }

        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.SINA, SHARE_MEDIA.QZONE, SHARE_MEDIA.ALIPAY, SHARE_MEDIA.SMS};

        String shareContent = "我在使用DD记账，记录生活中的每一笔开支，还有有趣的记账圈【" + desc + "】，特此推荐给您 http://income.dwtedx.com ，复制这段描述$" + descId + "$→打开DD记账→查看详情";
        new ShareAction((Activity) mContext)
                .setDisplayList(displaylist)
                .withText(shareContent)
                .setCallback(umShareListener)
                .open();

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Toast.makeText(mContext, share_media.toString() + "正在分享...", Toast.LENGTH_SHORT).show();
            saveShare(share_media);
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(mContext, share_media.toString() + "分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(mContext, share_media.toString() + "分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(mContext, share_media.toString() + "分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private void saveShare(SHARE_MEDIA share_media){
        int userid = 0;
        if(null != ApplicationData.mDiUserInfo && ApplicationData.mDiUserInfo.getId() > 0){
            userid = ApplicationData.mDiUserInfo.getId();
        }
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>((BaseActivity) mContext) {
            @Override
            public void onSuccess(Void dataVode) { }
        };
        TopicService.getInstance().topicShare(mTopicShareId, userid, share_media.getName(), dataVerHandler);
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
        @BindView(R.id.m_item_share_layout_view)
        LinearLayout mItemShareLayoutView;
        @BindView(R.id.m_item_talk_layout_view)
        LinearLayout mItemTalkLayoutView;
        @BindView(R.id.m_item_liked_layout_view)
        LinearLayout mItemLikedLayoutView;

        public ViewHolder(View itemView, int type) {
            super(itemView);
            if (type != TYPE_HEADER) ButterKnife.bind(this, itemView);
        }
    }
}
