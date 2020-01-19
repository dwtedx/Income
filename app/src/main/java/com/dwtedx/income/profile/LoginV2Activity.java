package com.dwtedx.income.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.ProgressDialog;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiUserInfo;
import com.dwtedx.income.entity.UMengInfo;
import com.dwtedx.income.provider.CustomerIDSharedPreferences;
import com.dwtedx.income.provider.LoginSharedPreferences;
import com.dwtedx.income.service.UserService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.ParseJsonToObject;
import com.dwtedx.income.widget.AppTitleBar;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by 秦友龙 on 2017/2/15.
 * 登录界面
 */
public class LoginV2Activity extends BaseActivity implements AppTitleBar.OnTitleClickListener, View.OnClickListener {

    private AppTitleBar mAppTitleBar;
    private TextView mRegistered, mForgetPassword;
    private Button mProfileLogin;
    private EditText mEtName, mEtPassWord;
    private ImageView mLoginQQ, mLoginWechat, mLoginSina;

    private UMShareAPI mShareAPI;
    private UMengInfo mUMengInfo;

    private LoginSharedPreferences mLoginSharedPreferences;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_v2);

        mProgressDialog = getProgressDialog();

        initView();
        getUserInfo();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isLogin()){
            finish();
        }
    }

    private void getUserInfo() {
        //已授权的平台，可以获取用户信息（新浪微博可以获取用户好友列表） 实现的方法与授权和解除授权类似：
        mShareAPI = UMShareAPI.get(this);
        mLoginSharedPreferences.init(this);
        String userinfo = mLoginSharedPreferences.getUserInfoStr();
        if(CommonUtility.isEmpty(userinfo)){
            return;
        }
        String[] userinfoArr = userinfo.split("\\|");
        if (userinfoArr[0] != null && userinfoArr[1] != null) {
            mEtName.setText(userinfoArr[0]);
            mEtPassWord.setText(userinfoArr[1]);
        }
    }

    private void initView() {
        mAppTitleBar = (AppTitleBar) findViewById(R.id.app_title);
        mAppTitleBar.setOnTitleClickListener(this);
        mRegistered = (TextView) findViewById(R.id.tv_registered);
        mRegistered.setOnClickListener(this);
        mForgetPassword = (TextView) findViewById(R.id.forget_password);
        mForgetPassword.setOnClickListener(this);
        mProfileLogin = (Button) findViewById(R.id.login_save_btn);
        mProfileLogin.setOnClickListener(this);

        mLoginQQ = (ImageView) findViewById(R.id.login_qq);
        mLoginQQ.setOnClickListener(this);

        mLoginWechat = (ImageView) findViewById(R.id.login_wechat);
        mLoginWechat.setOnClickListener(this);

        mLoginSina = (ImageView) findViewById(R.id.login_sina);
        mLoginSina.setOnClickListener(this);

        mEtName = (EditText) findViewById(R.id.user_name_text);
        mEtPassWord = (EditText) findViewById(R.id.password_text);

    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_registered:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_save_btn:
                login();
                break;
            case R.id.forget_password:
                intent = new Intent(this, ReSetPassWordActivity.class);
                startActivity(intent);
                break;
            case R.id.login_qq:
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.login_wechat:
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.login_sina:
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.SINA, umAuthListener);
                break;
        }
    }

    /**
     * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/4/7 上午11:21.
     * Company 路之遥网络科技有限公司
     * Description  选取需要授权的平台，并进行授权，其中umAuthLisrener是回调监听器，需要开发者根据需求重新定义
     */
    UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            mProgressDialog.show();
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            //Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
            mShareAPI.getPlatformInfo(LoginV2Activity.this, platform, umAuthUserInfoListener);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            mProgressDialog.hide();
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            mProgressDialog.hide();
            Toast.makeText(getApplicationContext(), "授权取消", Toast.LENGTH_SHORT).show();
        }
    };


    /*
     * 初始化UMShareAPI，然后进行用户信息获取
     */
    private UMAuthListener umAuthUserInfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> info) {
            mProgressDialog.hide();
            //Toast.makeText(getApplicationContext(), "用户信息获取成功", Toast.LENGTH_SHORT).show();
            mUMengInfo = new UMengInfo();
            if (SHARE_MEDIA.WEIXIN.equals(platform)) {
                mUMengInfo.setName(info.get("name").toString());
                mUMengInfo.setHead(info.get("iconurl").toString());
                mUMengInfo.setSex(info.get("gender").toString());
                mUMengInfo.setWeixinopenid(info.get("openid").toString());
                mUMengInfo.setOthertype(CommonConstants.OTHER_LOGIN_WEIXIN);
            } else if (SHARE_MEDIA.SINA.equals(platform)) {
                mUMengInfo.setName(info.get("name").toString());
                //mUMengInfo.setHead(info.get("iconurl").toString());//大头像
                mUMengInfo.setHead(info.get("avatar_large").toString());//大头像
                mUMengInfo.setSex(info.get("gender").toString());
                mUMengInfo.setSinaopenid(info.get("id").toString());
                mUMengInfo.setOthertype(CommonConstants.OTHER_LOGIN_SINA);
            } else if (SHARE_MEDIA.QQ.equals(platform)) {
                mUMengInfo.setName(info.get("name").toString());
                mUMengInfo.setHead(info.get("iconurl").toString());
                mUMengInfo.setSex(info.get("gender").toString());
                mUMengInfo.setQqopenid(info.get("openid").toString());
                mUMengInfo.setOthertype(CommonConstants.OTHER_LOGIN_QQ);
            }
            uMenglogin();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            mProgressDialog.hide();
            Toast.makeText(getApplicationContext(), "用户信息获取失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            mProgressDialog.hide();
            Toast.makeText(getApplicationContext(), "用户信息获取取消", Toast.LENGTH_SHORT).show();
        }
    };

    //重写onActivityResult()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }


    //用户信息登录
    private void login() {
        final String name = mEtName.getText().toString().trim();
        final String passWord = mEtPassWord.getText().toString().trim();

        if (CommonUtility.isEmpty(name)) {
            Toast.makeText(LoginV2Activity.this, "亲，请输入用户名！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (CommonUtility.isEmpty(passWord)) {
            Toast.makeText(LoginV2Activity.this, "亲，请输入密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        SaDataProccessHandler<Void, Void, DiUserInfo> dataVerHandler = new SaDataProccessHandler<Void, Void, DiUserInfo>(this) {
            @Override
            public void onSuccess(DiUserInfo data) {
                ApplicationData.mDiUserInfo = data;
                CustomerIDSharedPreferences.init(LoginV2Activity.this);
                CustomerIDSharedPreferences.setCustomerId(data.getId());

                mLoginSharedPreferences.setUserInfoStr(name + "|" + passWord);

                Toast.makeText(LoginV2Activity.this, getString(R.string.profile_login_sess), Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        UserService.getInstance().login(name, passWord, dataVerHandler);
    }

    /**
     * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/4/7 下午1:22.
     * Company 路之遥网络科技有限公司
     * Description 用户信息登录
     */
    private void uMenglogin() {
        SaDataProccessHandler<Void, Void, DiUserInfo> dataVerHandler = new SaDataProccessHandler<Void, Void, DiUserInfo>(this) {
            @Override
            public void onSuccess(DiUserInfo data) {
                if(null != data && data.getId() > 0) {
                    ApplicationData.mDiUserInfo = data;
                    CustomerIDSharedPreferences.init(LoginV2Activity.this);
                    CustomerIDSharedPreferences.setCustomerId(data.getId());
                    Toast.makeText(LoginV2Activity.this, getString(R.string.profile_login_sess), Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(LoginV2Activity.this, getString(R.string.register_tip4), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginV2Activity.this, RegisterActivity.class);
                    intent.putExtra("UMengInfo", ParseJsonToObject.getJsonFromObj(mUMengInfo).toString());
                    startActivity(intent);
                }
            }
        };
        UserService.getInstance().otherLoginV2(mUMengInfo, dataVerHandler);
    }


    /**
     * 此方法必须重写，以决绝退出activity时 dialog未dismiss而报错的bug
     */
    @Override
    protected void onDestroy() {
        try{
            mProgressDialog.dismiss();
        }catch (Exception e) {
            System.out.println("myDialog取消，失败！");
        }
        super.onDestroy();
    }

}
