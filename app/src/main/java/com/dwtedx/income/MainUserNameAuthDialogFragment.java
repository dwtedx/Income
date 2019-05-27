/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.dwtedx.income;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiUserInfo;
import com.dwtedx.income.profile.LoginV2Activity;
import com.dwtedx.income.provider.CustomerIDSharedPreferences;
import com.dwtedx.income.service.UserService;
import com.dwtedx.income.utility.CommonUtility;

/**
 * A dialog which uses fingerprint APIs to authenticate the user, and falls back to password
 * authentication if fingerprint is not available.
 */
public class MainUserNameAuthDialogFragment extends DialogFragment {

    private Button mCancelButton;
    private Button mConfirmButton;
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;

    private MainFingerprintActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Do not create a new Fragment when the Activity is re-created such as orientation changes.
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.fingerprint_description_userpass));
        View v = inflater.inflate(R.layout.fingerprint_dialog_username_container, container, false);
        mUserNameEditText = (EditText) v.findViewById(R.id.m_username_text);
        mPasswordEditText = (EditText) v.findViewById(R.id.m_password_text);
        mCancelButton = (Button) v.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mConfirmButton = (Button) v.findViewById(R.id.confirm_button);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainFingerprintActivity) getActivity();
    }

    //用户信息登录
    private void login() {
        final String name = mUserNameEditText.getText().toString().trim();
        final String passWord = mPasswordEditText.getText().toString().trim();

        if (CommonUtility.isEmpty(name)) {
            Toast.makeText(mActivity, "亲，请输入用户名！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (CommonUtility.isEmpty(passWord)) {
            Toast.makeText(mActivity, "亲，请输入密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        SaDataProccessHandler<Void, Void, DiUserInfo> dataVerHandler = new SaDataProccessHandler<Void, Void, DiUserInfo>(mActivity) {
            @Override
            public void onSuccess(DiUserInfo data) {
                ApplicationData.mDiUserInfo = data;
                CustomerIDSharedPreferences.init(mActivity);
                CustomerIDSharedPreferences.setCustomerId(data.getId());

                Toast.makeText(mActivity, getString(R.string.fingerprint_description_userpass_sess), Toast.LENGTH_SHORT).show();
                mActivity.onPurchased(true,null);
            }
        };
        UserService.getInstance().login(name, passWord, dataVerHandler);
    }

}
