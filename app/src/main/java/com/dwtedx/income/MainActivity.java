package com.dwtedx.income;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.adapter.GuideViewPagerAdapter;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiUserInfo;
import com.dwtedx.income.home.HomeV2Activity;
import com.dwtedx.income.home.HomeV3Activity;
import com.dwtedx.income.profile.WebViewActivity;
import com.dwtedx.income.provider.AppUpdateVersionSharedPreferences;
import com.dwtedx.income.provider.CustomerIDSharedPreferences;
import com.dwtedx.income.provider.FingerprintSetupSharedPreferences;
import com.dwtedx.income.provider.HomePrivacySharedPreferences;
import com.dwtedx.income.service.UserService;

public class MainActivity extends BaseActivity {

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
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));
        
        initView();
        initUserPrivacy(); //用户协议和隐私政策
        handleGuide();
    }

    /**
     * 初始化
     */
    private void handleGuide() {
        if(HomePrivacySharedPreferences.getIsTip()){
            return;
        }
        //是否要自动登录
        CustomerIDSharedPreferences.init(MainActivity.this);
        if(-1 != CustomerIDSharedPreferences.getCustomerId()){
            getCustomerInfo(CustomerIDSharedPreferences.getCustomerId());
        }

        //启动动画
        new Handler() {
            public void handleMessage(android.os.Message msg) {
                AppUpdateVersionSharedPreferences.init(getApplicationContext());
                //是否加载引导
                if (AppUpdateVersionSharedPreferences.getIsFirstOpen()) {
                    initGuidePager();
                } else {
                    showHomeV3();
                }
            };
        }.sendEmptyMessageDelayed(0, 2000);
    }

    private void initView() {
        mLodingImageLayout = (RelativeLayout) findViewById(R.id.loding_image_layout);
        mForwardBtnLayout = (LinearLayout) findViewById(R.id.forward_btn_layout);
        mForwardTextView = (TextView) findViewById(R.id.title_back_btn_contents);
        mLodingGauidImageLayout = (RelativeLayout) findViewById(R.id.loding_gauid_image_layout);
        mGuideImageView = (ViewPager) findViewById(R.id.loding_gauid_image);
        mLodingVertion = (TextView) findViewById(R.id.loding_vertion);
        mLodingVertion.setText(ApplicationData.mAppVersion);
        HomePrivacySharedPreferences.init(this);
    }

    private void showHomeV2() {
        FingerprintSetupSharedPreferences.init(getApplicationContext());
        if(FingerprintSetupSharedPreferences.getFingerprintSetup()){
            startActivity(new Intent(MainActivity.this, MainFingerprintActivity.class));
            finish();
        }else {
            startActivity(new Intent(MainActivity.this, HomeV2Activity.class));
            finish();
        }
    }
    private void showHomeV3() {
        FingerprintSetupSharedPreferences.init(getApplicationContext());
        if(FingerprintSetupSharedPreferences.getFingerprintSetup()){
            startActivity(new Intent(MainActivity.this, MainFingerprintActivity.class));
            finish();
        }else {
            startActivity(new Intent(MainActivity.this, HomeV3Activity.class));
            finish();
        }
    }

    /**
     * 初始化图片
     */
    private void initGuidePager() {
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

    /**
     * 用户信息
     * @param id
     */
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

    /**
     * 用户协议和隐私政策
     */
    private void initUserPrivacy() {
        if(HomePrivacySharedPreferences.getIsTip()){
            View view = getLayoutInflater().inflate(R.layout.activity_homev3_privacy, null, false);
            TextView textView = (TextView) view.findViewById(R.id.m_home_privacy_content);

            final SpannableStringBuilder style = new SpannableStringBuilder();
            //设置文字
            style.append(getString(R.string.home_privacy_content));
            //设置部分文字点击事件
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                    intent.putExtra("url", "http://income.dwtedx.com/useragreement.html");
                    intent.putExtra("title", getString(R.string.home_useragreement));
                    startActivity(intent);
                }
            };
            style.setSpan(clickableSpan, 56, 62, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                    intent.putExtra("url", "http://income.dwtedx.com/privacy.html");
                    intent.putExtra("title", getString(R.string.home_privacy));
                    startActivity(intent);
                }
            };
            style.setSpan(clickableSpan1, 63, 69, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置部分文字颜色
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#FF4081"));
            style.setSpan(foregroundColorSpan, 56, 62, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(foregroundColorSpan, 63, 69, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //配置给TextView
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(style);

            new MaterialDialog.Builder(this)
                    .title(R.string.home_privacy_useragreement)
                    .customView(view, false)
                    .positiveText(R.string.home_privacy_ok)
                    .negativeText(R.string.home_privacy_cancel)
                    .cancelable(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            //NEGATIVE   POSITIVE
                            if (which.name().equals("POSITIVE")) {
                                HomePrivacySharedPreferences.setIsTip(false);
                                //Application的初始化
                                ApplicationData.mIncomeApplication.initUMeng();
                                //ApplicationData.mIncomeApplication.initAlibcTrade();
                                ApplicationData.mIncomeApplication.initAccessTokenWithAkSk();
                                ApplicationData.mIncomeApplication.initX5WebView();
                                handleGuide();
                            }
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback(){

                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            System.exit(0);
                        }
                    })
                    .show();
        }
    }
    
}
