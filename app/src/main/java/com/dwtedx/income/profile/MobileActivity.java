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
public class MobileActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, View.OnClickListener {

    private AppTitleBar mToolBar;

    private EditText mUserNameText;
    private EditText mUserNameCodeText;

    private Button mBindBtn;
    private Button mCodeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);

        initView();

    }

    private void initView() {
        //初始化
        mToolBar = (AppTitleBar) findViewById(R.id.app_title);
        mToolBar.setOnTitleClickListener(this);
        if (CommonUtility.isEmpty(ApplicationData.mDiUserInfo.getPhone())) {
            mToolBar.setTitleText(R.string.profile_phone);
        } else {
            mToolBar.setTitleText(R.string.profile_phone_update);
        }

        //view
        mUserNameText = (EditText) findViewById(R.id.user_name_text);
        mUserNameCodeText = (EditText) findViewById(R.id.user_smscode_text);
        mCodeBtn = (Button) findViewById(R.id.user_smscode_text_btn);
        mCodeBtn.setOnClickListener(this);
        mBindBtn = (Button) findViewById(R.id.bind_btn);
        mBindBtn.setOnClickListener(this);
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

            case R.id.bind_btn:
                savePhone();
                break;
        }
    }

    private void getSmsCode() {
        String phone = mUserNameText.getText().toString();
        if (CommonUtility.isEmpty(phone)) {
            Toast.makeText(this, R.string.register_tip2, Toast.LENGTH_SHORT).show();
            return;
        }
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new
                SaDataProccessHandler<Void, Void, Void>(this) {
                    @Override
                    public void onSuccess(Void data) {
                        Toast.makeText(MobileActivity.this, R.string.register_tip5, Toast.LENGTH_SHORT).show();
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
    private void savePhone() {
        String phone = mUserNameText.getText().toString().trim();
        String code = mUserNameCodeText.getText().toString().trim();

        if (CommonUtility.isEmpty(phone)) {
            Toast.makeText(this, R.string.register_tip2, Toast.LENGTH_SHORT).show();
            return;
        }
        if (CommonUtility.isEmpty(code)) {
            Toast.makeText(this, R.string.register_tip6, Toast.LENGTH_SHORT).show();
            return;
        }

        SaDataProccessHandler<Void, Void, DiUserInfo> dataVerHandler = new SaDataProccessHandler<Void, Void, DiUserInfo>(this) {
            @Override
            public void onSuccess(DiUserInfo data) {
                ApplicationData.mDiUserInfo = data;
                Toast.makeText(MobileActivity.this, R.string.profile_phone_bind_tip, Toast.LENGTH_SHORT).show();
                MobileActivity.this.finish();
            }
        };
        UserService.getInstance().updateUserPhone(phone, code, dataVerHandler);
    }

}

