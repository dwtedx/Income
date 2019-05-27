package com.dwtedx.income.home;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.dwtedx.income.R;
import com.dwtedx.income.addrecord.AddRecordActivity;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.provider.HomeScanTipSharedPreferences;

/**
 * PopMenuView
 *
 * @author PengZhenjin
 * @date 2017-9-11
 */
public class PopScanTipView {

    private static final String TAG = "PopMenuView";

    public static PopScanTipView getInstance() {
        return PopupMenuViewHolder.INSTANCE;
    }

    private static class PopupMenuViewHolder {
        public static PopScanTipView INSTANCE = new PopScanTipView();
    }

    private View mRootVew;
    private PopupWindow mPopupWindow;

    private Button mHomeTipButton;
    private RelativeLayout mHomeTipLayout;

    /**
     * 动画执行的 属性值数组
     */
    private float mAnimatorProperty[] = null;

    /**
     * 第一排图 距离屏幕底部的距离
     */
    private int mTop = 0;

    /**
     * 第二排图 距离屏幕底部的距离
     */
    private int mBottom = 0;

    /**
     * 创建PopupWindow
     *
     * @param context
     */
    private void createView(final Context context) {
        this.mRootVew = LayoutInflater.from(context).inflate(R.layout.view_pop_scan_tip, null);
        this.mPopupWindow = new PopupWindow(this.mRootVew, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        this.mPopupWindow.setFocusable(false); // 设置为失去焦点 方便监听返回键的监听
        //mPopupWindow.setClippingEnabled(false); // 如果想要popupWindow 遮挡住状态栏可以加上这句代码
        this.mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        this.mPopupWindow.setOutsideTouchable(false);

        if (this.mAnimatorProperty == null) {
            this.mTop = dip2px(context, 310);
            this.mBottom = dip2px(context, 210);
            this.mAnimatorProperty = new float[]{this.mBottom, 60, -30, -20 - 10, 0};
        }

        this.initLayout(context);
    }

    /**
     * dp转化为px
     *
     * @param context  context
     * @param dipValue dp value
     * @return 转换之后的px值
     */
    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 初始化 view
     */
    private void initLayout(final Context context) {
        this.mHomeTipLayout = (RelativeLayout) this.mRootVew.findViewById(R.id.home_tip_layout);
        this.mHomeTipButton = (Button) this.mRootVew.findViewById(R.id.home_tip_button);
        this.mHomeTipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是否有提示
                HomeScanTipSharedPreferences.init(context);
                HomeScanTipSharedPreferences.setIsTip(ApplicationData.mAppVersionCode);
                ((HomeV3Activity) context).showScan();
                close();
            }
        });
    }

    /**
     * 显示PopupWindow
     *
     * @param context context
     * @param parent  parent
     */
    public void show(Context context, View parent) {
        createView(context);
        if (this.mPopupWindow != null && !this.mPopupWindow.isShowing()) {
            this.mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    /**
     * 关闭popupWindow
     */
    public void close() {
        if (this.mPopupWindow != null && this.mPopupWindow.isShowing()) {
            this.mPopupWindow.dismiss();
            this.mPopupWindow = null;
        }
    }

    /**
     * PopupWindow是否显示了
     *
     * @return
     */
    public boolean isShowing() {
        return this.mPopupWindow != null && this.mPopupWindow.isShowing();
    }

    /**
     * 启动PopupWindow动画
     *
     * @param view     view
     * @param duration 执行时长
     * @param distance 执行的轨迹数组
     */
    private void startAnimation(View view, int duration, float[] distance) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", distance);
        anim.setDuration(duration);
        anim.start();
    }

    /**
     * 关闭PopupWindow动画
     *
     * @param view     view
     * @param duration 动画执行时长
     * @param next     平移量
     */
    private void closeAnimation(View view, int duration, int next) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", 0f, next);
        anim.setDuration(duration);
        anim.start();
    }
}
