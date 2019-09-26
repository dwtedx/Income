package com.dwtedx.income.topic;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiTopic;
import com.dwtedx.income.entity.DiTopictalk;
import com.dwtedx.income.profile.LoginV2Activity;
import com.dwtedx.income.service.TopicService;
import com.dwtedx.income.topic.adapter.TopicImgRecyclerAdapter;
import com.dwtedx.income.topic.adapter.TopicTalkRecyclerAdapter;
import com.dwtedx.income.topic.adapter.TopicVoteRecyclerAdapter;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.RelativeDateFormat;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.CircleImageView;
import com.dwtedx.income.widget.RecycleViewDivider;
import com.previewlibrary.GPreviewBuilder;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TopicDetailActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener{

    @BindView(R.id.m_app_title)
    AppTitleBar mAppTitle;
    @BindView(R.id.m_all_layout_view)
    LinearLayout mAllLayoutView;
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
    @BindView(R.id.m_talk_edit_view)
    EditText mTalkEditView;
    @BindView(R.id.m_talk_button_view)
    TextView mTalkButtonView;
    @BindView(R.id.m_talk_layout_view)
    RelativeLayout mTalkLayoutView;
    @BindView(R.id.m_item_wxfavorite_share_layout_view)
    LinearLayout mItemWxfavoriteShareLayoutView;
    @BindView(R.id.m_item_sms_share_layout_view)
    LinearLayout mItemSmsShareLayoutView;

    private int mTopicId;
    private DiTopic mDiTopic;

    //回复
    private TopicTalkRecyclerAdapter mTopicTalkRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        ButterKnife.bind(this);

        mAppTitle.setOnTitleClickListener(this);
        mAppTitle.setRightVisibility(View.GONE);

        mTopicId = getIntent().getIntExtra("topicId", 0);
        findTopic();
    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                //提示
                new MaterialDialog.Builder(this)
                        .title(R.string.tip)
                        .content(R.string.topic_detail_delete_confirm_tip)
                        .positiveText(R.string.ok)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                deleteTopic();
                            }
                        })
                        .show();
                break;
        }
    }

    private void deleteTopic() {
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(TopicDetailActivity.this) {
            @Override
            public void onSuccess(Void data) {
                MyTopicActivity.isRefresh = true;
                TopicFragment.isRefresh = true;
                Toast.makeText(TopicDetailActivity.this, R.string.topic_detail_delete_tip, Toast.LENGTH_SHORT).show();
                TopicDetailActivity.this.finish();
            }

        };
        TopicService.getInstance().deleteTopic(mTopicId, dataVerHandler);
    }

    private void saveTalk() {
        if (null == ApplicationData.mDiUserInfo || ApplicationData.mDiUserInfo.getId() == 0) {
            Toast.makeText(TopicDetailActivity.this, "回复需要先登录哦", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(TopicDetailActivity.this, LoginV2Activity.class));
            return;
        }
        String context = mTalkEditView.getText().toString();
        if (CommonUtility.isEmpty(context)) {
            Toast.makeText(TopicDetailActivity.this, R.string.topic_add_send_tip, Toast.LENGTH_SHORT).show();
            return;
        }
        //保存
        DiTopictalk topictalk = new DiTopictalk();
        topictalk.setTopicid(mTopicId);
        topictalk.setUserid(ApplicationData.mDiUserInfo.getId());
        topictalk.setName(ApplicationData.mDiUserInfo.getName());
        topictalk.setRemark(ApplicationData.mDiUserInfo.getHead());
        topictalk.setCreatetimestr(CommonUtility.getCurrentTime());
        topictalk.setContent(context);
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(TopicDetailActivity.this) {
            @Override
            public void onSuccess(Void data) {
                mDiTopic.getTopictalk().add(0, topictalk);
                mTopicTalkRecyclerAdapter.notifyDataSetChanged();
                mTalkEditView.setText("");
                Toast.makeText(TopicDetailActivity.this, R.string.topic_detail_talk_tip, Toast.LENGTH_SHORT).show();
            }

        };
        TopicService.getInstance().seveTopicTalk(topictalk, dataVerHandler);
    }

    private void init() {
        //删除按钮
        if(isLogin() && ApplicationData.mDiUserInfo.getId() == mDiTopic.getUserid()){
            mAppTitle.setRightVisibility(View.VISIBLE);
        }
        mAllLayoutView.setVisibility(View.VISIBLE);
        TopicImageLoader.loadImageUser(mImageView.getContext(), mDiTopic.getUserpath(), mUserImageView);
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
                    TopicImageLoader.loadImage(mImageView.getContext(), mDiTopic.getTopicimg().get(0), mImageView);
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

        //回复
        RecyclerView.LayoutManager layoutManagerHeader = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mTalkRecyclerView.setLayoutManager(layoutManagerHeader);
        //自定义分割线的样式
        mTalkRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(this, R.color.common_division_line)));
        mTopicTalkRecyclerAdapter = new TopicTalkRecyclerAdapter(this, mDiTopic);
        mTalkRecyclerView.setAdapter(mTopicTalkRecyclerAdapter);

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
        mTalkButtonView.setOnClickListener(onClickListener);
        //分享按钮
        mItemWeixinShareLayoutView.setOnClickListener(onClickListener);
        mItemWxcircleShareLayoutView.setOnClickListener(onClickListener);
        mItemWxfavoriteShareLayoutView.setOnClickListener(onClickListener);
        mItemSinaShareLayoutView.setOnClickListener(onClickListener);
        mItemQzoneShareLayoutView.setOnClickListener(onClickListener);
        mItemAlipayShareLayoutView.setOnClickListener(onClickListener);
        mItemSmsShareLayoutView.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.m_item_liked_layout_view:
                    //点赞
                    topicLiked(v);
                    break;

                case R.id.m_talk_button_view:
                    //回复
                    saveTalk();
                    break;

                case R.id.m_item_weixin_share_layout_view:
                    openShare(SHARE_MEDIA.WEIXIN);
                    break;

                case R.id.m_item_wxcircle_share_layout_view:
                    openShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                    break;

                case R.id.m_item_wxfavorite_share_layout_view:
                    openShare(SHARE_MEDIA.WEIXIN_FAVORITE);
                    break;

                case R.id.m_item_sina_share_layout_view:
                    openShare(SHARE_MEDIA.SINA);
                    break;

                case R.id.m_item_qzone_share_layout_view:
                    openShare(SHARE_MEDIA.QZONE);
                    break;

                case R.id.m_item_alipay_share_layout_view:
                    openShare(SHARE_MEDIA.ALIPAY);
                    break;

                case R.id.m_item_sms_share_layout_view:
                    openShare(SHARE_MEDIA.SMS);
                    break;
            }
        }
    };

    private void topicLiked(View v) {
        if (null == ApplicationData.mDiUserInfo || ApplicationData.mDiUserInfo.getId() == 0) {
            Toast.makeText(TopicDetailActivity.this, "点赞需要先登录哦", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(TopicDetailActivity.this, LoginV2Activity.class));
            return;
        }
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(TopicDetailActivity.this) {
            @Override
            public void onSuccess(Void dataVode) {
                mDiTopic.setLiked(mDiTopic.getLiked() + 1);
                ((TextView) v.findViewById(R.id.m_item_liked_view)).setText(mDiTopic.getLiked() + TopicDetailActivity.this.getString(R.string.topic_liked_text));
                Toast.makeText(TopicDetailActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handlerError(SaException e) {
                //super.handlerError(e);
                release();
                Toast.makeText(TopicDetailActivity.this, e.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        TopicService.getInstance().topicLicked(mDiTopic.getId(), dataVerHandler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

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

    // 用来配置各个平台的SDKF
    private void openShare(SHARE_MEDIA share_media) {

        System.out.println("话题id=========================" + mDiTopic.getId());
        String descId = Base64.encodeToString(String.valueOf(mDiTopic.getId()).getBytes(), Base64.DEFAULT);
        System.out.println("加密话题id=========================" + descId);

        // 解码
        //byte[] decodeByte = Base64.decode(descId .getBytes(), Base64.DEFAULT);
        //String decode = new String(decodeByte);
        //System.out.println("解密" + decode);

        String desc = mDiTopic.getDescription();
        if(mDiTopic.getDescription().length() > 17){
            desc = mDiTopic.getDescription().substring(0, 17) + "...";
        }

        String shareContent = "我在使用DD记账，记录生活中的每一笔开支，还有有趣的记账圈【" + desc + "】，特此推荐给您 http://income.dwtedx.com ，复制这段描述$" + descId + "$→打开DD记账→查看详情";
        new ShareAction(this)
                .setPlatform(share_media)//传入平台
                .withText(shareContent)
                .setCallback(umShareListener)
                .share();

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Toast.makeText(TopicDetailActivity.this, share_media.toString() + "正在分享...", Toast.LENGTH_SHORT).show();
            saveShare(share_media);
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(TopicDetailActivity.this, share_media.toString() + "分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(TopicDetailActivity.this, share_media.toString() + "分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(TopicDetailActivity.this, share_media.toString() + "分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private void saveShare(SHARE_MEDIA share_media){
        int userid = 0;
        if(null != ApplicationData.mDiUserInfo && ApplicationData.mDiUserInfo.getId() > 0){
            userid = ApplicationData.mDiUserInfo.getId();
        }
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(TopicDetailActivity.this) {
            @Override
            public void onSuccess(Void dataVode) { }
        };
        TopicService.getInstance().topicShare(mTopicId, userid, share_media.getName(), dataVerHandler);
    }
}
