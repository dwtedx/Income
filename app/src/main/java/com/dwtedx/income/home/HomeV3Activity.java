package com.dwtedx.income.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.baidu.ocr.ui.camera.CameraActivity;
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
    private String mCameraFileName;
    private String mCameraFileNamePath;
    private static final int REQUEST_CODE_RECEIPT = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homev3);

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        //当labelVisibilityMode==0时或按钮数大于3则位移，那么只要将labelVisibilityMode值设置为不是0和-1就可以了
        mNavigation.setLabelVisibilityMode(1);
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
                if (data.isUpdate()) {
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
        mCameraFileName = CommonUtility.getTempImageName();
        mCameraFileNamePath = CommonConstants.INCOME_IMAGES + "/" +mCameraFileName;
        Intent intent = new Intent(HomeV3Activity.this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, mCameraFileNamePath);
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
        startActivityForResult(intent, REQUEST_CODE_RECEIPT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //QQ与新浪不需要添加Activity，但需要在使用QQ分享或者授权的Activity中，添加：
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        // 识别成功回调，通用票据识别
        if (requestCode == REQUEST_CODE_RECEIPT && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(HomeV3Activity.this, ScanResultActivity.class);
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, mCameraFileNamePath);
            startActivity(intent);
        }
    }
    ////////百度orc扫描//////////////////////////////////////百度orc扫描//////////////////////////////////////百度orc扫描//////////////////////////////
}
