package com.dwtedx.income.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class RegisterByUsernameActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, View.OnClickListener {

    private AppTitleBar mCommonTitle;

    private EditText mUserNameText;
    private EditText mRegisterPasswordText;
    private EditText mRegisterPasswordTextConfig;

    private Button mRegisterBtn;

    private LoginSharedPreferences mLoginSharedPreferences;
    private UMengInfo mUMengInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_byusername);

        try {
            mUMengInfo = ParseJsonToObject.getObject(UMengInfo.class, new JSONObject(getIntent().getStringExtra("UMengInfo")));
        } catch (Exception e) {
            e.printStackTrace();
            mUMengInfo = null;
            finish();
        }

        mCommonTitle = (AppTitleBar) findViewById(R.id.app_title);
        mCommonTitle.setOnTitleClickListener(this);

        mUserNameText = (EditText) findViewById(R.id.user_name_text);
        mRegisterPasswordText = (EditText) findViewById(R.id.register_password_text);
        mRegisterPasswordTextConfig = (EditText) findViewById(R.id.register_password_text_config);
        mRegisterBtn = (Button) findViewById(R.id.register_btn);
        mRegisterBtn.setOnClickListener(this);

        mLoginSharedPreferences.init(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
                register();
                break;
        }
    }

    private void register() {
        final String username = mUserNameText.getText().toString().trim();
        final String pass = mRegisterPasswordText.getText().toString().trim();
        String passConfig = mRegisterPasswordTextConfig.getText().toString().trim();

        if(CommonUtility.isEmpty(username)){
            Toast.makeText(this, R.string.register_username_tip2, Toast.LENGTH_SHORT).show();
            return;
        }
        if(username.length() < 4){
            Toast.makeText(this, R.string.register_username_tip3, Toast.LENGTH_SHORT).show();
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

        SaDataProccessHandler<Void, Void, DiUserInfo> dataVerHandler = new SaDataProccessHandler<Void, Void, DiUserInfo>(this) {
            @Override
            public void onSuccess(DiUserInfo data) {
                if(null != data && data.getId() > 0) {
                    ApplicationData.mDiUserInfo = data;
                    CustomerIDSharedPreferences.init(RegisterByUsernameActivity.this);
                    CustomerIDSharedPreferences.setCustomerId(data.getId());

                    mLoginSharedPreferences.setUserInfoStr(username + "|" + pass);

                    Toast.makeText(RegisterByUsernameActivity.this, getString(R.string.register_tip3), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(RegisterByUsernameActivity.this, getString(R.string.register_error), Toast.LENGTH_SHORT).show();
                }
            }
        };
        UserService.getInstance().register(mUMengInfo, dataVerHandler);

    }
}
