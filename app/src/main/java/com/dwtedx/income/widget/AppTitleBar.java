/**
 * @Title: CommonTitle.java
 * @Package com.meineke.auto11.base.widget
 * @author heli
 * @date 2014年9月12日 下午3:15:45
 * @version V1.0
 */

package com.dwtedx.income.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dwtedx.income.R;

import java.lang.ref.WeakReference;

/**
 * @author heli
 * @ClassName: CommonTitle
 * @Description: 自定义程序通用title，并实现左右按钮点击事件
 * @date 2014年9月12日 下午3:15:45
 */
public class AppTitleBar extends RelativeLayout implements OnClickListener {
    private TextView mTitleTextView;
    private View mLeftBtn;
    private TextView mLeftBtnContent;
    private View mRightBtn;
    private TextView mRightBtnContent;

    private int mTitleTextSize;
    private int mBtnTextSize;

    private int mLeftBtnImgId;
    private int mRightBtnImgId;

    private String mTitleText;
    private String mLeftBtnText;
    private String mRightBtnText;

    private boolean mLeftBtnVisible;
    private boolean mRightBtnVisible;

    private WeakReference<OnTitleClickListener> mListener;

    public AppTitleBar(Context context) {
        super(context);
    }

    public AppTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        // get attributes from resource
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.app_title);
        mTitleTextSize = a.getInt(R.styleable.app_title_text_size, 16);
        mBtnTextSize = a.getInt(R.styleable.app_title_btn_text_size, 16);

        mLeftBtnImgId = a.getResourceId(R.styleable.app_title_left_btn_image_id, -1);
        mRightBtnImgId = a.getResourceId(R.styleable.app_title_right_btn_image_id, -1);

        mTitleText = a.getString(R.styleable.app_title_text);
        mLeftBtnText = a.getString(R.styleable.app_title_left_btn_text);
        mRightBtnText = a.getString(R.styleable.app_title_right_btn_text);

        mLeftBtnVisible = a.getBoolean(R.styleable.app_title_left_btn_visible, true);
        mRightBtnVisible = a.getBoolean(R.styleable.app_title_right_btn_visible, true);

        LayoutInflater.from(context).inflate(R.layout.app_title_bar, this, true);

        // configure resource for each view
        mTitleTextView = (TextView) this.findViewById(R.id.title_text);
        mTitleTextView.setTextSize(mTitleTextSize);
        mTitleTextView.setText(mTitleText);

        mLeftBtn = this.findViewById(R.id.title_back_btn);
        mLeftBtn.setVisibility(mLeftBtnVisible ? View.VISIBLE : View.INVISIBLE);
        if (mLeftBtnVisible) {
            mLeftBtnContent = (TextView) this.findViewById(R.id.title_back_btn_contents);
//            mLeftBtnContent.setText((null != mLeftBtnText) ? mLeftBtnText : "");
//            mLeftBtnContent.setTextSize(mBtnTextSize);
//            if (mLeftBtnImgId > 0) {
//                mLeftBtnContent.setBackgroundResource(mLeftBtnImgId);
//            }
        }

        mRightBtn = this.findViewById(R.id.title_rightBtn);
        mRightBtn.setVisibility(mRightBtnVisible ? View.VISIBLE : View.INVISIBLE);
        if (mRightBtnVisible) {
            mRightBtnContent = (TextView) this.findViewById(R.id.title_rightBtn_content);
            mRightBtnContent.setText((null != mRightBtnText) ? mRightBtnText : "");
            mRightBtnContent.setTextSize(mBtnTextSize);
            if (mRightBtnImgId > 0) {
                mRightBtnContent.setBackgroundResource(mRightBtnImgId);
            }
        }

        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
        a.recycle();
    }

    @Override
    public void onClick(View arg0) {
        if (null != mListener && null != mListener.get()) {
            if (R.id.title_back_btn == arg0.getId()) {
                mListener.get().onTitleClick(OnTitleClickListener.TITLE_CLICK_LEFT);
            } else if (R.id.title_rightBtn == arg0.getId()) {
                mListener.get().onTitleClick(OnTitleClickListener.TITLE_CLICK_RIGHT);
            }
        }
    }

    public void setOnTitleClickListener(OnTitleClickListener listener) {
        mListener = new WeakReference<OnTitleClickListener>(listener);
    }

    public interface OnTitleClickListener {
        public static final int TITLE_CLICK_LEFT = 0;
        public static final int TITLE_CLICK_RIGHT = 1;

        abstract void onTitleClick(int type);
    }

    public void setTitleText(int stringsId) {
        mTitleTextView.setText(stringsId);
    }

    public void setTitleText(String strTitle) {
        mTitleTextView.setText(strTitle);
    }

//    public void setLeftText(int stringsId) {
//        mLeftBtnContent.setText(stringsId);
//    }

    public void setRightText(int stringsId) {
        mRightBtnContent.setText(stringsId);
    }
    public void setRightimg(int stringsId) {
        mRightBtnContent.setBackgroundResource(stringsId);
    }
    public void setRightVisibility(int visibility) {
        mRightBtnContent.setVisibility(visibility);
    }

}
