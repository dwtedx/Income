package com.dwtedx.income.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiUserInfo;
import com.dwtedx.income.service.UserService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.FileProviderUtils;
import com.dwtedx.income.utility.SystemProgramUtils;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.CircleImageView;

import java.io.File;

public class ProfileInfoActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, View.OnClickListener {

    private AppTitleBar mToolBar;
    private int[] mClickView = {R.id.user_name_head_view, R.id.user_nick_name_text, R.id.user_email_text, R.id.user_signature_text,
            R.id.user_work_text, R.id.user_weixin_text, R.id.user_qq_text, R.id.user_sex_text, R.id.user_birthday_text};

    //所有要显示信息的View
    private CircleImageView mHeadImageView;
    private Button mNickNameText;
    private Button mEmailText;
    private Button mSignatureText;
    private Button mWorkText;
    private Button mWeixinText;
    private Button mQQtext;
    private Button mSexText;
    private Button mBirthdayText;

    //头像
    private String mCameraFileName;
    private String mCameraCapFileName;
    private final static int CAMERA_REQUEST_CODE = 1004;//权限请求

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initData();

    }

    private void initData() {
        if(null != ApplicationData.mDiUserInfo) {
            //赋值
            Glide.with(this).load(ApplicationData.mDiUserInfo.getHead()).into(mHeadImageView);
            mNickNameText.setText(ApplicationData.mDiUserInfo.getName());
            mEmailText.setText(ApplicationData.mDiUserInfo.getEmail());
            mSignatureText.setText(ApplicationData.mDiUserInfo.getSignature());
            mWorkText.setText(ApplicationData.mDiUserInfo.getWork());
            mWeixinText.setText(ApplicationData.mDiUserInfo.getWeixin());
            mQQtext.setText(ApplicationData.mDiUserInfo.getQq());
            mSexText.setText(ApplicationData.mDiUserInfo.getSex());
            mBirthdayText.setText(ApplicationData.mDiUserInfo.getBirthdayStr());
        }else{
            //赋值
            Glide.with(this).load(R.mipmap.userhead).into(mHeadImageView);
            mNickNameText.setText("");
            mEmailText.setText("");
            mSignatureText.setText("");
            mWorkText.setText("");
            mWeixinText.setText("");
            mQQtext.setText("");
            mSexText.setText("");
            mBirthdayText.setText("");
        }
    }

    private void initView() {
        //查找View
        mToolBar = (AppTitleBar) findViewById(R.id.app_title);
        mToolBar.setOnTitleClickListener(this);
        mHeadImageView = (CircleImageView) findViewById(R.id.imageView_head);
        mNickNameText = (Button) findViewById(R.id.user_nick_name_text);
        mEmailText = (Button) findViewById(R.id.user_email_text);
        mSignatureText = (Button) findViewById(R.id.user_signature_text);
        mWorkText = (Button) findViewById(R.id.user_work_text);
        mWeixinText = (Button) findViewById(R.id.user_weixin_text);
        mQQtext = (Button) findViewById(R.id.user_qq_text);
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
            case R.id.user_name_head_view:
                selectPicture();
                break;

            case R.id.user_nick_name_text:
            case R.id.user_email_text:
            case R.id.user_signature_text:
            case R.id.user_work_text:
            case R.id.user_weixin_text:
            case R.id.user_qq_text:
            case R.id.user_sex_text:
            case R.id.user_birthday_text:
                showEditInfo();
                break;
        }
    }

    private void showEditInfo() {
        if (!isLogin()) {
            startActivity(new Intent(ProfileInfoActivity.this, LoginV2Activity.class));
            return;
        }
        startActivity(new Intent(this, ProfileInfoEditActivity.class));
    }

    //头像上传////////////////头像上传///////////////////头像上传/////////////////////头像上传//////////////////头像上传/////////
    //选择相片
    private void selectPicture() {
        mCameraFileName = CommonUtility.getTempImageName();
        mCameraCapFileName = "CAP" + mCameraFileName;
        new MaterialDialog.Builder(this)
                .items(R.array.mode)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {
                            if (ContextCompat.checkSelfPermission(ProfileInfoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                    || ContextCompat.checkSelfPermission(ProfileInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                    || ContextCompat.checkSelfPermission(ProfileInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                //申请WRITE_EXTERNAL_STORAGE权限
                                //ActivityCompat.requestPermissions(getActivity(),
                                //new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                                //以下是直接使用Fragment的requestPermissions方法
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                                } else {
                                    SystemProgramUtils.startCamera(ProfileInfoActivity.this, new File(CommonConstants.INCOME_IMAGES, mCameraFileName));
                                }
                            } else {
                                SystemProgramUtils.startCamera(ProfileInfoActivity.this,new File(CommonConstants.INCOME_IMAGES, mCameraFileName));
                            }
                        } else if (which == 1) {
                            SystemProgramUtils.startPicture(ProfileInfoActivity.this);
                        }
                    }
                }).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            try {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    SystemProgramUtils.startCamera(ProfileInfoActivity.this, new File(CommonConstants.INCOME_IMAGES, mCameraFileName));
                } else {
                    // Permission Denied
                    Toast.makeText(ProfileInfoActivity.this, "访问被拒绝！", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ProfileInfoActivity.this, "访问被拒绝！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (Activity.RESULT_OK != resultCode) {
                Toast.makeText(ProfileInfoActivity.this, "请求出错！请稍后重试！", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri filtUri;
            File outputFile = new File(CommonConstants.INCOME_IMAGES, mCameraCapFileName);//裁切后输出的图片
            switch (requestCode) {

                case SystemProgramUtils.REQUEST_CODE_PAIZHAO:
                    //拍照完成，进行图片裁切
                    File file = new File(CommonConstants.INCOME_IMAGES, mCameraFileName);
                    filtUri = FileProviderUtils.uriFromFile(ProfileInfoActivity.this, file);
                    SystemProgramUtils.startPhotoZoom(ProfileInfoActivity.this, filtUri, outputFile);
                    break;
                case SystemProgramUtils.REQUEST_CODE_ZHAOPIAN:
                    //相册选择图片完毕，进行图片裁切
                    if (data == null || data.getData() == null) {
                        return;
                    }
                    filtUri = data.getData();
                    SystemProgramUtils.startPhotoZoom(ProfileInfoActivity.this, filtUri, outputFile);
                    break;
                case SystemProgramUtils.REQUEST_CODE_CAIQIE:
                    //图片裁切完成，显示裁切后的图片
                    try {
                        Bitmap bitmap = CommonUtility.getLocalBitmap(outputFile.getPath());
                        uploadPhoto(bitmap);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
            }
        }catch (Exception e){
            Toast.makeText(ProfileInfoActivity.this, "拍照或相册未返回图片！建议您重启或更换设备换头像！", Toast.LENGTH_LONG).show();
        }

    }

    //上传头像的网络请求
    private void uploadPhoto(Bitmap bmp) {
        mHeadImageView.setImageBitmap(bmp);
        String imgData = CommonUtility.encodeTobase64(bmp);
        SaDataProccessHandler<Void, Void, DiUserInfo> dataVerHandler = new SaDataProccessHandler<Void, Void, DiUserInfo>(this) {
            @Override
            public void onSuccess(DiUserInfo data) {
                ApplicationData.mDiUserInfo = data;
                Glide.with(ProfileInfoActivity.this).load(ApplicationData.mDiUserInfo.getHead()).into(mHeadImageView);
                Snackbar.make(findViewById(R.id.app_title), R.string.user_head_tip, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        };
        UserService.getInstance().uploadPic(ApplicationData.mDiUserInfo.getId(), imgData, dataVerHandler);
    }

    //头像上传结束////////////////头像上传结束///////////////////头像上传结束/////////////////////头像上传结束//////////////////头像上传结束/////////

}
