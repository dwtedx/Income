package com.dwtedx.income.base;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;
import com.dwtedx.income.connect.ErrorDilaog;
import com.dwtedx.income.connect.ProgressDialog;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.topic.TopicDetailActivity;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;


/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 上午9:01.
 * @Company 路之遥网络科技有限公司
 * @Description 应用程序中Activity的基类，用于定义Activity共有方法
 */
public abstract class BaseActivity extends AppCompatActivity {
    private final static String TAG = "BaseActivity";

    //private final static String mNotTopicActivitys = "|topic.TopicDetailActivity|MainActivity|MainFingerprintActivity|MainShortcutActivity|";
    private final static String mNotTopicActivitys = "|MainActivity|MainFingerprintActivity|MainShortcutActivity|";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //状态栏颜色 不支持4.4
        //getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        }
        onUMengPageCollectionMode();
        //推送的统计
        PushAgent.getInstance(this).onAppStart();
    }

    public void onUMengPageCollectionMode() {
        //禁止默认的页面统计方式，这样将不会再自动统计Activity(默认页面统计只能统计Activity，不能统计Activity下面包含的Fragment)
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计
        //MobclickAgent.onResume(this);
        //口令检测   使用剪切板在API11以后的版本
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ClipboardManager manager = (ClipboardManager) ApplicationData.mIncomeApplication.getSystemService(Context.CLIPBOARD_SERVICE);
                    String calssName = getLocalClassName();
                    if (manager != null && !mNotTopicActivitys.contains(calssName)) {
                        if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                            CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                            String addedTextString = String.valueOf(addedText);
                            if (!CommonUtility.isEmpty(addedTextString) && addedTextString.contains("打开DD记账")) {
                                //名字读取
                                int nameLastIndexOf = addedTextString.lastIndexOf("】");
                                String rmmoveNameLastIndex = addedTextString.substring(0, nameLastIndexOf);
                                String content = rmmoveNameLastIndex.substring(rmmoveNameLastIndex.lastIndexOf("【") + 1).trim();
                                content = String.format(getResources().getString(R.string.topic_context_tip), content);
                                //id读取
                                int lastIndexOf = addedTextString.lastIndexOf("$");
                                String rmmoveLastIndex = addedTextString.substring(0, lastIndexOf);
                                String topicId = rmmoveLastIndex.substring(rmmoveLastIndex.lastIndexOf("$") + 1).trim();
                                Log.i(getLocalClassName(), "onResume topicId =======================================" + topicId);
                                byte[] decodeByte = Base64.decode(topicId.getBytes(), Base64.DEFAULT);
                                String decode = new String(decodeByte);
                                Log.i(getLocalClassName(), "解密 onResume topicId =======================================" + decode);
                                if (CommonUtility.isNumeric(decode)) {
                                    clearClipboard();
                                    //提示
                                    new MaterialDialog.Builder(BaseActivity.this)
                                            .title(R.string.topic_tip)
                                            .content(content)
                                            .positiveText(R.string.ok)
                                            .negativeText(R.string.cancel)
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    Intent intent = new Intent(BaseActivity.this, TopicDetailActivity.class);
                                                    intent.putExtra("topicId", Integer.parseInt(decode));
                                                    startActivity(intent);
                                                }
                                            })
                                            .show();
                                }
                            }
                        }
                    }
                }
            }, 500);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.toastShow(R.string.topic_errot_tip, ToastUtil.ICON.WARNING);
        }
    }

    /**
     * 清空剪贴板内容
     */
    public static void clearClipboard() {
        ClipboardManager manager = (ClipboardManager) ApplicationData.mIncomeApplication.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            manager.setPrimaryClip(ClipData.newPlainText(null, null));//参数一：标签，可为空，参数二：要复制到剪贴板的文本
            if (manager.hasPrimaryClip()) {
                manager.getPrimaryClip().getItemAt(0).getText();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
        //MobclickAgent.onPause(this);
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

    public boolean isLogin() {
        if (null != ApplicationData.mDiUserInfo && ApplicationData.mDiUserInfo.getId() > 0) {
            return true;
        }
        return false;
    }

    public boolean isVIP() {
        if (isLogin()) {
            if (ApplicationData.mDiUserInfo.getVipflag() == CommonConstants.VIP_TYPE_VIP) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @return 是否在前台显示
     */
    public boolean isForeground() {
        if (this == null || this.isDestroyed() || this.isFinishing()) {
            return false;
        }
        return true;
    }
}
