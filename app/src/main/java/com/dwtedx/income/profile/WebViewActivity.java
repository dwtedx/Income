package com.dwtedx.income.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.webclient.AllWebChromeClient;
import com.dwtedx.income.widget.webclient.AllWebViewClient;

public class WebViewActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener {

    private AppTitleBar mAppTitleBar;
    private final static String TAG = "WebViewUrl";
    private WebView mWebview;

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
        final ProgressBar bar = (ProgressBar) findViewById(R.id.songsong_progressbar);
        mWebview = (WebView) findViewById(R.id.gongdoocar_webview);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setSupportZoom(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.setInitialScale(1);
        mWebview.setWebViewClient(new AllWebViewClient());
        mWebview.setWebChromeClient(new AllWebChromeClient(bar));
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        String loadUrl = mUrl;
        Log.w(TAG, loadUrl);
        mWebview.loadUrl(loadUrl);

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
