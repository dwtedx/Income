package com.dwtedx.income.home;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.dwtedx.income.R;
import com.dwtedx.income.addrecord.AddRecordActivity;

/**
 * PopMenuView
 *
 * @author PengZhenjin
 * @date 2017-9-11
 */
public class PopMenuView {

    private static final String TAG = "PopMenuView";

    public static PopMenuView getInstance() {
        return PopupMenuViewHolder.INSTANCE;
    }

    private static class PopupMenuViewHolder {
        public static PopMenuView INSTANCE = new PopMenuView();
    }

    private View mRootVew;
    private PopupWindow mPopupWindow;

    private RelativeLayout mCloseLayout;
    private ImageView mCloseIv;
    private LinearLayout mRecordIconLayout, mScanTicketIconLayout;

    private Context mContext;

    /**
     * 创建PopupWindow
     *
     * @param context
     */
    private void createView(final Context context) {
        this.mContext = context;
        this.mRootVew = LayoutInflater.from(context).inflate(R.layout.view_pop_menu, null);
        this.mPopupWindow = new PopupWindow(this.mRootVew, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        this.mPopupWindow.setFocusable(false); // 设置为失去焦点 方便监听返回键的监听
        mPopupWindow.setClippingEnabled(false); // 如果想要popupWindow 遮挡住状态栏可以加上这句代码
        this.mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        this.mPopupWindow.setOutsideTouchable(false);

        this.initLayout(context);
    }

    /**
     * dp转化为px
     *
     * @param dipValue dp value
     * @return 转换之后的px值
     */
    private int dip2px( float dipValue) {
        final float scale = this.mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 初始化 view
     */
    private void initLayout(Context context) {
        this.mCloseLayout = (RelativeLayout) this.mRootVew.findViewById(R.id.close_layout);
        this.mCloseIv = (ImageView) this.mRootVew.findViewById(R.id.close_iv);
        this.mRecordIconLayout = (LinearLayout) this.mRootVew.findViewById(R.id.m_record_icon_layout);
        this.mScanTicketIconLayout = (LinearLayout) this.mRootVew.findViewById(R.id.m_scan_ticket_icon_layout);

        this.mCloseLayout.setOnClickListener(new ItemClick(0, context));

        this.mRecordIconLayout.setOnClickListener(new ItemClick(1, context));
        this.mScanTicketIconLayout.setOnClickListener(new ItemClick(2, context));
    }

    /**
     * Item点击事件
     */
    private class ItemClick implements View.OnClickListener {

        private int index;
        private Context context;

        public ItemClick(int index, Context context) {
            this.index = index;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (index == 0) {   // 关闭按钮
                closePopupWindowAction();
            } else if (index == 1) {
                context.startActivity(new Intent(context, AddRecordActivity.class));
                close();
            } else if (index == 2) {
                ((HomeV3Activity) context).getScanParaAndShowScan();
                close();
            }
        }
    }

    /**
     * 打开popupWindow执行的动画
     */
    private void openPopupWindowAction() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mCloseIv, "rotation", 0f, 135f);
        objectAnimator.setDuration(200);
        objectAnimator.start();

        startAnimation();

    }

