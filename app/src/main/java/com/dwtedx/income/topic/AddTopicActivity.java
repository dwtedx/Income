package com.dwtedx.income.topic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.ProgressDialog;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiTopic;
import com.dwtedx.income.entity.DiTopicimg;
import com.dwtedx.income.entity.DiTopicvote;
import com.dwtedx.income.scan.ChooseLocationActivity;
import com.dwtedx.income.service.TopicService;
import com.dwtedx.income.topic.adapter.AddTopicImgRecyclerAdapter;
import com.dwtedx.income.topic.adapter.AddTopicVoteRecyclerAdapter;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.ParseJsonToObject;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.RecycleViewDivider;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTopicActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, AddTopicImgRecyclerAdapter.onAddPicClickListener, AddTopicImgRecyclerAdapter.OnItemClickListener, AddTopicImgRecyclerAdapter.OnItemLongClickListener, View.OnClickListener {
    private static final int REQUEST_CODE_CHOOSE_LOCAL = 71;
    private static final int ACCESS_COARSE_LOCALHOST_REQUEST_CODE = 72;

    @BindView(R.id.m_app_title)
    AppTitleBar mAppTitle;
    @BindView(R.id.m_topic_content)
    EditText mTopicContent;
    @BindView(R.id.m_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.m_location_button)
    Button mLocationButton;
    @BindView(R.id.m_vote_button)
    Button mVoteButton;
    @BindView(R.id.m_vote_switch)
    Switch mVoteSwitch;
    @BindView(R.id.m_vote_recycler_view)
    RecyclerView mVoteRecyclerView;

    private ProgressDialog mProgressDialog;
    private DiTopic mDiTopic;

    //百度定位7.6相关
    public LocationClient mLocationClient;
    private MyLocationListener myListener;
    private List<String> mPoiList;

    //图片选择器
    private PictureParameterStyle mPictureParameterStyle;//图片选择器主题
    private PictureCropParameterStyle mCropParameterStyle;
    private List<LocalMedia> mLocalMediaList;
    //List<DiTopicimg> mDiTopicimgList;
    private AddTopicImgRecyclerAdapter mAdapter;
    private boolean needScaleBig = true;
    private boolean needScaleSmall = true;
    private ItemTouchHelper mItemTouchHelper;

    //投票选项
    private List<DiTopicvote> mDiTopicvoteList;
    private AddTopicVoteRecyclerAdapter mAdapterVote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        ButterKnife.bind(this);

        mAppTitle.setOnTitleClickListener(this);

        mProgressDialog = getProgressDialog();
        mProgressDialog.setCancelable(false);
        mDiTopic = new DiTopic();

        //定位
        mLocationButton.setOnClickListener(this);
        initLocation();

        mLocalMediaList = new ArrayList<LocalMedia>();
        mRecyclerView.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManagerHeader = new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(layoutManagerHeader);
        mAdapter = new AddTopicImgRecyclerAdapter(this, this);
        mAdapter.setList(mLocalMediaList);
        mAdapter.setSelectMax(9);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setItemLongClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        initTouchHelper();

        //投票
        mVoteButton.setOnClickListener(this);
        mVoteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mVoteRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    mVoteRecyclerView.setVisibility(View.GONE);
                }
            }
        });
        mDiTopicvoteList = new ArrayList<>();
        mDiTopicvoteList.add(new DiTopicvote());
        mDiTopicvoteList.add(new DiTopicvote(CommonConstants.INCOME_SCAN_ADDBUTTON));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mVoteRecyclerView.setLayoutManager(layoutManager);
        //自定义分割线的样式
        mVoteRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 0, ContextCompat.getColor(this, R.color.common_division_line)));
        mAdapterVote = new AddTopicVoteRecyclerAdapter(this, mDiTopicvoteList);
        mVoteRecyclerView.setAdapter(mAdapterVote);

    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;

            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                saveTopic();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.m_location_button:
                intent = new Intent(this, ChooseLocationActivity.class);
                intent.putExtra("POI", ParseJsonToObject.getJsonFromObjList(mPoiList).toString());
                startActivityForResult(intent, REQUEST_CODE_CHOOSE_LOCAL);
                break;

            case R.id.m_vote_button:
                mVoteSwitch.setChecked(!mVoteSwitch.isChecked());
                break;

        }
    }

    /**
     * 此方法必须重写，以决绝退出activity时 dialog未dismiss而报错的bug
     */
    @Override
    protected void onDestroy() {
        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {
            System.out.println("myDialog取消，失败！");
        }
        super.onDestroy();
    }

    ///////////////图片选择器相关//////////////////////////////图片选择器相关//////////////////////////////图片选择器相关//////////////////////////////图片选择器相关//////////////////////////////图片选择器相关///////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    mLocalMediaList.clear();
                    mLocalMediaList.addAll(PictureSelector.obtainMultipleResult(data));
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    mAdapter.notifyDataSetChanged();
                    break;

                case REQUEST_CODE_CHOOSE_LOCAL:
                    String poiLoca = data.getStringExtra("POI");
                    mLocationButton.setText(poiLoca);
                    mDiTopic.setLocation(poiLoca);
                    break;
            }
        }
    }

    @Override
    public void onItemClick(int position, View v) {
        // 预览图片 可自定长按保存路径
        //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
        //PictureSelector.create(AddTopicActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, mLocalMediaList);

        if (mLocalMediaList.size() > 0) {
            LocalMedia media = mLocalMediaList.get(position);
            String mimeType = media.getMimeType();
            int mediaType = PictureMimeType.getMimeType(mimeType);
            switch (mediaType) {
                case PictureConfig.TYPE_VIDEO:
                    // 预览视频
                    PictureSelector.create(AddTopicActivity.this).externalPictureVideo(media.getPath());
                    break;
                case PictureConfig.TYPE_AUDIO:
                    // 预览音频
                    PictureSelector.create(AddTopicActivity.this).externalPictureAudio(
                            media.getPath().startsWith("content://") ? media.getAndroidQToPath() : media.getPath());
                    break;
                default:
                    // 预览图片 可自定长按保存路径
                    PictureSelector.create(AddTopicActivity.this)
                            .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                            .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                            .setPictureWindowAnimationStyle(new PictureWindowAnimationStyle(R.anim.activity_slide_in, R.anim.activity_slide_out))// 自定义相册启动退出动画
                            .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                            .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                            .openExternalPreview(position, mLocalMediaList);
                    break;
            }
        }
    }

    @Override
    public void onItemLongClick(RecyclerView.ViewHolder holder, int position, View v) {
        //如果item不是最后一个，则执行拖拽
        needScaleBig = true;
        needScaleSmall = true;
        int size = mAdapter.getList().size();
        if (size != 9) {
            mItemTouchHelper.startDrag(holder);
            return;
        }
        if (holder.getLayoutPosition() != size - 1) {
            mItemTouchHelper.startDrag(holder);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter != null) {
            outState.putParcelableArrayList("selectorList",
                    (ArrayList<? extends Parcelable>) mAdapter.getList());
        }
    }

    private void initTouchHelper() {
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int itemViewType = viewHolder.getItemViewType();
                if (itemViewType != mAdapter.TYPE_CAMERA) {
                    viewHolder.itemView.setAlpha(0.7f);
                }
                return makeMovementFlags(ItemTouchHelper.DOWN | ItemTouchHelper.UP
                        | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //得到item原来的position
                try {
                    int fromPosition = viewHolder.getAdapterPosition();
                    //得到目标position
                    int toPosition = target.getAdapterPosition();
                    int itemViewType = target.getItemViewType();
                    if (itemViewType != mAdapter.TYPE_CAMERA) {
                        if (fromPosition < toPosition) {
                            for (int i = fromPosition; i < toPosition; i++) {
                                Collections.swap(mAdapter.getList(), i, i + 1);
                            }
                        } else {
                            for (int i = fromPosition; i > toPosition; i--) {
                                Collections.swap(mAdapter.getList(), i, i - 1);
                            }
                        }
                        mAdapter.notifyItemMoved(fromPosition, toPosition);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                int itemViewType = viewHolder.getItemViewType();
                if (itemViewType != mAdapter.TYPE_CAMERA) {

                    if (needScaleBig) {
                        //如果需要执行放大动画
                        viewHolder.itemView.animate().scaleXBy(0.1f).scaleYBy(0.1f).setDuration(100);
                        //执行完成放大动画,标记改掉
                        needScaleBig = false;
                        //默认不需要执行缩小动画，当执行完成放大 并且松手后才允许执行
                        needScaleSmall = false;
                    }

                    if (View.INVISIBLE == viewHolder.itemView.getVisibility()) {
                        //如果viewHolder不可见，则表示用户放手，重置删除区域状态
                    }
                    if (needScaleSmall) {//需要松手后才能执行
                        viewHolder.itemView.animate().scaleXBy(1f).scaleYBy(1f).setDuration(100);
                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
                needScaleSmall = true;
                return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int itemViewType = viewHolder.getItemViewType();
                if (itemViewType != mAdapter.TYPE_CAMERA) {
                    viewHolder.itemView.setAlpha(1.0f);
                    super.clearView(recyclerView, viewHolder);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        // 绑定拖拽事件
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onAddPicClick() {
        //PictureFileUtils.deleteAllCacheDirFile(this);
        getWeChatStyle();
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(AddTopicActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                .setPictureWindowAnimationStyle(new PictureWindowAnimationStyle(R.anim.activity_slide_in, R.anim.activity_slide_out))// 自定义相册启动退出动画
                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                .maxSelectNum(9)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .isPreviewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .isEnableCrop(false)// 是否裁剪 true or false
                .isCompress(true)// 是否压缩 true or false
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(true)// 是否显示gif图片 true or false
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .isOpenClickSound(false)// 是否开启点击声音 true or false
                .selectionData(mLocalMediaList)// 是否传入已选图片 List<LocalMedia> list
                .isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .minimumCompressSize(300)// 小于100kb的图片不压缩
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private void getWeChatStyle() {
        // 相册主题
        mPictureParameterStyle = new PictureParameterStyle();
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle.isChangeStatusBarFontColor = false;
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle.isOpenCompletedNumStyle = false;
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle.isOpenCheckNumStyle = true;
        // 状态栏背景色
        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#393a3e");
        // 相册列表标题栏背景色
        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e");
        // 相册父容器背景色
        mPictureParameterStyle.pictureContainerBackgroundColor = ContextCompat.getColor(this, R.color.common_color_black);
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_wechat_up;
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_wechat_down;
        // 相册文件夹列表选中圆点
        mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_selector_num_oval_blue;
        // 相册返回箭头
        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_close;
        // 标题栏字体颜色
        mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(this, R.color.picture_color_white);
        // 相册右侧按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(this, R.color.picture_color_53575e);
        // 相册右侧按钮字体默认颜色
        mPictureParameterStyle.pictureRightDefaultTextColor = ContextCompat.getColor(this, R.color.picture_color_53575e);
        // 相册右侧按可点击字体颜色,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureRightSelectedTextColor = ContextCompat.getColor(this, R.color.picture_color_white);
        // 相册右侧按钮背景样式,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureUnCompleteBackgroundStyle = R.drawable.picture_send_button_default_bg;
        // 相册右侧按钮可点击背景样式,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureCompleteBackgroundStyle = R.drawable.picture_send_button_bg;
        // 相册列表勾选图片样式
        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_wechat_num_selector;
        // 相册标题背景样式 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatTitleBackgroundStyle = R.drawable.picture_album_bg;
        // 微信样式 预览右下角样式 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatChooseStyle = R.drawable.picture_wechat_select_cb;
        // 相册返回箭头 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatLeftBackStyle = R.drawable.picture_icon_back;
        // 相册列表底部背景色
        mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(this, R.color.picture_color_grey);
        // 已选数量圆点背景样式
        mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(this, R.color.picture_color_white);
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(this, R.color.picture_color_9b);
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(this, R.color.picture_color_white);
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(this, R.color.picture_color_53575e);
        // 预览界面底部背景色
        mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(this, R.color.picture_color_half_grey);
        // 外部预览界面删除按钮样式
        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete;
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalControlStyle = R.drawable.picture_original_wechat_checkbox;
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalFontColor = ContextCompat.getColor(this, R.color.common_color_white);
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = false;
        // 设置NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP有效
        mPictureParameterStyle.pictureNavBarColor = Color.parseColor("#393a3e");

        // 完成文案是否采用(%1$d/%2$d)的字符串，只允许两个占位符哟
//        mPictureParameterStyle.isCompleteReplaceNum = true;
        // 自定义相册右侧文本内容设置
//        mPictureParameterStyle.pictureUnCompleteText = getString(R.string.app_wechat_send);
        //自定义相册右侧已选中时文案 支持占位符String 但只支持两个 必须isCompleteReplaceNum为true
//        mPictureParameterStyle.pictureCompleteText = getString(R.string.app_wechat_send_num);
//        // 自定义相册列表不可预览文字
//        mPictureParameterStyle.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        mPictureParameterStyle.picturePreviewText = "";
//        // 自定义预览页右下角选择文字文案
//        mPictureParameterStyle.pictureWeChatPreviewSelectedText = "";

//        // 自定义相册标题文字大小
//        mPictureParameterStyle.pictureTitleTextSize = 9;
//        // 自定义相册右侧文字大小
//        mPictureParameterStyle.pictureRightTextSize = 9;
//        // 自定义相册预览文字大小
//        mPictureParameterStyle.picturePreviewTextSize = 9;
//        // 自定义相册完成文字大小
//        mPictureParameterStyle.pictureCompleteTextSize = 9;
//        // 自定义原图文字大小
//        mPictureParameterStyle.pictureOriginalTextSize = 9;
//        // 自定义预览页右下角选择文字大小
//        mPictureParameterStyle.pictureWeChatPreviewSelectedTextSize = 9;

        // 裁剪主题
        mCropParameterStyle = new PictureCropParameterStyle(
                ContextCompat.getColor(this, R.color.picture_color_grey),
                ContextCompat.getColor(this, R.color.picture_color_grey),
                Color.parseColor("#393a3e"),
                ContextCompat.getColor(this, R.color.common_color_white),
                mPictureParameterStyle.isChangeStatusBarFontColor);
    }

    ///////////////图片选择器相关//////////////////////////////图片选择器相关//////////////////////////////图片选择器相关//////////////////////////////图片选择器相关//////////////////////////////图片选择器相关///////////////

    private void saveTopic() {
        mTopicContent.requestFocus();//获取焦点 光标出现
        String desc = mTopicContent.getText().toString();
        if (CommonUtility.isEmpty(desc)) {
            Toast.makeText(this, R.string.topic_add_tip_text_tip, Toast.LENGTH_SHORT).show();
            return;
        }
        //等待框
        mProgressDialog.show();

        mDiTopic.setUserid(ApplicationData.mDiUserInfo.getId());
        mDiTopic.setName(ApplicationData.mDiUserInfo.getName());
        mDiTopic.setDescription(desc);
        //上传图片
        if (null != mLocalMediaList && mLocalMediaList.size() > 0) {
            mDiTopic.setTopicimg(new ArrayList<DiTopicimg>());
            LocalMedia localMedia = null;
            for (int i = 0; i < mLocalMediaList.size(); i++) {
                final int counti = i;
                localMedia = mLocalMediaList.get(i);
                // 例如 LocalMedia 里面返回五种path
                // 1.media.getPath(); 原图path
                // 2.media.getCutPath();裁剪后path，需判断media.isCut();切勿直接使用
                // 3.media.getCompressPath();压缩后path，需判断media.isCompressed();切勿直接使用
                // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                // 5.media.getAndroidQToPath();Android Q版本特有返回的字段，但如果开启了压缩或裁剪还是取裁剪或压缩路径；注意：.isAndroidQTransform 为false 此字段将返回空
                // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩
                Uri uri = null;
                if (localMedia.isCompressed()) {
                    uri = Uri.fromFile(new File(localMedia.getCompressPath()));
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = Uri.fromFile(new File(localMedia.getAndroidQToPath()));
                    } else {
                        uri = Uri.fromFile(new File(localMedia.getPath()));
                    }
                }
                Bitmap bitmap = CommonUtility.getScalingBitmap(uri, this);
                String imgData = CommonUtility.encodeTobase64(bitmap);
                SaDataProccessHandler<Void, Void, DiTopicimg> dataVerHandler = new SaDataProccessHandler<Void, Void, DiTopicimg>(this) {
                    @Override
                    public void onSuccess(DiTopicimg data) {
                        DiTopicimg topicimg = new DiTopicimg();
                        topicimg.setPath(data.getPath());
                        topicimg.setWidth(bitmap.getWidth());
                        topicimg.setHeight(bitmap.getHeight());
                        mDiTopic.getTopicimg().add(topicimg);
                        if (counti == (mLocalMediaList.size() - 1)) {
                            postTopic();
                        }
                    }

                    @Override
                    public void onPreExecute() {
                    }

                    @Override
                    public void handlerError(SaException e) {
                        super.handlerError(e);
                        mProgressDialog.dismiss();
                    }
                };
                TopicService.getInstance().uploadImg(imgData, dataVerHandler);
            }
        } else {
            postTopic();
        }
    }

    private void postTopic() {
        //投票
        mDiTopic.setTopicvote(mAdapterVote.getTotalVotes());
        //保存
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(this) {
            @Override
            public void onSuccess(Void data) {
                Toast.makeText(AddTopicActivity.this, R.string.topic_add_send_success_top, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
                TopicFragment.isRefresh = true;
                AddTopicActivity.this.finish();
            }

            @Override
            public void onPreExecute() {
            }

            @Override
            public void handlerError(SaException e) {
                super.handlerError(e);
                mProgressDialog.dismiss();
            }
        };
        TopicService.getInstance().seveTopic(mDiTopic, dataVerHandler);
    }

    ///////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关///////////////
    private void initLocation() {
        try {
            LocationClient.setAgreePrivacy(true);
            mLocationClient = new LocationClient(getApplicationContext());
            //声明LocationClient类
            myListener = new MyLocationListener();
            mLocationClient.registerLocationListener(myListener);
            //注册监听函数
            //第三步，配置定位SDK参数
            LocationClientOption option = new LocationClientOption();
            option.setIsNeedAddress(true);
            option.setIsNeedLocationPoiList(true);
            //可选，是否需要周边POI信息，默认为不需要，即参数为false
            //如果开发者需要获得周边POI信息，此处必须为true
            mLocationClient.setLocOption(option);
            //mLocationClient为第二步初始化过的LocationClient对象
            //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
            //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
            mLocationClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //保存经纬度和
            mDiTopic.setLatitude(String.valueOf(location.getLatitude()));
            mDiTopic.setLongitude(String.valueOf(location.getLongitude()));
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取周边POI信息相关的结果
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            if (null == location) {
                Toast.makeText(AddTopicActivity.this, R.string.scan_location_tip, Toast.LENGTH_SHORT).show();
                return;
            }
            String cityStr = "中国";
            if (!CommonUtility.isEmpty(location.getCity())) {
                cityStr = location.getCity();
            }
            if (null != location.getPoiList() && location.getPoiList().size() > 0) {
                //位置
                mPoiList = new ArrayList<>();
                mPoiList.add(cityStr);
                for (Poi poi : location.getPoiList()) {
                    mPoiList.add(cityStr + "·" + poi.getName());
                }
                cityStr += "·" + location.getPoiList().get(0).getName();
            }
            mDiTopic.setLocation(cityStr);
            mLocationButton.setText(cityStr);
            //获取周边POI信息
            //POI信息包括POI ID、名称等，具体信息请参照类参考中POI类的相关说明
        }
    }

    ///////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关///////////////

}
