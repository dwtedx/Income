package com.dwtedx.income;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dwtedx.income.adapter.GuideViewPagerAdapter;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiUserInfo;
import com.dwtedx.income.home.HomeV2Activity;
import com.dwtedx.income.home.HomeV3Activity;
import com.dwtedx.income.provider.AppUpdateVersionSharedPreferences;
import com.dwtedx.income.provider.CustomerIDSharedPreferences;
import com.dwtedx.income.provider.FingerprintSetupSharedPreferences;
import com.dwtedx.income.service.UserService;

public class MainShortcutActivity extends BaseActivity {

    private ViewPager mGuideImageView;
    private TextView mLodingVertion;
    private RelativeLayout mLodingImageLayout;
    private RelativeLayout mLodingGauidImageLayout;

    private int curIndex;
    private int oldIndex = 0;
    private TextView mForwardTextView;
    private LinearLayout mForwardBtnLayout;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //状态栏颜色 不支持4.4
        //getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            this.getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));
        }

        mLodingImageLayout = (RelativeLayout) findViewById(R.id.loding_image_layout);
        mForwardBtnLayout = (LinearLayout) findViewById(R.id.forward_btn_layout);
        mForwardTextView = (TextView) findViewById(R.id.title_back_btn_contents);
        mLodingGauidImageLayout = (RelativeLayout) findViewById(R.id.loding_gauid_image_layout);
        mGuideImageView = (ViewPager) findViewById(R.id.loding_gauid_image);
        mLodingVertion = (TextView) findViewById(R.id.loding_vertion);
        mLodingVertion.setText(ApplicationData.mAppVersion);

        //是否要自动登录
        CustomerIDSharedPreferences.init(MainShortcutActivity.this);
        CustomerIDSharedPreferences.init(MainShortcutActivity.this);
        if(-1 != CustomerIDSharedPreferences.getCustomerId()){
            getCustomerInfo(CustomerIDSharedPreferences.getCustomerId());
        }

        //启动动画
        new Handler() {
            public void handleMessage(android.os.Message msg) {
                AppUpdateVersionSharedPreferences.init(getApplicationContext());
                //是否加载引导
                if (AppUpdateVersionSharedPreferences.getIsFirstOpen()) {
                    InitGuidePager();
                } else {
                    showHomeV3();
                }
            };
        }.sendEmptyMessage(0);
    }

    private void showHomeV2() {
        FingerprintSetupSharedPreferences.init(getApplicationContext());
        if(FingerprintSetupSharedPreferences.getFingerprintSetup()){
            startActivity(new Intent(MainShortcutActivity.this, MainFingerprintActivity.class));
            finish();
        }else {
            startActivity(new Intent(MainShortcutActivity.this, HomeV2Activity.class));
            finish();
        }
    }
    private void showHomeV3() {
        FingerprintSetupSharedPreferences.init(getApplicationContext());
        if(FingerprintSetupSharedPreferences.getFingerprintSetup()){
            startActivity(new Intent(MainShortcutActivity.this, MainFingerprintActivity.class));
            finish();
        }else {
            startActivity(new Intent(MainShortcutActivity.this, HomeV3Activity.class));
            finish();
        }
    }

    /**
     * 初始化图片
     */
    private void InitGuidePager() {
        //状态栏颜色 不支持4.4
        //getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        final GuideViewPagerAdapter pagerAdapter = new GuideViewPagerAdapter(getSupportFragmentManager());
        mLodingGauidImageLayout.setVisibility(View.VISIBLE);
        mLodingImageLayout.setVisibility(View.GONE);
        mGuideImageView.setAdapter(pagerAdapter);

        // 设置圆点
        final LinearLayout ovalLayout = (LinearLayout) findViewById(R.id.loding_gauid_image_oval);
        LayoutInflater inflater = LayoutInflater.from(this);
        ovalLayout.removeAllViews();
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            ovalLayout.addView(inflater.inflate(R.layout.ad_bottom_item, ovalLayout, false));
        }
        //选中第一个
        ovalLayout.getChildAt(0).findViewById(R.id.ad_item_v).setBackgroundResource(R.mipmap.dot_on);
        mGuideImageView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                curIndex = position;
                //取消圆点选中
                ovalLayout.getChildAt(oldIndex).findViewById(R.id.ad_item_v).setBackgroundResource(R.mipmap.dot_off);
                //圆点选中
                ovalLayout.getChildAt(curIndex).findViewById(R.id.ad_item_v).setBackgroundResource(R.mipmap.dot_on);
                oldIndex = curIndex;
                //点击事件
                if (position == (pagerAdapter.getCount() - 1)) {
                    mForwardTextView.setBackgroundResource(android.R.color.transparent);
                    mForwardTextView.setText("完成");
                }else{
                    mForwardTextView.setBackgroundResource(R.mipmap.forward_withe);
                    mForwardTextView.setText("");
                }
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            public void onPageScrollStateChanged(int arg0) { }
        });

        //点击事件
        mForwardBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件
                if (curIndex == (pagerAdapter.getCount() - 1)) {
                    showHomeV3();
                }else{
                    mGuideImageView.setCurrentItem(curIndex + 1);
                }
            }
        });
    }

    private void getCustomerInfo(int id){
        SaDataProccessHandler<Void, Void, DiUserInfo> dataVerHandler = new SaDataProccessHandler<Void, Void, DiUserInfo>(this) {
            @Override
            public void onSuccess(DiUserInfo data) {
                //Log.i(LoginV2Activity.class.getName(), "登录成功：" + data);
                if(null != data){
                    ApplicationData.mDiUserInfo = data;
                }
            }

            @Override
            public void onPreExecute() {}

            @Override
            public void handlerError(SaException e) {}
        };

        UserService.getInstance().getUserById(id, dataVerHandler);
    }
}
