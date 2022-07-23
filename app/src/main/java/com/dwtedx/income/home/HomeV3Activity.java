package com.dwtedx.income.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.dwtedx.income.IncomeApplication;
import com.dwtedx.income.R;
import com.dwtedx.income.addrecord.AddRecordActivity;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiParacontent;
import com.dwtedx.income.entity.DiVersion;
import com.dwtedx.income.home.adapter.HomeFragmetAdapter;
import com.dwtedx.income.provider.HomeScanTipSharedPreferences;
import com.dwtedx.income.provider.ScanSetupSharedPreferences;
import com.dwtedx.income.scan.ScanResultActivity;
import com.dwtedx.income.service.IncomeService;
import com.dwtedx.income.service.UserService;
import com.dwtedx.income.topic.GlideEngine;
import com.dwtedx.income.updateapp.UpdateService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.FileUtils;
import com.dwtedx.income.utility.PermissionsUtils;
import com.dwtedx.income.utility.ToastUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.util.List;

public class HomeV3Activity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private BottomNavigationView mNavigation;

    private boolean mIsStartActivity;
    private ViewPager mViewpager;
    private HomeFragmetAdapter adapter;

    private ImageView mAddRecordView;

    //版本更新
    private DiVersion mDiVersion;
    private final static int ACCESS_COARSE_STORAGE_REQUEST_CODE = 70;

    //百度 ocr 拍照路径
    public static final String KEY_OUTPUT_FILE_PATH = "key_output_file_path";
    private PictureParameterStyle mPictureParameterStyle;//图片选择器主题
    private PictureCropParameterStyle mCropParameterStyle;

    //记账动画
    private PopMenuView mPopMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homev3);

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        //当labelVisibilityMode==0时或按钮数大于3则位移，那么只要将labelVisibilityMode值设置为不是0和-1就可以了 LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        mNavigation.setLabelVisibilityMode(BottomNavigationView.LABEL_VISIBILITY_LABELED);
        //mNavigation.setItemTextAppearanceActive(R.style.bottom_selected_text);
        //mNavigation.setItemTextAppearanceInactive(R.style.bottom_normal_text);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //mNavigation.setSelectedItemId(R.id.navigation_notifications2);

        mViewpager = (ViewPager) findViewById(R.id.view_pagers);
        adapter = new HomeFragmetAdapter(getSupportFragmentManager(), this);
        //viewpager加载adapter
        mViewpager.setAdapter(adapter);
        mViewpager.addOnPageChangeListener(this);
        mViewpager.setOffscreenPageLimit(4);

        ScanSetupSharedPreferences.init(this);
        mAddRecordView = (ImageView) findViewById(R.id.m_add_record_view);
        mAddRecordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ScanSetupSharedPreferences.getScanSetup()) {
                    //更多记账方式
                    if(null == mPopMenuView) {
                        mPopMenuView = new PopMenuView(HomeV3Activity.this.getApplicationContext());
                    }
                    mPopMenuView.show(HomeV3Activity.this, mAddRecordView);
                }else{
                    startActivity(new Intent(HomeV3Activity.this, AddRecordActivity.class));
                }
            }
        });

        applyPermissions();
        isNetworkAvailable();
        initFolder();
        getVersions();
        showGuide();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_income:
                    mViewpager.setCurrentItem(0);
                    return true;
                case R.id.navigation_report:
                    mViewpager.setCurrentItem(1);
                    return true;
                case R.id.navigation_record:
                    ToastUtil.toastShow(R.string.app_name, ToastUtil.ICON.SUCCESS);
                    return true;
                case R.id.navigation_discovery:
                    mViewpager.setCurrentItem(2);
                    return true;
                case R.id.navigation_profile:
                    mViewpager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mIsStartActivity = false;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mNavigation.setSelectedItemId(R.id.navigation_income);
                mIsStartActivity = false;
                break;
            case 1:
                mNavigation.setSelectedItemId(R.id.navigation_report);
                break;
            case 2:
                mNavigation.setSelectedItemId(R.id.navigation_discovery);
                break;
            case 3:
                mNavigation.setSelectedItemId(R.id.navigation_profile);
                mIsStartActivity = false;
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) { }

    @Override
    public void onBackPressed() {
        // 当popupWindow 正在展示的时候 按下返回键 关闭popupWindow 否则关闭activity
        if (mPopMenuView.isShowing()) {
            mPopMenuView.closePopupWindowAction();
        }
        else {
            super.onBackPressed();
        }
    }

    private void isNetworkAvailable(){
        if(0 == CommonUtility.isNetworkAvailable(this)){
            new MaterialDialog.Builder(this)
                    .title(R.string.tip)
                    .content(R.string.home_network_tip)
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            //NEGATIVE   POSITIVE
                            if(which.name().equals("POSITIVE")){
                                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            }
                        }
                    })
                    .show();
        }
    }

    private void applyPermissions() {
        //没有授权也能回调
        PermissionsUtils.requestPermission(false, this, new PermissionsUtils.Callback() {
            @Override
            public void run(boolean hasPermissions) {
                //这时hasPermissions可能为true也可能为false，取决于用户是否授权
                if (!hasPermissions) {
                    System.exit(0);
                }
            }
        }, PermissionsUtils.PERMISSION_WRITE_EXTERNAL_STORAGE_CAMERA_LOCATION);
    }

    private void initFolder() {
        // create folder
        File qhFolder = new File(FileUtils.getFileRootPath(CommonConstants.INCOME));
        if (!qhFolder.exists()) {
            qhFolder.mkdirs();
        }

        File imageFolder = new File(FileUtils.getFileRootPath(CommonConstants.INCOME_IMAGES));
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }

        File tempFolder = new File(FileUtils.getFileRootPath(CommonConstants.INCOME_TEMP));
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }

        File downFolder = new File(FileUtils.getFileRootPath(CommonConstants.INCOME_DOWN));
        if (!downFolder.exists()) {
            downFolder.mkdirs();
        }

        File viodeFolder = new File(FileUtils.getFileRootPath(CommonConstants.INCOME_VIDEO));
        if (!viodeFolder.exists()) {
            viodeFolder.mkdirs();
        }
    }

    private void getVersions() {
        SaDataProccessHandler<Void, Void, DiVersion> dataVerHandler = new SaDataProccessHandler<Void, Void, DiVersion>(HomeV3Activity.this) {
            @Override
            public void onSuccess(final DiVersion data) {
                ApplicationData.mAppVersionAudit = data.getAudit();
                if (data.isUpdate() && isForeground()) {
                    new MaterialDialog.Builder(HomeV3Activity.this)
                            .title(getString(R.string.tip_update) + data.getVersion())
                            .content(data.getContent())
                            .positiveText(R.string.ok)
                            .negativeText(R.string.cancel)
                            .onAny(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    //NEGATIVE   POSITIVE
                                    if (which.name().equals("POSITIVE")) {
                                        mDiVersion = data;
                                        showUpdate();
                                    }
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onPreExecute() { }

            @Override
            public void handlerError(SaException e) { }
        };
        UserService.getInstance().versionUpdate(dataVerHandler);
    }

    private void showUpdate() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, ACCESS_COARSE_STORAGE_REQUEST_CODE);
        } else {
            Intent intent = new Intent(HomeV3Activity.this, UpdateService.class);
            intent.putExtra("Key_App_Name", "Income");
            intent.putExtra("Key_Down_Url", mDiVersion.getApkurl());
            HomeV3Activity.this.startService(intent);
        }
    }

    private void showGuide() {
        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(600);
        enterAnimation.setFillAfter(true);
        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(300);
        exitAnimation.setFillAfter(true);
        NewbieGuide.with(this)
                .setLabel("income_detail_guide")
                .alwaysShow(false)//总是显示，调试时可以打开
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(mAddRecordView, HighLight.Shape.CIRCLE, 5)
                        .setLayoutRes(R.layout.activity_homev3_tip)
                        .setEnterAnimation(enterAnimation)//进入动画
                        .setExitAnimation(exitAnimation))//退出动画
                .show();
    }

    ////////百度orc扫描//////////////////////////////////////百度orc扫描//////////////////////////////////////百度orc扫描//////////////////////////////
    public void getScanParaAndShowScan() {
        if (!IncomeApplication.mHasGotToken) {
            //Snackbar.make(mLabelListSampleRfal, R.string.home_scan_error_tip, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            ToastUtil.toastShow(R.string.home_scan_error_tip, ToastUtil.ICON.WARNING);
            return;
        }
        if(null != ApplicationData.scanParacontent){
            showScan();
            return;
        }
        SaDataProccessHandler<Void, Void, List<DiParacontent>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<DiParacontent>>(HomeV3Activity.this) {
            @Override
            public void onSuccess(final List<DiParacontent> data) {
                ApplicationData.scanParacontent = data;
                showScan();
            }
        };
        IncomeService.getInstance().getScanPara(dataVerHandler);
    }

    public void showScan() {
        //是否有提示
        HomeScanTipSharedPreferences.init(this);
        if (HomeScanTipSharedPreferences.getIsTip()) {
        //if (true) {
            PopScanTipView.getInstance().show(HomeV3Activity.this, mAddRecordView);
            return;
        }

        //PictureFileUtils.deleteAllCacheDirFile(this);
        getWeChatStyle();
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(HomeV3Activity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                .setPictureWindowAnimationStyle(new PictureWindowAnimationStyle(R.anim.activity_slide_in, R.anim.activity_slide_out))// 自定义相册启动退出动画
                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .isPreviewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .isEnableCrop(true)// 是否裁剪 true or false
                .isCompress(true)// 是否压缩 true or false
                .withAspectRatio(3, 4)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .minimumCompressSize(300)// 小于100kb的图片不压缩
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .isSingleDirectReturn(true)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
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
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //QQ与新浪不需要添加Activity，但需要在使用QQ分享或者授权的Activity中，添加：
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        // 识别成功回调，通用票据识别
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    List<LocalMedia> mediaList = PictureSelector.obtainMultipleResult(data);
                    String path = null;
                    if(mediaList.get(0).isCut()){
                        path = mediaList.get(0).getCutPath();
                    }else{
                        path = mediaList.get(0).getPath();
                    }
                    Intent intent = new Intent(HomeV3Activity.this, ScanResultActivity.class);
                    intent.putExtra(KEY_OUTPUT_FILE_PATH, path);
                    startActivity(intent);
                    break;
            }
        }
    }
    ////////百度orc扫描//////////////////////////////////////百度orc扫描//////////////////////////////////////百度orc扫描//////////////////////////////
}
