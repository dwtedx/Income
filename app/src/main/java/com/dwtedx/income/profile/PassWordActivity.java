package com.dwtedx.income.profile;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
public class PassWordActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, View.OnClickListener {

    private AppTitleBar mToolBar;

    private View mPasswordLayout, mPasswordLine;

    private EditText mPassWord;
    private EditText mPassWordNew;
    private EditText mPassWordConfig;

    private Button mSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        initView();

    }

    private void initView() {
        //初始化
        mToolBar = (AppTitleBar) findViewById(R.id.app_title);
        mToolBar.setOnTitleClickListener(this);
        mPasswordLayout = findViewById(R.id.profile_password_layout);
        mPasswordLine = findViewById(R.id.profile_password_line);
        if (CommonUtility.isEmpty(ApplicationData.mDiUserInfo.getPassword())) {
            mToolBar.setTitleText(R.string.profile_password_title);
            mPasswordLayout.setVisibility(View.GONE);
            mPasswordLine.setVisibility(View.GONE);
        } else {
            mToolBar.setTitleText(R.string.profile_password_title_update);
        }

        //view
        mPassWord = (EditText) findViewById(R.id.profile_password_text);
        mPassWordNew = (EditText) findViewById(R.id.profile_password_new_text);
        mPassWordConfig = (EditText) findViewById(R.id.profile_password_config_text);
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
                Intent intent = new Intent(this, ReSetPassWordActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_save_btn:
                savePass();
                break;

        }
    }

    /**
     * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/4/7 下午1:22.
     * Company 路之遥网络科技有限公司
     * Description 用户信息登录
     */
    private void savePass() {
        //初始化密码
        int type = 0;
        String pass = mPassWord.getText().toString().trim();
        String newPass = mPassWordNew.getText().toString().trim();
        String newConfig = mPassWordConfig.getText().toString().trim();

        if (CommonUtility.isEmpty(ApplicationData.mDiUserInfo.getPassword())) {
            type = 1;
            if(CommonUtility.isEmpty(newPass) || CommonUtility.isEmpty(newConfig)){
                Toast.makeText(this, R.string.profile_password_tip0, Toast.LENGTH_SHORT).show();
                return;
            }
            if(!newPass.equals(newConfig)){
                Toast.makeText(this, R.string.profile_password_tip1, Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            type = 2;
            if(CommonUtility.isEmpty(pass)){
                Toast.makeText(this, R.string.profile_password_tip2, Toast.LENGTH_SHORT).show();
                return;
            }
            if(CommonUtility.isEmpty(newPass) || CommonUtility.isEmpty(newConfig)){
                Toast.makeText(this, R.string.profile_password_tip0, Toast.LENGTH_SHORT).show();
                return;
            }
            if(!newPass.equals(newConfig)){
                Toast.makeText(this, R.string.profile_password_tip1, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(newPass.length() < 6){
            Toast.makeText(this, R.string.profile_password_tip4, Toast.LENGTH_SHORT).show();
            return;
        }

        //type 1：初始化密码 2：修改密码
        SaDataProccessHandler<Void, Void, DiUserInfo> dataVerHandler = new SaDataProccessHandler<Void, Void, DiUserInfo>(this) {
            @Override
            public void onSuccess(DiUserInfo data) {
                LoginSharedPreferences.init(PassWordActivity.this);
                LoginSharedPreferences.clear();
                CustomerIDSharedPreferences.init(PassWordActivity.this);
                CustomerIDSharedPreferences.clear();
                ApplicationData.mDiUserInfo = null;
                Toast.makeText(PassWordActivity.this, R.string.profile_password_tip3, Toast.LENGTH_SHORT).show();
                PassWordActivity.this.finish();
            }
        };
        UserService.getInstance().updatePassWord(type, pass, newPass, dataVerHandler);
    }

}

