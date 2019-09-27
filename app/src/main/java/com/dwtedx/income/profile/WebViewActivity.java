package com.dwtedx.income.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.x5webview.X5WebChromeClient;
import com.dwtedx.income.widget.x5webview.X5WebView;
import com.dwtedx.income.widget.x5webview.X5WebViewClient;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.utils.TbsLog;

public class WebViewActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener {

    private AppTitleBar mAppTitleBar;
    private final static String TAG = "WebViewUrl";
    private X5WebView mWebview;
    private ProgressBar mProgressbar;

    private String mUrl;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");

        mAppTitleBar = (AppTitleBar) findViewById(R.id.app_title);
        mAppTitleBar.setTitleText(mTitle);
        mAppTitleBar.setOnTitleClickListener(this);

        // mWebview
        mProgressbar = (ProgressBar) findViewById(R.id.songsong_progressbar);
        mWebview = (X5WebView) findViewById(R.id.gongdoocar_webview);
        initX5WebView();

    }

    private void initX5WebView() {

        mProgressbar.setMax(100);
        mWebview.setWebViewClient(new X5WebViewClient(){
            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView view, String url) {
                // 获取页面内容
                view.loadUrl("javascript:window.dms.showSource(document.getElementsByTagName('img')[0].src);");
                super.onPageFinished(view, url);
            }
        });
        mWebview.setWebChromeClient(new X5WebChromeClient(mProgressbar){
            @Override
            public void onReceivedTitle(WebView webView, String title) {
                super.onReceivedTitle(webView, title);
                if (mTitle == null && title != null) {
                    mAppTitleBar.setTitleText(title);
                }else {
                    mAppTitleBar.setTitleText(mTitle);
                }
            }
        });

        WebSettings webSetting = mWebview.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        //不显示webview缩放按钮
        webSetting.setDisplayZoomControls(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        String userAgent = webSetting.getUserAgentString();
        webSetting.setUserAgentString(userAgent + "AutohigoDMS");//自定义UserAgent

        long time = System.currentTimeMillis();

        mWebview.loadUrl(mUrl);

        TbsLog.d("time-cost", "cost time: " + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }


    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                if(mWebview.canGoBack()){
                    mWebview.goBack();// 返回前一个页面
                }else {
                    finish();
                }
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
