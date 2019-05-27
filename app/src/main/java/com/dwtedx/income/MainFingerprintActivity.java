package com.dwtedx.income;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.home.HomeV3Activity;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.FingerprintAuthenticationDialogFragment;
import com.dwtedx.income.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFingerprintActivity extends BaseActivity implements View.OnClickListener {

    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    private static final String DIALOG_FRAGMENT_USER_TAG = "userFragment";

    @BindView(R.id.m_userhead_imageview)
    CircleImageView mUserheadImageview;
    @BindView(R.id.m_user_textview)
    TextView mUserTextview;
    @BindView(R.id.m_fingerprint_layout)
    LinearLayout mFingerprintLayout;
    @BindView(R.id.m_fingerprint_description_userpass)
    TextView mFingerprintDescriptionUserpass;

    FingerprintAuthenticationDialogFragment mFragment;
    MainUserNameAuthDialogFragment mUserNameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fingerprint);
        ButterKnife.bind(this);

        //状态栏颜色 不支持4.4
        //getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));
        }

        mFragment = new FingerprintAuthenticationDialogFragment();
        //fragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));
        mFragment.setCancelable(false);
        mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        mUserNameFragment = new MainUserNameAuthDialogFragment();
        mUserNameFragment.setCancelable(false);

        mFingerprintLayout.setOnClickListener(this);
        mFingerprintDescriptionUserpass.setOnClickListener(this);

        if(isLogin()){
            Glide.with(this).load(ApplicationData.mDiUserInfo.getHead()).into(mUserheadImageview);
            String userName = ApplicationData.mDiUserInfo.getName();
            if(CommonUtility.isEmpty(userName)){
                userName = ApplicationData.mDiUserInfo.getPhone();
            }
            if(CommonUtility.isEmpty(userName)){
                userName = ApplicationData.mDiUserInfo.getUsername();
            }
            mUserTextview.setText(userName);
        }else{
            mUserTextview.setVisibility(View.GONE);
        }
    }

    /**
     * Proceed the purchase operation
     *
     * @param withFingerprint {@code true} if the purchase was made by using a fingerprint
     * @param cryptoObject    the Crypto object
     */
    public void onPurchased(boolean withFingerprint, @Nullable FingerprintManager.CryptoObject cryptoObject) {
        if (withFingerprint) {
            startActivity(new Intent(MainFingerprintActivity.this, HomeV3Activity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.m_fingerprint_layout:
                mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                break;

            case R.id.m_fingerprint_description_userpass:
                mUserNameFragment.show(getFragmentManager(), DIALOG_FRAGMENT_USER_TAG);
                break;
        }
    }
}
