package com.dwtedx.income.profile;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.ProgressDialog;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.entity.DiScan;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.entity.DiUserInfo;
import com.dwtedx.income.entity.DiVersion;
import com.dwtedx.income.provider.AppUpdateVersionSharedPreferences;
import com.dwtedx.income.provider.CustomerIDSharedPreferences;
import com.dwtedx.income.provider.FingerprintSetupSharedPreferences;
import com.dwtedx.income.service.IncomeService;
import com.dwtedx.income.service.UserService;
import com.dwtedx.income.sqliteservice.DIAccountService;
import com.dwtedx.income.sqliteservice.DIScanService;
import com.dwtedx.income.sqliteservice.DITypeService;
import com.dwtedx.income.sqliteservice.DlIncomeService;
import com.dwtedx.income.updateapp.UpdateService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.DataCleanManager;
import com.dwtedx.income.utility.FingerprintUiHelper;
import com.dwtedx.income.widget.AppTitleBar;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;
import java.util.Set;

public class SetupActivity extends BaseActivity implements View.OnClickListener, AppTitleBar.OnTitleClickListener {

    private AppTitleBar mAppTitleBar;
    private int[] mClickView = {R.id.profile_username_text, R.id.profile_phone_text, R.id.profile_fingerprint_text, R.id.profile_data_text, R.id.profile_password_text, R.id.setup_update, R.id.message_recommendation, R.id.setup_user_agreement, R.id.about_me, R.id.login_out, R.id.share_app, R.id.clear_cache, R.id.setup_score};
    private TextView mSetupPhoneText;
    private TextView mSetupNameText;
    private TextView mClearCacheText;
    private TextView mSetupVersion;
    private Switch mFingerprintSwitch;

