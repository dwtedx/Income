package com.dwtedx.income.profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiUserInfo;
import com.dwtedx.income.entity.UMengInfo;
import com.dwtedx.income.provider.CustomerIDSharedPreferences;
import com.dwtedx.income.provider.LoginSharedPreferences;
import com.dwtedx.income.service.UserService;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.ParseJsonToObject;
import com.dwtedx.income.widget.AppTitleBar;

import org.json.JSONObject;

/**
 * Created by 陈连杰 on 2016/2/15.
 * 输入注册信息的界面
 */
public class RegisterActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, View.OnClickListener {

    private AppTitleBar mCommonTitle;

    private EditText mUserNameText;
    private EditText mUserNameCodeText;
    private EditText mRegisterPasswordText;
    private EditText mRegisterPasswordTextConfig;

    private Button mRegisterBtn;
    private Button mRegisterCodeBtn;

    private TextView mRegisterByUsername;

    private LoginSharedPreferences mLoginSharedPreferences;
    private UMengInfo mUMengInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        try {
            mUMengInfo = ParseJsonToObject.getObject(UMengInfo.class, new JSONObject(getIntent().getStringExtra("UMengInfo")));
        } catch (Exception e) {
            e.printStackTrace();
            mUMengInfo = null;
        }

        mCommonTitle = (AppTitleBar) findViewById(R.id.app_title);
        mCommonTitle.setOnTitleClickListener(this);
        if(null == mUMengInfo){
            mUMengInfo = new UMengInfo();
            mCommonTitle.setTitleText(R.string.register);
        }else{
            mCommonTitle.setTitleText(R.string.register_title);
        }

        mUserNameText = (EditText) findViewById(R.id.user_name_text);
        mUserNameCodeText = (EditText) findViewById(R.id.user_smscode_text);
        mRegisterPasswordText = (EditText) findViewById(R.id.register_password_text);
        mRegisterPasswordTextConfig = (EditText) findViewById(R.id.register_password_text_config);
        mRegisterBtn = (Button) findViewById(R.id.register_btn);
        mRegisterBtn.setOnClickListener(this);
        mRegisterCodeBtn = (Button) findViewById(R.id.user_smscode_text_btn);
        mRegisterCodeBtn.setOnClickListener(this);
        mRegisterByUsername = (TextView) findViewById(R.id.register_by_username);
        mRegisterByUsername.setOnClickListener(this);

        mLoginSharedPreferences.init(this);
    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                Intent intent = new Intent(this, RegisterByUsernameActivity.class);
                if(null != mUMengInfo){
                    intent.putExtra("UMengInfo", ParseJsonToObject.getJsonFromObj(mUMengInfo).toString());
                }
                startActivity(intent);
                finish();
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
                register();
                break;

            case R.id.user_smscode_text_btn:
                getSmsCode();
                break;

            case R.id.register_by_username:
                Intent intent = new Intent(this, RegisterByUsernameActivity.class);
                if(null != mUMengInfo){
                    intent.putExtra("UMengInfo", ParseJsonToObject.getJsonFromObj(mUMengInfo).toString());
                }
                startActivity(intent);
                finish();
                break;
        }
    }

    private void getSmsCode(){
        String phone = mUserNameText.getText().toString();
        if(CommonUtility.isEmpty(phone)){
            Toast.makeText(this, R.string.register_tip2, Toast.LENGTH_SHORT).show();
            return;
        }
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new
                SaDataProccessHandler<Void, Void, Void>(this) {
                    @Override
                    public void onSuccess(Void data) {
                        Toast.makeText(RegisterActivity.this, R.string.register_tip5, Toast.LENGTH_SHORT).show();
                        startCount();
                    }
                };
        UserService.getInstance().sendSms(phone, dataVerHandler);
    }

    /**
     * 验证码倒计时
     */
    private void startCount() {
        new CountDownTimer(60 * 1000, 1000) {
            //倒计时开始
            @Override
            public void onTick(long millisUntilFinished) {
                mRegisterCodeBtn.setText((millisUntilFinished / 1000) + "秒后获取");
                mRegisterCodeBtn.setEnabled(false);
            }

            //倒计时结束后
            @Override
            public void onFinish() {
                mRegisterCodeBtn.setText("获取验证码");
                mRegisterCodeBtn.setEnabled(true);
            }
        }.start();
    }

    private void register() {
        final String username = mUserNameText.getText().toString().trim();
        final String pass = mRegisterPasswordText.getText().toString().trim();
        final String smscode = mUserNameCodeText.getText().toString().trim();
        String passConfig = mRegisterPasswordTextConfig.getText().toString().trim();

        if(CommonUtility.isEmpty(username)){
            Toast.makeText(this, R.string.register_tip2, Toast.LENGTH_SHORT).show();
            return;
        }
        if(CommonUtility.isEmpty(smscode)){
            Toast.makeText(this, R.string.register_tip6, Toast.LENGTH_SHORT).show();
            return;
        }
        if(CommonUtility.isEmpty(pass) || CommonUtility.isEmpty(passConfig)){
            Toast.makeText(this, R.string.register_tip0, Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.length() < 6){
            Toast.makeText(this, R.string.profile_password_tip4, Toast.LENGTH_SHORT).show();
            return;
        }
        if(!pass.equals(passConfig)){
            Toast.makeText(this, R.string.register_tip1, Toast.LENGTH_SHORT).show();
            return;
        }

        mUMengInfo.setUsername(username);
        mUMengInfo.setPassword(pass);
        mUMengInfo.setSmscode(smscode);

        SaDataProccessHandler<Void, Void, DiUserInfo> dataVerHandler = new SaDataProccessHandler<Void, Void, DiUserInfo>(this) {
            @Override
            public void onSuccess(DiUserInfo data) {
                if(null != data && data.getId() > 0) {
                    ApplicationData.mDiUserInfo = data;
                    CustomerIDSharedPreferences.init(RegisterActivity.this);
                    CustomerIDSharedPreferences.setCustomerId(data.getId());

                    mLoginSharedPreferences.setUserInfoStr(username + "|" + pass);

                    Toast.makeText(RegisterActivity.this, getString(R.string.register_tip3), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this, getString(R.string.register_error), Toast.LENGTH_SHORT).show();
                }
            }
        };
        UserService.getInstance().registerByPhone(mUMengInfo, dataVerHandler);

    }
}
