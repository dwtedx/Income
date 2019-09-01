package com.dwtedx.income.topic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiTopic;
import com.dwtedx.income.profile.LoginV2Activity;
import com.dwtedx.income.service.TopicService;
import com.dwtedx.income.topic.adapter.TopicImgRecyclerAdapter;
import com.dwtedx.income.topic.adapter.TopicVoteRecyclerAdapter;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.RelativeDateFormat;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.CircleImageView;
import com.previewlibrary.GPreviewBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TopicDetailActivity extends BaseActivity {

    @BindView(R.id.m_app_title)
    AppTitleBar mAppTitle;
    @BindView(R.id.m_user_image_view)
    CircleImageView mUserImageView;
    @BindView(R.id.m_user_name_view)
    TextView mUserNameView;
    @BindView(R.id.m_time_view)
    TextView mTimeView;
    @BindView(R.id.m_item_desc_view)
    TextView mItemDescView;
    @BindView(R.id.m_image_view)
    ImageView mImageView;
    @BindView(R.id.m_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.m_topic_type_view)
    TextView mTopicTypeView;
    @BindView(R.id.m_vote_recycler_view)
    RecyclerView mVoteRecyclerView;
    @BindView(R.id.m_vote_recycler_layout_view)
    LinearLayout mVoteRecyclerLayoutView;
    @BindView(R.id.m_topic_location_view)
    TextView mTopicLocationView;
    @BindView(R.id.m_item_liked_view)
    TextView mItemLikedView;
    @BindView(R.id.m_item_liked_layout_view)
    LinearLayout mItemLikedLayoutView;
    @BindView(R.id.m_topic_location_layout_view)
    RelativeLayout mTopicLocationLayoutView;
    @BindView(R.id.m_item_weixin_share_layout_view)
    LinearLayout mItemWeixinShareLayoutView;
    @BindView(R.id.m_item_wxcircle_share_layout_view)
    LinearLayout mItemWxcircleShareLayoutView;
    @BindView(R.id.m_item_sina_share_layout_view)
    LinearLayout mItemSinaShareLayoutView;
    @BindView(R.id.m_item_qq_share_layout_view)
    LinearLayout mItemQqShareLayoutView;
    @BindView(R.id.m_item_qzone_share_layout_view)
    LinearLayout mItemQzoneShareLayoutView;
    @BindView(R.id.m_item_alipay_share_layout_view)
    LinearLayout mItemAlipayShareLayoutView;
    @BindView(R.id.m_item_talk_view)
    TextView mItemTalkView;
    @BindView(R.id.m_item_talk_layout_view)
    LinearLayout mItemTalkLayoutView;
    @BindView(R.id.m_talk_recycler_view)
    RecyclerView mTalkRecyclerView;
    @BindView(R.id.m_talk_recycler_layout_view)
    LinearLayout mTalkRecyclerLayoutView;
    @BindView(R.id.m_item_share_view)
    TextView mItemShareView;

    private int mTopicId;
    private DiTopic mDiTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        ButterKnife.bind(this);

        mTopicId = getIntent().getIntExtra("topicId", 0);
        findTopic();

    }

    private void init() {
        TopicImageLoader.loadImageUser(this, mDiTopic.getUserpath(), mUserImageView);
        mUserNameView.setText(mDiTopic.getUsername());
        mTimeView.setText(RelativeDateFormat.format(mDiTopic.getCreatetimestr()));
        mItemDescView.setText(mDiTopic.getDescription());
        //定位
        if (CommonUtility.isEmpty(mDiTopic.getLocation())) {
            mTopicLocationLayoutView.setVisibility(View.GONE);
        } else {
            mTopicLocationView.setText(mDiTopic.getLocation());
            mTopicLocationLayoutView.setVisibility(View.VISIBLE);
        }
        mItemShareView.setText(mDiTopic.getShared() + getString(R.string.topic_share_text));
        mItemTalkView.setText(mDiTopic.getTalkcount() + getString(R.string.topic_talk_text));
        mItemLikedView.setText(mDiTopic.getLiked() + getString(R.string.topic_liked_text));
        if (CommonConstants.TOPIC_TYPE_VOTE == mDiTopic.getType()) {
            mTopicTypeView.setText(R.string.topic_type_vate);
            mTopicTypeView.setVisibility(View.VISIBLE);
        } else {
            mTopicTypeView.setVisibility(View.GONE);
        }
        //图片处理
        if (null != mDiTopic.getTopicimg() && mDiTopic.getTopicimg().size() > 0) {
            //自定义分割线的样式
            switch (mDiTopic.getTopicimg().size()) {
                case 1:
                    mRecyclerView.setVisibility(View.GONE);
                    mImageView.setVisibility(View.VISIBLE);
                    //计算视图大小
                    if (mDiTopic.getTopicimg().get(0).getHeight() > 0) {
                        ViewGroup.LayoutParams para = mImageView.getLayoutParams();
                        int maxWidth = CommonUtility.dip2px(this, 218);
                        int maxHeigth = CommonUtility.dip2px(this, 230);
                        //按照最长边计算
                        if (mDiTopic.getTopicimg().get(0).getWidth() > mDiTopic.getTopicimg().get(0).getHeight()) {
                            if (mDiTopic.getTopicimg().get(0).getWidth() > maxWidth) {
                                float abc = ((float) maxWidth) / mDiTopic.getTopicimg().get(0).getWidth();
                                para.width = maxWidth;
                                para.height = (int) (mDiTopic.getTopicimg().get(0).getHeight() * abc);
                            } else {
                                para.width = mDiTopic.getTopicimg().get(0).getWidth();
                                para.height = mDiTopic.getTopicimg().get(0).getHeight();
                            }
                        } else {
                            //高比宽长
                            if (mDiTopic.getTopicimg().get(0).getHeight() > maxHeigth) {
                                float abc = ((float) maxHeigth) / mDiTopic.getTopicimg().get(0).getHeight();
                                para.width = (int) (mDiTopic.getTopicimg().get(0).getWidth() * abc);
                                para.height = maxHeigth;
                            } else {
                                para.width = mDiTopic.getTopicimg().get(0).getWidth();
                                para.height = mDiTopic.getTopicimg().get(0).getHeight();
                            }
                        }
                        mImageView.setLayoutParams(para);

                    }
                    TopicImageLoader.loadImage(this, mDiTopic.getTopicimg().get(0), mImageView);
                    break;

                case 4:
                    RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams();
                    params4.width = CommonUtility.dip2px(this, 222);
                    params4.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                    mRecyclerView.setLayoutParams(params4);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mImageView.setVisibility(View.GONE);
                    RecyclerView.LayoutManager layoutManagerHeader4 = new GridLayoutManager(this, 2) {
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    };
                    mRecyclerView.setLayoutManager(layoutManagerHeader4);

                    mRecyclerView.setAdapter(new TopicImgRecyclerAdapter(this, mDiTopic.getTopicimg(), mRecyclerView));
                    break;

                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams();
                    params.width = RecyclerView.LayoutParams.MATCH_PARENT;
                    params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                    mRecyclerView.setLayoutParams(params);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mImageView.setVisibility(View.GONE);
                    RecyclerView.LayoutManager layoutManagerHeader = new GridLayoutManager(this, 3) {
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    };
                    mRecyclerView.setLayoutManager(layoutManagerHeader);

                    mRecyclerView.setAdapter(new TopicImgRecyclerAdapter(this, mDiTopic.getTopicimg(), mRecyclerView));
                    break;
            }
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mImageView.setVisibility(View.GONE);
        }

        //投票显示
        if (null != mDiTopic.getTopicvote() && mDiTopic.getTopicvote().size() > 0) {
            mVoteRecyclerLayoutView.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager layoutManagerHeader = new LinearLayoutManager(this) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            mVoteRecyclerView.setLayoutManager(layoutManagerHeader);

            mVoteRecyclerView.setAdapter(new TopicVoteRecyclerAdapter(this, mDiTopic, mVoteRecyclerView));
        } else {
            mVoteRecyclerLayoutView.setVisibility(View.GONE);
        }

        //事件
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rect bounds = new Rect();
                mImageView.getGlobalVisibleRect(bounds);
                mDiTopic.getTopicimg().get(0).setBounds(bounds);
                GPreviewBuilder.from(TopicDetailActivity.this)
                        .setSingleData(mDiTopic.getTopicimg().get(0))
                        .setCurrentIndex(0)
                        .setSingleShowType(false)
                        .setSingleFling(true)
                        .setType(GPreviewBuilder.IndicatorType.Dot)
                        .start();
            }
        });

        //mItemLayoutView.setTag(position);
        //mItemShareLayoutView.setTag(position);
        //mItemTalkLayoutView.setTag(position);
        //mItemLikedLayoutView.setTag(position);
        //mItemLayoutView.setOnClickListener(onClickListener);
        //mItemShareLayoutView.setOnClickListener(onClickListener);
        mItemTalkLayoutView.setOnClickListener(onClickListener);
        mItemLikedLayoutView.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){

//                case R.id.m_item_layout_view:
//                case R.id.m_item_talk_layout_view:
//                    Intent intent = new Intent(mContext, TopicDetailActivity.class);
//                    intent.putExtra("topicId", topic.getId());
//                    mContext.startActivity(intent);
//                    break;

//                case R.id.m_item_share_layout_view:
//                    Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show();
//                    break;

                case R.id.m_item_liked_layout_view:
                    //点赞
                    if(null == ApplicationData.mDiUserInfo || ApplicationData.mDiUserInfo.getId() == 0){
                        Toast.makeText(TopicDetailActivity.this, "点赞需要先登录哦", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(TopicDetailActivity.this, LoginV2Activity.class));
                        return;
                    }
                    SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(TopicDetailActivity.this) {
                        @Override
                        public void onSuccess(Void dataVode) {
                            mDiTopic.setLiked(mDiTopic.getLiked() + 1);
                            ((TextView)v.findViewById(R.id.m_item_liked_view)).setText(mDiTopic.getLiked() + TopicDetailActivity.this.getString(R.string.topic_liked_text));
                            Toast.makeText(TopicDetailActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                        }
                    };
                    TopicService.getInstance().topicLicked(mDiTopic.getId(), dataVerHandler);
                    break;
            }
        }
    };


    private void findTopic() {

        SaDataProccessHandler<Void, Void, DiTopic> dataVerHandler = new SaDataProccessHandler<Void, Void, DiTopic>(this) {
            @Override
            public void onSuccess(DiTopic data) {
                mDiTopic = data;
                init();

            }

        };

        TopicService.getInstance().findTopic(mTopicId, dataVerHandler);
    }
}