    private ProgressDialog mProgressDialog;
    //数据恢复的记录标记
    private int mIncomeListSize;
    private Handler mHandler;//延迟用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAppTitleBar = (AppTitleBar) findViewById(R.id.app_title);
        mAppTitleBar.setOnTitleClickListener(this);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);isHardwareDetected
        ////设置是否有返回箭头
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //指纹识别开关
        mFingerprintSwitch = (Switch) findViewById(R.id.profile_fingerprint_switch);
        FingerprintSetupSharedPreferences.init(this);
        mFingerprintSwitch.setChecked(FingerprintSetupSharedPreferences.getFingerprintSetup());
        mFingerprintSwitch.setSwitchTextAppearance(this, FingerprintSetupSharedPreferences.getFingerprintSetup()?R.style.s_true:R.style.s_false);
        mFingerprintSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //控制开关字体颜色
                if (b) {
                    mFingerprintSwitch.setSwitchTextAppearance(SetupActivity.this,R.style.s_true);
                }else {
                    mFingerprintSwitch.setSwitchTextAppearance(SetupActivity.this,R.style.s_false);
                }
            }
        });

        for (int id : mClickView) {
            findViewById(id).setOnClickListener(this);
        }

        if(!isLogin()){
            findViewById(R.id.login_out).setVisibility(View.GONE);
        }

        mSetupPhoneText = (TextView) findViewById(R.id.setup_phone_textview);
        mSetupNameText = (TextView) findViewById(R.id.setup_username_textview);
        mSetupVersion = (TextView) findViewById(R.id.setup_version);
        mSetupVersion.setText(UpdateService.getAPKVersion(this));
        mClearCacheText = (TextView) findViewById(R.id.profile_clear_cache_text);
        try {
            String cachSize = DataCleanManager.getTotalCacheSize(this);
            mClearCacheText.setText(cachSize);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mHandler = new Handler();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(null != ApplicationData.mDiUserInfo) {
            //赋值
            mSetupPhoneText.setText(ApplicationData.mDiUserInfo.getPhone());
            mSetupNameText.setText(ApplicationData.mDiUserInfo.getUsername());
        }else {
            mSetupPhoneText.setText(null);
            mSetupNameText.setText(null);
        }
    }

    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                break;
        }
    }

    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
    //    switch (item.getItemId()) {
    //        //重写ToolBar返回按钮的行为，防止重新打开父Activity重走生命周期方法
    //        case android.R.id.home:
    //            finish();
    //            return true;
    //    }
    //    return super.onOptionsItemSelected(item);
    //}

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.profile_phone_text:
                setPhone();
                break;
            case R.id.profile_username_text:
                setUserName();
                break;
            case R.id.profile_fingerprint_text:
                setUpFingerprint();
                break;
            case R.id.profile_data_text:
                dataRegain();
                break;
            case R.id.profile_password_text:
                setPassWord();
                break;
            case R.id.setup_update:
                getVersions();
                break;
            case R.id.message_recommendation:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", "http://dwtedx.com/message_1.html");
                intent.putExtra("title", "DD博客留言板");
                startActivity(intent);
                break;
            case R.id.setup_user_agreement:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", "http://income.dwtedx.com");
                intent.putExtra("title", "DD记账");
                startActivity(intent);
                break;
            case R.id.about_me:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", "http://dwtedx.com/about.html");
                intent.putExtra("title", "关于我");
                startActivity(intent);
                break;
            case R.id.login_out:
                ApplicationData.mDiUserInfo = null;
                CustomerIDSharedPreferences.init(this);
                CustomerIDSharedPreferences.clear();
                finish();
                break;
            case R.id.share_app:
                openShare();
                break;
            case R.id.clear_cache:
                //清除缓存
                clearCache();
                break;
            case R.id.setup_score:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    private void setUpFingerprint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = getSystemService(FingerprintManager.class);
            if(null == fingerprintManager){
                Toast.makeText(this, R.string.fingerprint_description_null_tip, Toast.LENGTH_SHORT).show();
                return;
            }
            FingerprintUiHelper fingerprintUiHelper = new FingerprintUiHelper(fingerprintManager,null, null, null);

            if(!fingerprintUiHelper.isFingerprintAuthAvailable()){
                Toast.makeText(this, R.string.fingerprint_not_support_one, Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isLogin()){
                Toast.makeText(this, R.string.fingerprint_not_support_login, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginV2Activity.class);
                startActivity(intent);
                return;
            }
            FingerprintSetupSharedPreferences.setFingerprintSetup(!FingerprintSetupSharedPreferences.getFingerprintSetup());
            mFingerprintSwitch.setChecked(FingerprintSetupSharedPreferences.getFingerprintSetup());
        }else {
            Toast.makeText(this, R.string.fingerprint_not_support, Toast.LENGTH_SHORT).show();
        }
    }

    private void getVersions() {
        SaDataProccessHandler<Void, Void, DiVersion> dataVerHandler = new SaDataProccessHandler<Void, Void, DiVersion>(SetupActivity.this) {
            @Override
            public void onSuccess(final DiVersion data) {
                if (data.isUpdate()) {
                    new MaterialDialog.Builder(SetupActivity.this)
                            .title(getString(R.string.tip_update) + data.getVersion())
                            .content(data.getContent())
                            .positiveText(R.string.ok)
                            .negativeText(R.string.cancel)
                            .onAny(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    //NEGATIVE   POSITIVE
                                    if (which.name().equals("POSITIVE")) {
                                        //调用拨号面板：
                                        Intent intent = new Intent(SetupActivity.this, UpdateService.class);
                                        intent.putExtra("Key_App_Name", "Income");
                                        intent.putExtra("Key_Down_Url", data.getApkurl());
                                        SetupActivity.this.startService(intent);
                                    }
                                }
                            })
                            .show();
                } else {
                    Snackbar.make(findViewById(R.id.app_title), "暂时没有新版本呢", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

        };
        UserService.getInstance().versionUpdate(dataVerHandler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    // 用来配置各个平台的SDKF
    private void openShare() {
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.ALIPAY, SHARE_MEDIA.EMAIL, SHARE_MEDIA.SMS};

        String shareContent = "我在使用DD记账、记录生活中的每一笔开支、还有很丰富的报表、感觉真的很不错、特此推荐给您 http://income.dwtedx.com/";
        UMWeb web = new UMWeb("http://income.dwtedx.com");
        web.setTitle("DD记账、记录生活点点滴滴");//标题
        web.setThumb(new UMImage(SetupActivity.this, R.mipmap.ic_launcher));//缩略图
        web.setDescription(shareContent);//描述

        new ShareAction(SetupActivity.this)
                .setDisplayList(displaylist)
                .withText(shareContent)
                .withMedia(web)
                .setCallback(umShareListener)
                .open();

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Toast.makeText(SetupActivity.this, share_media.toString() + "正在分享...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(SetupActivity.this, share_media.toString() + "分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(SetupActivity.this, share_media.toString() + "分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(SetupActivity.this, share_media.toString() + "分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 清除缓存
     */
    private void clearCache() {

        new MaterialDialog.Builder(this)
                .title(R.string.tip)
                .content(R.string.clear_cache_tip)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //NEGATIVE   POSITIVE
                        if (which.name().equals("POSITIVE")) {
                            DataCleanManager.clearAllCache(SetupActivity.this);
                            mClearCacheText.setText(getString(R.string.clear_cache_text));
                            //SharedPreferences
                            AppUpdateVersionSharedPreferences.init(SetupActivity.this);
                            AppUpdateVersionSharedPreferences.clear();
                            CustomerIDSharedPreferences.init(SetupActivity.this);
                            AppUpdateVersionSharedPreferences.clear();
                            Snackbar.make(findViewById(R.id.app_title), R.string.clear_success, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                })
                .show();
    }

    private void setUserName() {
        if (!isLogin()) {
            startActivity(new Intent(SetupActivity.this, LoginV2Activity.class));
            return;
        }
        if(!CommonUtility.isEmpty(mSetupNameText.getText().toString())){
            Snackbar.make(findViewById(R.id.app_title), R.string.user_name_tip, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        new MaterialDialog.Builder(this)
                .title(R.string.user_name)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(4, 10)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .input("用户名(只能设置一次)", mSetupNameText.getText().toString(), false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, final CharSequence input) {
                        if(!CommonUtility.isNumericOrABC(input.toString())){
                            Snackbar.make(findViewById(R.id.app_title), R.string.user_name_tip1, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            return;
                        }
                        SaDataProccessHandler<Void, Void, DiUserInfo> dataVerHandler = new SaDataProccessHandler<Void, Void, DiUserInfo>(SetupActivity.this) {
                            @Override
                            public void onSuccess(DiUserInfo data) {
                                mSetupNameText.setText(input.toString());
                                ApplicationData.mDiUserInfo = data;
                                Snackbar.make(findViewById(R.id.app_title), R.string.user_name_set_suss, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                        };
                        UserService.getInstance().updateUserName(input.toString(), dataVerHandler);
                    }
                }).show();
    }

    private void setPhone() {
        if (!isLogin()) {
            startActivity(new Intent(SetupActivity.this, LoginV2Activity.class));
            return;
        }
        Intent intent = new Intent(this, MobileActivity.class);
        startActivity(intent);
    }

    private void setPassWord() {
        if (!isLogin()) {
            startActivity(new Intent(this, LoginV2Activity.class));
            return;
        }
        if(CommonUtility.isEmpty(ApplicationData.mDiUserInfo.getUsername()) && CommonUtility.isEmpty(ApplicationData.mDiUserInfo.getPhone())){
            Snackbar.make(findViewById(R.id.app_title), R.string.user_name_set_tip, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        Intent intent = new Intent(this, PassWordActivity.class);
        startActivity(intent);
    }

    //数据恢复////////////////数据恢复///////////////////数据恢复/////////////////////数据恢复//////////////////数据恢复/////////

    private void dataRegain() {
        if (!isLogin()) {
            startActivity(new Intent(SetupActivity.this, LoginV2Activity.class));
            return;
        }
        new MaterialDialog.Builder(this)
                .title(R.string.tip)
                .content(R.string.profile_data_tip)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //NEGATIVE   POSITIVE
                        if (which.name().equals("POSITIVE")) {
                            showProgressDialog();
                            DlIncomeService.getInstance(SetupActivity.this).deleteAll();
                            DIScanService.getInstance(SetupActivity.this).deleteAll();
                            getIncomeCynchronizeSingleList();
                        }
                    }
                })
                .show();
    }

    public void showProgressDialog() {
        mProgressDialog = getProgressDialog();
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void cancelProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.setCancelable(true);
            mProgressDialog.cancel();
        }
    }

    /**
     * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/8/19 上午10:24.
     * Company 路之遥网络科技有限公司
     * Description 恢复记账数据
     */
    private void getIncomeCynchronizeSingleList() {
        SaDataProccessHandler<Void, Void, List<DiIncome>> dataVerHandler = new
                SaDataProccessHandler<Void, Void, List<DiIncome>>(SetupActivity.this) {
                    @Override
                    public void onSuccess(List<DiIncome> data) {
                        if (data.size() > 0) {
                            mIncomeListSize = mIncomeListSize + data.size();
                            for (DiIncome income : data) {
                                income.setIsupdate(CommonConstants.INCOME_RECORD_UPDATEED);
                                DlIncomeService.getInstance(SetupActivity.this).saveSynchronizeEgain(income);
                                if(income.getRecordtype() == CommonConstants.INCOME_RECORD_TYPE_1
                                        && null != income.getScanList()
                                        && income.getScanList().size() > 0) {
                                    for (DiScan scan : income.getScanList()) {
                                        scan.setIsupdate(CommonConstants.INCOME_RECORD_UPDATEED);
                                        scan.setIncomeid(DlIncomeService.getInstance(SetupActivity.this).getLastId());
                                        DIScanService.getInstance(SetupActivity.this).saveSynchronizeEgain(scan);
                                    }
                                }
                            }
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getIncomeCynchronizeSingleList();
                                }
                            }, 500);
                        } else {
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getTypeCynchronizeSingleList();
                                }
                            }, 500);
                        }
                    }

                    @Override
                    public void onPreExecute() { }

                    @Override
                    public void handlerError(SaException e) {
                        super.handlerError(e);
                        cancelProgressDialog();
                    }
                };
        IncomeService.getInstance().incomeCynchronizeSingleList(mIncomeListSize, CommonConstants.PAGE_LENGTH_NUMBER, dataVerHandler);
    }

    /**
     * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/8/19 上午10:25.
     * Company 路之遥网络科技有限公司
     * Description恢复类型
     */
    private void getTypeCynchronizeSingleList() {
        SaDataProccessHandler<Void, Void, List<DiType>> dataVerHandler = new
                SaDataProccessHandler<Void, Void, List<DiType>>(SetupActivity.this) {
                    @Override
                    public void onSuccess(List<DiType> data) {
                        for (DiType diType : data) {
                            DITypeService.getInstance(SetupActivity.this).saveSynchronizeEgain(diType);
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getAccountCynchronizeSingleList();
                            }
                        }, 500);
                    }

                    @Override
                    public void onPreExecute() { }

                    @Override
                    public void handlerError(SaException e) {
                        super.handlerError(e);
                        cancelProgressDialog();
                    }
                };
        IncomeService.getInstance().typeCynchronizeSingleList(dataVerHandler);
    }

    /**
     * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/8/19 上午10:25.
     * Company 路之遥网络科技有限公司
     * Description 恢复帐户
     */
    private void getAccountCynchronizeSingleList() {
        SaDataProccessHandler<Void, Void, List<DiAccount>> dataVerHandler = new
                SaDataProccessHandler<Void, Void, List<DiAccount>>(SetupActivity.this) {
                    @Override
                    public void onSuccess(List<DiAccount> data) {
                        for (DiAccount account : data) {
                            DIAccountService.getInstance(SetupActivity.this).saveSynchronizeEgain(account);
                        }
                        cancelProgressDialog();
                        Snackbar.make(findViewById(R.id.app_title), R.string.profile_data_success, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }

                    @Override
                    public void onPreExecute() { }

                    @Override
                    public void handlerError(SaException e) {
                        super.handlerError(e);
                        cancelProgressDialog();
                    }

                };
        IncomeService.getInstance().accountCynchronizeSingleList(dataVerHandler);
    }

    //数据恢复结束////////////////数据恢复结束///////////////////数据恢复结束/////////////////////数据恢复结束//////////////////数据恢复结束/////////

}
