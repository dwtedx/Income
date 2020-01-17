package com.dwtedx.income.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;

import com.dwtedx.income.topic.AddTopicActivity;
import com.dwtedx.income.topic.GlideEngine;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.dwtedx.income.IncomeApplication;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiParacontent;
import com.dwtedx.income.entity.DiVersion;
import com.dwtedx.income.home.adapter.HomeFragmetAdapter;
import com.dwtedx.income.profile.SetupActivity;
import com.dwtedx.income.provider.HomeScanTipSharedPreferences;
import com.dwtedx.income.scan.ScanResultActivity;
import com.dwtedx.income.service.IncomeService;
import com.dwtedx.income.service.UserService;
import com.dwtedx.income.updateapp.UpdateService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.SideViewPager;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.util.List;

public class HomeV3Activity extends BaseActivity implements ViewPager.OnPageChangeListener, SideViewPager.onSideListener{

    private BottomNavigationView mNavigation;

    private boolean mIsStartActivity;
    private SideViewPager mViewpager;
    private HomeFragmetAdapter adapter;

    private ImageView mAddRecordView;

    //版本更新
    private DiVersion mDiVersion;
    private final static int ACCESS_COARSE_STORAGE_REQUEST_CODE = 70;

    //百度 ocr 拍照路径
    public static final String KEY_OUTPUT_FILE_PATH = "key_output_file_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homev3);

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        //当labelVisibilityMode==0时或按钮数大于3则位移，那么只要将labelVisibilityMode值设置为不是0和-1就可以了
        mNavigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        //mNavigation.setItemTextAppearanceActive(R.style.bottom_selected_text);
        //mNavigation.setItemTextAppearanceInactive(R.style.bottom_normal_text);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //mNavigation.setSelectedItemId(R.id.navigation_notifications2);

        mViewpager = (SideViewPager) findViewById(R.id.view_pagers);
        adapter = new HomeFragmetAdapter(getSupportFragmentManager(), this);
        //viewpager加载adapter
        mViewpager.setAdapter(adapter);
        mViewpager.addOnPageChangeListener(this);
        mViewpager.setOnSideListener(this);

        mAddRecordView = (ImageView) findViewById(R.id.m_add_record_view);
        mAddRecordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "更多", Toast.LENGTH_LONG).show();
                PopMenuView.getInstance().show(HomeV3Activity.this, mAddRecordView);
            }
        });

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
                    Toast.makeText(HomeV3Activity.this, R.string.app_name, Toast.LENGTH_SHORT).show();
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
    public void onLeftSide() {
        // 左边界滑动时处理
        if(!mIsStartActivity) {
            startActivity(new Intent(this, IncomeListActivity.class));
            mIsStartActivity = true;
        }
    }

    @Override
    public void onRightSide() {
        // 右边界滑动时处理
        if(!mIsStartActivity) {
            startActivity(new Intent(this, SetupActivity.class));
            mIsStartActivity = true;
        }
    }

    @Override
    public void onBackPressed() {
        // 当popupWindow 正在展示的时候 按下返回键 关闭popupWindow 否则关闭activity
        if (PopMenuView.getInstance().isShowing()) {
            PopMenuView.getInstance().closePopupWindowAction();
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

    private void initFolder() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, ACCESS_COARSE_STORAGE_REQUEST_CODE);
        } else {
            checkCacheFolder();
        }
    }

    public void checkCacheFolder() {
        // create folder
        File qhFolder = new File(CommonConstants.INCOME);
        if (!qhFolder.exists()) {
            qhFolder.mkdirs();
        }

        File imageFolder = new File(CommonConstants.INCOME_IMAGES);
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }

        File tempFolder = new File(CommonConstants.INCOME_TEMP);
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }

        File downFolder = new File(CommonConstants.INCOME_DOWN);
        if (!downFolder.exists()) {
            downFolder.mkdirs();
        }

        File viodeFolder = new File(CommonConstants.INCOME_VIDEO);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_COARSE_STORAGE_REQUEST_CODE) {
            try {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    checkCacheFolder();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "访问被拒绝！会导致很多功能异常！请到设置里面开启", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "访问被拒绝！会导致很多功能异常！请到设置里面开启", Toast.LENGTH_SHORT).show();
            }
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
            Toast.makeText(this, R.string.home_scan_error_tip, Toast.LENGTH_SHORT).show();
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

        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(HomeV3Activity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .theme(R.style.picture_income_wechat_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                .setPictureWindowAnimationStyle(new PictureWindowAnimationStyle(R.anim.activity_slide_in, R.anim.activity_slide_out))// 自定义相册启动退出动画
                //.setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                //.maxSelectNum(1)// 最大图片选择数量 int
                //.minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)// 设置相册Activity方向，不设置默认使用系统
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                //.glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(3, 4)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                //.compressSavePath("/Income/Images")//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                //.selectionMedia(mLocalMediaList)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cutOutQuality(90)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(300)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.cropWH(500, 800)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .videoQuality(1)// 视频录制质量 0 or 1 int
                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(30)//视频秒数录制 默认60s int
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

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
