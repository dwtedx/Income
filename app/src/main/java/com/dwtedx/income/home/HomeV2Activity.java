package com.dwtedx.income.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.dwtedx.income.IncomeApplication;
import com.dwtedx.income.R;
import com.dwtedx.income.addrecord.AddRecordActivity;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.ProgressDialog;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.discovery.ItemCategoryTopActivity;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiParacontent;
import com.dwtedx.income.entity.DiVersion;
import com.dwtedx.income.home.adapter.HomeFragmetAdapter;
import com.dwtedx.income.profile.ProfileActivity;
import com.dwtedx.income.profile.SetupActivity;
import com.dwtedx.income.provider.HomeScanTipSharedPreferences;
import com.dwtedx.income.provider.HomeTipSharedPreferences;
import com.dwtedx.income.report.ReportActivity;
import com.dwtedx.income.scan.ScanResultActivity;
import com.dwtedx.income.service.IncomeService;
import com.dwtedx.income.service.UserService;
import com.dwtedx.income.updateapp.UpdateService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.SideViewPager;
import com.dwtedx.income.widget.rapidfloatingaction.RapidFloatingActionButton;
import com.dwtedx.income.widget.rapidfloatingaction.RapidFloatingActionHelper;
import com.dwtedx.income.widget.rapidfloatingaction.RapidFloatingActionLayout;
import com.dwtedx.income.widget.rapidfloatingaction.contentimpl.labellist.RFACLabelItem;
import com.dwtedx.income.widget.rapidfloatingaction.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.dwtedx.income.widget.rapidfloatingaction.util.RFABShape;
import com.dwtedx.income.widget.rapidfloatingaction.util.RFABTextUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeV2Activity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, SideViewPager.onSideListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    private TabLayout mTabLayout;
    private SideViewPager mViewpager;
    private HomeFragmetAdapter adapter;

    private Button mHomeTipButton;
    private RelativeLayout mHomeTipLayout;
    private LinearLayout mHomeListLayout, mHomeItemLayout;
    private RapidFloatingActionLayout mLabelListSampleRfal;
    private RapidFloatingActionButton rapidFloatingActionButton;
    private RapidFloatingActionHelper mRfabHelper;

    private ProgressDialog mProgressDialog;

    //动态imave
    private ImageView mIitemImageView;
    private int mPosition;
    private boolean mIsStartActivity;

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
        setContentView(R.layout.activity_homev2);

        mTabLayout = (TabLayout) findViewById(R.id.tablayouts);
        mViewpager = (SideViewPager) findViewById(R.id.view_pagers);
        adapter = new HomeFragmetAdapter(getSupportFragmentManager(), this);
        //viewpager加载adapter
        mViewpager.setAdapter(adapter);
        mViewpager.addOnPageChangeListener(this);
        mViewpager.setOnSideListener(this);
        //TabLayout加载viewpager
        mTabLayout.setupWithViewPager(mViewpager);
        //设置TabLayout的模式
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mHomeListLayout = (LinearLayout) findViewById(R.id.home_list_layout);
        mHomeListLayout.setOnClickListener(this);

        mIitemImageView = (ImageView) findViewById(R.id.home_item_imageview);
        mHomeItemLayout = (LinearLayout) findViewById(R.id.home_item_layout);
        mHomeItemLayout.setOnClickListener(this);

        //是否有提示
        HomeTipSharedPreferences.init(this);
        if (HomeTipSharedPreferences.getIsTip()) {
            mHomeTipLayout = (RelativeLayout) findViewById(R.id.home_tip_layout);
            mHomeTipLayout.setVisibility(View.VISIBLE);
            mHomeTipButton = (Button) findViewById(R.id.home_tip_button);
            mHomeTipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHomeTipLayout.setVisibility(View.GONE);
                    HomeTipSharedPreferences.setIsTip(ApplicationData.mAppVersionCode);
                }
            });
        }

        mLabelListSampleRfal = (RapidFloatingActionLayout) findViewById(R.id.m_label_list_sample_rfal);
        rapidFloatingActionButton = (RapidFloatingActionButton) findViewById(R.id.m_label_list_sample_rfab);

        isNetworkAvailable();
        initRapidFloatingAction();
        initFolder();
        getVersions();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsStartActivity = false;
    }

    //RapidFloatingAction按钮////////////RapidFloatingAction按钮////////////RapidFloatingAction按钮////////////RapidFloatingAction按钮//////////
    private void initRapidFloatingAction() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.home_record_scan))
                .setResId(R.mipmap.scan_ticket)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setLabelSizeSp(14)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.home_record_quick))
                .setResId(R.mipmap.record_icon)
                //.setDrawable(getResources().getDrawable(R.drawable.ic_phone_vector_white))
                .setIconNormalColor(0xff056f00)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(Color.WHITE)
                .setLabelSizeSp(14)
                .setLabelBackgroundDrawable(RFABShape.generateCornerShapeDrawable(0xaa000000, RFABTextUtil.dip2px(this, 4)))
                .setWrapper(1)
        );

        rfaContent
                .setItems(items)
                .setIconShadowRadius(RFABTextUtil.dip2px(this, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(RFABTextUtil.dip2px(this, 5));

        mLabelListSampleRfal.setDisableContentDefaultAnimation(true);
        mRfabHelper = new RapidFloatingActionHelper(
                this,
                mLabelListSampleRfal,
                rapidFloatingActionButton,
                rfaContent
        ).build();
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        //Toast.makeText(getContext(), "clicked label: " + position, Toast.LENGTH_SHORT).show();
        mRfabHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        //Toast.makeText(getContext(), "clicked icon: " + position, Toast.LENGTH_SHORT).show();
        mRfabHelper.toggleContent();
        switch (position) {
            case 0:
                getScanParaAndShowScan();
                break;

            case 1:
                Log.i(HomeV2Activity.class.getName(), "AddRecordActivity-----------------------------------------");
                startActivity(new Intent(HomeV2Activity.this, AddRecordActivity.class));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //case R.id.m_label_list_sample_rfab:
            //    startActivity(new Intent(HomeV2Activity.this, AddRecordActivity.class));
            //    break;

            case R.id.nav_header_view:
                startActivity(new Intent(HomeV2Activity.this, ProfileActivity.class));
                break;

            case R.id.home_list_layout:
                startActivity(new Intent(HomeV2Activity.this, IncomeListActivity.class));
                break;

            case R.id.home_item_layout:
                switch (mPosition){
                    case 0:
                        startActivity(new Intent(this, SearchActivity.class));
                        break;

                    case 1:
                        startActivity(new Intent(this, ReportActivity.class));
                        break;

                    case 2:
                        startActivity(new Intent(this, ItemCategoryTopActivity.class));
                        break;

                    case 3:
                        startActivity(new Intent(this, SetupActivity.class));
                        break;
                }
                break;

        }
    }

    public void showProgressDialog() {
        mProgressDialog = getProgressDialog();
        mProgressDialog.show();
    }

    public void cancelProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.cancel();
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
        SaDataProccessHandler<Void, Void, DiVersion> dataVerHandler = new SaDataProccessHandler<Void, Void, DiVersion>(HomeV2Activity.this) {
            @Override
            public void onSuccess(final DiVersion data) {
                if (data.isUpdate()) {
                    new MaterialDialog.Builder(HomeV2Activity.this)
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
            Intent intent = new Intent(HomeV2Activity.this, UpdateService.class);
            intent.putExtra("Key_App_Name", "Income");
            intent.putExtra("Key_Down_Url", mDiVersion.getApkurl());
            HomeV2Activity.this.startService(intent);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mPosition = position;
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mIitemImageView.setImageResource(R.mipmap.home_seareh);
                mIsStartActivity = false;
                break;
            case 1:
                mIitemImageView.setImageResource(R.mipmap.home_more);
                break;
            case 2:
                mIitemImageView.setImageResource(R.mipmap.discovery_catergoy);
                break;
            case 3:
                mIitemImageView.setImageResource(R.mipmap.profile_set);
                mIsStartActivity = false;
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 这个方法在滚动状态改变时被调用，滚动状态共有三种
        // IDLE（空闲状态，没有任何滚动正在进行）
        // DRAGGING（正在拖动图片）
        // Settling（手指离开屏幕，自动完成剩余的动画效果）
        switch (state){
            case ViewPager.SCROLL_STATE_IDLE:
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                break;
        }
    }

    @Override
    public void onLeftSide() {
        // 左边界滑动时处理
        if(!mIsStartActivity) {
            startActivity(new Intent(HomeV2Activity.this, IncomeListActivity.class));
            mIsStartActivity = true;
        }
    }

    @Override
    public void onRightSide() {
        // 右边界滑动时处理
        if(!mIsStartActivity) {
            startActivity(new Intent(HomeV2Activity.this, SetupActivity.class));
            mIsStartActivity = true;
        }
    }


    ////////百度orc扫描//////////////////////////////
    private void getScanParaAndShowScan() {
        if (!IncomeApplication.mHasGotToken) {
            Snackbar.make(mLabelListSampleRfal, R.string.home_scan_error_tip, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        if(null != ApplicationData.scanParacontent){
            showScan();
            return;
        }
        SaDataProccessHandler<Void, Void, List<DiParacontent>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<DiParacontent>>(HomeV2Activity.this) {
            @Override
            public void onSuccess(final List<DiParacontent> data) {
                ApplicationData.scanParacontent = data;
                showScan();
            }
        };
        IncomeService.getInstance().getScanPara(dataVerHandler);
    }

    private void showScan() {
        //是否有提示
        HomeScanTipSharedPreferences.init(this);
        if (HomeScanTipSharedPreferences.getIsTip()) {
            mHomeTipLayout = (RelativeLayout) findViewById(R.id.home_tip_layout);
            ImageView tipImageView = (ImageView) findViewById(R.id.home_tip_image);
            tipImageView.setImageResource(R.mipmap.scan_tip);
            mHomeTipLayout.setVisibility(View.VISIBLE);
            mHomeTipButton = (Button) findViewById(R.id.home_tip_button);
            mHomeTipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHomeTipLayout.setVisibility(View.GONE);
                    HomeScanTipSharedPreferences.setIsTip(ApplicationData.mAppVersionCode);
                    showScan();
                }
            });
            return;
        }
        mCameraFileName = CommonUtility.getTempImageName();
        mCameraFileNamePath = CommonConstants.INCOME_IMAGES + "/" +mCameraFileName;
        Intent intent = new Intent(HomeV2Activity.this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, mCameraFileNamePath);
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
        startActivityForResult(intent, REQUEST_CODE_RECEIPT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 识别成功回调，通用票据识别
        if (requestCode == REQUEST_CODE_RECEIPT && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(HomeV2Activity.this, ScanResultActivity.class);
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, mCameraFileNamePath);
            startActivity(intent);
        }
    }
}
