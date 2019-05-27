package com.dwtedx.income.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiUserInfo;
import com.dwtedx.income.entity.UMengInfo;
import com.dwtedx.income.provider.CustomerIDSharedPreferences;
import com.dwtedx.income.service.UserService;
import com.dwtedx.income.utility.CommonConstants;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private UMShareAPI mShareAPI;
    private UMengInfo mUMengInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.tabs_back).setOnClickListener(this);
        findViewById(R.id.login_qq).setOnClickListener(this);
        findViewById(R.id.login_weibo).setOnClickListener(this);

        //已授权的平台，可以获取用户信息（新浪微博可以获取用户好友列表） 实现的方法与授权和解除授权类似：
        //mShareAPI = UMShareAPI.get(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tabs_back:
                LoginActivity.this.finish();
                break;
            case R.id.login_qq:
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.login_weibo:
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.SINA, umAuthListener);
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

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
            mShareAPI.getPlatformInfo(LoginActivity.this, platform, umAuthUserInfoListener);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
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
            Toast.makeText(getApplicationContext(), "用户信息获取失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "用户信息获取取消", Toast.LENGTH_SHORT).show();
        }
    };

    //重写onActivityResult()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
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
                ApplicationData.mDiUserInfo = data;
                CustomerIDSharedPreferences.init(LoginActivity.this);
                CustomerIDSharedPreferences.setCustomerId(data.getId());
                Toast.makeText(LoginActivity.this, getString(R.string.profile_login_sess), Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        UserService.getInstance().otherLogin(mUMengInfo, dataVerHandler);
    }

}