    /**
     * 关闭popupWindow执行的动画
     */
    public void closePopupWindowAction() {
        if (this.mCloseIv != null && this.mCloseLayout != null) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this.mCloseIv, "rotation", 135f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.start();

            closeAnimation();

            this.mCloseLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    close();
                }
            }, 300);
        }
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
            openPopupWindowAction();
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
     */
    private void startAnimation() {
        //设置动画,只能在自己的父布局中平移
        //ivTupian自己的相对坐标系中运动（自己的起点就是0,0坐标）
        //0f 开始的坐标，tvJuliX表示移动的距离，整体表示的意思是ivTupian从0开始在X坐标上移动tvJuliX距离。
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(this.mRecordIconLayout, "translationX", -dip2px(60), -10, 10, 5, 2, 0);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(this.mRecordIconLayout, "translationY", dip2px(102), 10, -10, -5, -2, 0);
        ObjectAnimator objectScaleX = ObjectAnimator.ofFloat(this.mRecordIconLayout, "scaleX", 0f, 0.5f, 1f, 1f, 1f, 1f);
        ObjectAnimator objectScaleY = ObjectAnimator.ofFloat(this.mRecordIconLayout, "scaleY", 0f, 0.5f, 1f, 1f, 1f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        //同时播放
        animatorSet.playTogether(objectAnimatorX, objectAnimatorY, objectScaleX, objectScaleY);
        //animatorSet.play(objectAnimatorX).with(objectAnimatorY);
        //动画用时100ms
        animatorSet.setDuration(500);
        //开始动画
        animatorSet.start();

        //扫单的动画
        ObjectAnimator objectAnimatorXScan = ObjectAnimator.ofFloat(this.mScanTicketIconLayout, "translationX", dip2px(60), 10, -10, -5, -2, 0);
        ObjectAnimator objectAnimatorYScan = ObjectAnimator.ofFloat(this.mScanTicketIconLayout, "translationY", dip2px(102), 10, -10, -5, -2, 0);
        ObjectAnimator objectScaleXScan = ObjectAnimator.ofFloat(this.mScanTicketIconLayout, "scaleX", 0f, 0.5f, 1f, 1f, 1f, 1f);
        ObjectAnimator objectScaleYScan = ObjectAnimator.ofFloat(this.mScanTicketIconLayout, "scaleY", 0f, 0.5f, 1f, 1f, 1f, 1f);
        AnimatorSet animatorSetScan = new AnimatorSet();
        animatorSetScan.playTogether(objectAnimatorXScan, objectAnimatorYScan, objectScaleXScan, objectScaleYScan);
        animatorSetScan.setDuration(430);
        animatorSetScan.start();

    }

    /**
     * 关闭PopupWindow动画
     *
     */
    private void closeAnimation() {
        //设置动画,只能在自己的父布局中平移
        //ivTupian自己的相对坐标系中运动（自己的起点就是0,0坐标）
        //0f 开始的坐标，tvJuliX表示移动的距离，整体表示的意思是ivTupian从0开始在X坐标上移动tvJuliX距离。
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(this.mRecordIconLayout, "translationX", 0, -dip2px(60));
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(this.mRecordIconLayout, "translationY", 0, dip2px(102));
        ObjectAnimator objectScaleX = ObjectAnimator.ofFloat(this.mRecordIconLayout, "scaleX", 1f, 0f);
        ObjectAnimator objectScaleY = ObjectAnimator.ofFloat(this.mRecordIconLayout, "scaleY", 1f, 0f);
        AnimatorSet animatorSet = new AnimatorSet();
        //同时播放
        animatorSet.playTogether(objectAnimatorX, objectAnimatorY, objectScaleX, objectScaleY);
        //animatorSet.play(objectAnimatorX).with(objectAnimatorY);
        //动画用时100ms
        animatorSet.setDuration(280);
        //开始动画
        animatorSet.start();

        //扫单的动画
        ObjectAnimator objectAnimatorXScan = ObjectAnimator.ofFloat(this.mScanTicketIconLayout, "translationX", 0, dip2px(60));
        ObjectAnimator objectAnimatorYScan = ObjectAnimator.ofFloat(this.mScanTicketIconLayout, "translationY", 0,  dip2px(102));
        ObjectAnimator objectScaleXScan = ObjectAnimator.ofFloat(this.mScanTicketIconLayout, "scaleX", 1f, 0f);
        ObjectAnimator objectScaleYScan = ObjectAnimator.ofFloat(this.mScanTicketIconLayout, "scaleY", 1f, 0f);
        AnimatorSet animatorSetScan = new AnimatorSet();
        animatorSetScan.playTogether(objectAnimatorXScan, objectAnimatorYScan, objectScaleXScan, objectScaleYScan);
        animatorSetScan.setDuration(280);
        animatorSetScan.start();
    }
}
