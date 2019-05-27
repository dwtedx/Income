package com.dwtedx.income.profile;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiUserInfo;
import com.dwtedx.income.provider.CustomerIDSharedPreferences;
import com.dwtedx.income.provider.LoginSharedPreferences;
import com.dwtedx.income.service.UserService;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.AppTitleBar;

/**
 * A login screen that offers login via email/password.
 */
public class ReSetPassWordActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, View.OnClickListener {

    private AppTitleBar mToolBar;

    private EditText mPhoneView;
    private EditText mCodeView;

    private EditText mPassWordView;
    private EditText mPassWordConfigView;

    private Button mCodeBtn;
    private Button mSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initView();

    }

    private void initView() {
        //初始化
        mToolBar = (AppTitleBar) findViewById(R.id.app_title);
        mToolBar.setOnTitleClickListener(this);


        //view
        mPhoneView = (EditText) findViewById(R.id.user_name_text);
        mCodeView = (EditText) findViewById(R.id.user_smscode_text);
        mPassWordView = (EditText) findViewById(R.id.profile_password_new_text);
        mPassWordConfigView = (EditText) findViewById(R.id.profile_password_config_text);
        mCodeBtn = (Button) findViewById(R.id.user_smscode_text_btn);
        mCodeBtn.setOnClickListener(this);
        mSaveBtn = (Button) findViewById(R.id.user_info_save_btn);
        mSaveBtn.setOnClickListener(this);
    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_smscode_text_btn:
                getSmsCode();
                break;

            case R.id.user_info_save_btn:
                savePass();
                break;
        }
    }

    private void getSmsCode() {
        String phone = mPhoneView.getText().toString();
        if (CommonUtility.isEmpty(phone)) {
            Toast.makeText(this, R.string.register_tip2, Toast.LENGTH_SHORT).show();
            return;
        }
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new
                SaDataProccessHandler<Void, Void, Void>(this) {
                    @Override
                    public void onSuccess(Void data) {
                        Toast.makeText(ReSetPassWordActivity.this, R.string.register_tip5, Toast.LENGTH_SHORT).show();
                        startCount();
                    }
                };
        UserService.getInstance().sendSmsHave(phone, dataVerHandler);
    }

    /**
     * 验证码倒计时
     */
    private void startCount() {
        new CountDownTimer(60 * 1000, 1000) {
            //倒计时开始
            @Override
            public void onTick(long millisUntilFinished) {
                mCodeBtn.setText((millisUntilFinished / 1000) + "秒后获取");
                mCodeBtn.setEnabled(false);
            }

            //倒计时结束后
            @Override
            public void onFinish() {
                mCodeBtn.setText("获取验证码");
                mCodeBtn.setEnabled(true);
            }
        }.start();
    }

    /**
     * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/4/7 下午1:22.
     * Company 路之遥网络科技有限公司
     * Description 用户信息登录
     */
    private void savePass() {
        //密码
        String phone = mPhoneView.getText().toString();
        String code = mCodeView.getText().toString();
        String pass = mPassWordView.getText().toString();
        String passConfig = mPassWordConfigView.getText().toString();

        if (CommonUtility.isEmpty(phone)) {
            Toast.makeText(this, R.string.register_tip2, Toast.LENGTH_SHORT).show();
            return;
        }
        if (CommonUtility.isEmpty(code)) {
            Toast.makeText(this, R.string.register_tip6, Toast.LENGTH_SHORT).show();
            return;
        }
        if (CommonUtility.isEmpty(pass) || CommonUtility.isEmpty(passConfig)) {
            Toast.makeText(this, R.string.profile_password_tip0, Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.length() < 6){
            Toast.makeText(this, R.string.profile_password_tip4, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pass.equals(passConfig)) {
            Toast.makeText(this, R.string.profile_password_tip1, Toast.LENGTH_SHORT).show();
            return;
        }

        SaDataProccessHandler<Void, Void, DiUserInfo> dataVerHandler = new SaDataProccessHandler<Void, Void, DiUserInfo>(this) {
            @Override
            public void onSuccess(DiUserInfo data) {
                LoginSharedPreferences.init(ReSetPassWordActivity.this);
                LoginSharedPreferences.clear();
                CustomerIDSharedPreferences.init(ReSetPassWordActivity.this);
                CustomerIDSharedPreferences.clear();
                ApplicationData.mDiUserInfo = null;
                Toast.makeText(ReSetPassWordActivity.this, R.string.profile_password_tip3, Toast.LENGTH_SHORT).show();
                ReSetPassWordActivity.this.finish();
            }
        };
        UserService.getInstance().reSetPassWord(phone, code, pass, dataVerHandler);
    }

}

