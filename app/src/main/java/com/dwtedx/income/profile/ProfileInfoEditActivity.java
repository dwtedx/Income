package com.dwtedx.income.profile;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;

import com.dwtedx.income.entity.DiUserInfo;
import com.dwtedx.income.service.UserService;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.AppTitleBar;

import java.util.Calendar;

public class ProfileInfoEditActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, View.OnClickListener {

    private AppTitleBar mToolBar;
    private int[] mClickView = {R.id.user_sex_text, R.id.user_birthday_text, R.id.user_info_save_btn};

    //所有要显示信息的View
    private EditText mNickNameText;
    private EditText mEmailText;
    private EditText mSignatureText;
    private EditText mWorkText;
    private EditText mWeixinText;
    private EditText mQQText;
    private Button mSexText;
    private Button mBirthdayText;

    private DiUserInfo mDiUserInfo;

    //时间
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info_edit);

        mDiUserInfo = new DiUserInfo();
        mDiUserInfo.setBirthday(ApplicationData.mDiUserInfo.getBirthdayStr());
        mDiUserInfo.setSex(ApplicationData.mDiUserInfo.getSex());
        mCalendar = Calendar.getInstance();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(null == ApplicationData.mDiUserInfo){
            finish();
        }else {
            initData();
        }
    }

    private void initData() {
        //赋值
        mNickNameText.setText(ApplicationData.mDiUserInfo.getName());
        mEmailText.setText(ApplicationData.mDiUserInfo.getEmail());
        mSignatureText.setText(ApplicationData.mDiUserInfo.getSignature());
        mWorkText.setText(ApplicationData.mDiUserInfo.getWork());
        mWeixinText.setText(ApplicationData.mDiUserInfo.getWeixin());
        mQQText.setText(ApplicationData.mDiUserInfo.getQq());
        mSexText.setText(ApplicationData.mDiUserInfo.getSex());
        mBirthdayText.setText(ApplicationData.mDiUserInfo.getBirthdayStr());
    }

    private void initView() {
        //查找View
        mToolBar = (AppTitleBar) findViewById(R.id.app_title);
        mToolBar.setOnTitleClickListener(this);
        mNickNameText = (EditText) findViewById(R.id.user_nick_name_text);
        mEmailText = (EditText) findViewById(R.id.user_email_text);
        mSignatureText = (EditText) findViewById(R.id.user_signature_text);
        mWorkText = (EditText) findViewById(R.id.user_work_text);
        mWeixinText = (EditText) findViewById(R.id.user_weixin_text);
        mQQText = (EditText) findViewById(R.id.user_qq_text);
        mSexText = (Button) findViewById(R.id.user_sex_text);
        mBirthdayText = (Button) findViewById(R.id.user_birthday_text);

        for (int id : mClickView) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                startActivity(new Intent(this, SetupActivity.class));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_sex_text:
                selectSex();
                break;

            case R.id.user_birthday_text:
                setBirthday();
                break;

            case R.id.user_info_save_btn:
                saveInfo();
                break;
        }
    }

    private void saveInfo() {
        mDiUserInfo.setName(mNickNameText.getText().toString());
        mDiUserInfo.setEmail(mEmailText.getText().toString());
        mDiUserInfo.setSignature(mSignatureText.getText().toString());
        mDiUserInfo.setWork(mWorkText.getText().toString());
        mDiUserInfo.setWeixin(mWeixinText.getText().toString());
        mDiUserInfo.setQq(mQQText.getText().toString());

        SaDataProccessHandler<Void, Void, DiUserInfo> dataVerHandler = new SaDataProccessHandler<Void, Void, DiUserInfo>(ProfileInfoEditActivity.this) {
            @Override
            public void onSuccess(DiUserInfo data) {
                ApplicationData.mDiUserInfo = data;
                ProfileInfoEditActivity.this.finish();
            }
        };
        UserService.getInstance().updateUserInfo(mDiUserInfo, dataVerHandler);

    }

    private void setBirthday() {
        DatePickerDialog dd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(year, monthOfYear, dayOfMonth);
                String birthday = CommonUtility.stringDateFormartYYYYMMdd(mCalendar.getTime());
                mBirthdayText.setText(birthday);
                mDiUserInfo.setBirthday(birthday);
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        dd.show();
    }

    private void selectSex() {
        new MaterialDialog.Builder(this)
                .title(R.string.user_sex)
                .items(R.array.user_sex_mode)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        mSexText.setText(text.toString());
                        mDiUserInfo.setSex(text.toString());
                    }
                })
                .show();
    }
}
