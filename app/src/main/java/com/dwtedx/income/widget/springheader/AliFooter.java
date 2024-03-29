package com.dwtedx.income.widget.springheader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.liaoinstan.springview.container.BaseSimpleFooter;
import com.liaoinstan.springview.widget.SpringView;

import androidx.core.content.ContextCompat;

/**
 * Created by Administrator on 2016/3/21.
 */
public class AliFooter extends BaseSimpleFooter {
    private Context context;
    private int rotationSrc;
    private int arrowSrc;
    private int logoSrc;
    private boolean isShowText;

    private final int ROTATE_ANIM_DURATION = 180;
    private RotateAnimation mRotateUpAnim;
    private RotateAnimation mRotateDownAnim;

    private TextView footerTitle;
    private ImageView footerArrow;
    private ImageView footerLogo;
    private ProgressBar footerProgressbar;
    private View frame;

    public AliFooter(Context context) {
        this(context, 0, R.drawable.arrow, 0, false);
    }

    public AliFooter(Context context, boolean isShowText) {
        this(context, 0, R.drawable.arrow, 0, isShowText);
    }

    public AliFooter(Context context, int logoSrc) {
        this(context, 0, R.drawable.arrow, logoSrc, false);
    }

    public AliFooter(Context context, int logoSrc, boolean isShowText) {
        this(context, 0, R.drawable.arrow, logoSrc, isShowText);
    }

    public AliFooter(Context context, int rotationSrc, int arrowSrc, int logoSrc, boolean isShowText) {
        setType(SpringView.Type.FOLLOW);
        setMovePara(2.0f);
        this.context = context;
        this.rotationSrc = rotationSrc;
        this.arrowSrc = arrowSrc;
        this.logoSrc = logoSrc;
        this.isShowText = isShowText;
        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.ali_footer, viewGroup, false);
        footerTitle = view.findViewById(R.id.ali_footer_text);
        footerArrow = view.findViewById(R.id.ali_footer_arrow);
        footerLogo = view.findViewById(R.id.ali_footer_logo);
        footerProgressbar = view.findViewById(R.id.ali_footer_progressbar);
        frame = view.findViewById(R.id.ali_frame);
        if (logoSrc != 0) footerLogo.setImageResource(logoSrc);
        if (!isShowText) footerTitle.setVisibility(View.GONE);
        if (rotationSrc != 0)
            footerProgressbar.setIndeterminateDrawable(ContextCompat.getDrawable(context, rotationSrc));
        footerArrow.setImageResource(arrowSrc);
        return view;
    }

    @Override
    public int getDragSpringHeight(View rootView) {
        return frame.getMeasuredHeight();
    }

    @Override
    public int getDragLimitHeight(View rootView) {
        return frame.getMeasuredHeight();
    }

    @Override
    public void onPreDrag(View rootView) {
    }

    @Override
    public void onDropAnim(View rootView, int dy) {
    }

    @Override
    public void onLimitDes(View rootView, boolean upORdown) {
        if (upORdown) {
            footerTitle.setText("松开加载");
            if (footerArrow.getVisibility() == View.VISIBLE)
                footerArrow.startAnimation(mRotateUpAnim);
        } else {
            footerTitle.setText("上拉加载");
            if (footerArrow.getVisibility() == View.VISIBLE)
                footerArrow.startAnimation(mRotateDownAnim);
        }
    }

    @Override
    public void onStartAnim() {
        footerTitle.setText("正在加载");
        footerArrow.setVisibility(View.INVISIBLE);
        footerArrow.clearAnimation();
        footerProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishAnim() {
        footerTitle.setText("上拉加载");
        footerArrow.setVisibility(View.VISIBLE);
        footerProgressbar.setVisibility(View.INVISIBLE);
    }
}