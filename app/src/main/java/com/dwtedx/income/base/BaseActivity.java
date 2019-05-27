package com.dwtedx.income.base;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.Theme;
import com.dwtedx.income.R;
import com.dwtedx.income.broadcast.CommonBroadcast;
import com.dwtedx.income.connect.ErrorDilaog;
import com.dwtedx.income.connect.ProgressDialog;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.umeng.analytics.MobclickAgent;


/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 上午9:01.
 * @Company 路之遥网络科技有限公司
 * @Description 应用程序中Activity的基类，用于定义Activity共有方法
 */
public abstract class BaseActivity extends AppCompatActivity {
    private final static String TAG = "BaseActivity";

    // 写一个广播的内部类，
    private BroadcastReceiver mProadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceive(intent.getIntExtra(CommonBroadcast.BROADCAST_ACTION_TYPE, -1), intent);
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 在当前的activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(CommonBroadcast.BROADCAST_ACTION);
        registerReceiver(mProadcastReceiver, filter);//注册
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏

        //状态栏颜色 不支持4.4
        //getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mProadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void onBroadcastReceive(int type, Intent intent) {

    }

    public String getStringFromResources(int id) {
        String resourcesStr = this.getResources().getString(id);
        if (resourcesStr != null) {
            return resourcesStr;
        } else {
            Log.d("error", "getString from    Resources error!!!");
            return "";
        }
    }

    public ProgressDialog getProgressDialog() {
        //mProgressDialog = new ProgressDialog(this);
        //mProgressDialog.setMessage(getStringFromResources(R.string.waiting));
        //mProgressDialog.setIndeterminate(true);
        //mProgressDialog.setCancelable(true);
        //mProgressDialog.setTitle(R.string.tip);
        //mProgressDialog = new CustomProgressDialog(this, getResources().getString(R.string.waiting));
        //if (null == mProgressDialog) {
        //mProgressDialog = new ProgressDialog(this, null);
        //}
        return new ProgressDialog(this, null);
    }

    public boolean handlerError(SaException e) {
        //Log.w(TAG, e.getMessage());
        Log.w(TAG, e);
        ErrorDilaog.showErrorDiloag(this, e);
        return true;
    }

    public boolean isLogin(){
        if(null != ApplicationData.mDiUserInfo && ApplicationData.mDiUserInfo.getId() > 0){
            return true;
        }
        return false;
    }

}
