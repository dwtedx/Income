package com.dwtedx.income.widget.webclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class CustomWebView extends WebView {
    public ScrollInterface mScrollInterface;
 
    public CustomWebView(Context context) {
        super(context);
    }
 
    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
 
        super.onScrollChanged(l, t, oldl, oldt);
 
        mScrollInterface.onSChanged(l, t, oldl, oldt);
 
    }
 
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        boolean result =  super.onTouchEvent(event);
        mScrollInterface.onSChanged(0, getScrollY(), 0, 0);
        return result;
    }

    public void setOnCustomScroolChangeListener(ScrollInterface scrollInterface) {
 
        this.mScrollInterface = scrollInterface;
 
    }
 
    public interface ScrollInterface {
 
        public void onSChanged(int l, int t, int oldl, int oldt);
 
    }
 
}