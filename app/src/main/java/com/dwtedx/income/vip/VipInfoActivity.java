package com.dwtedx.income.vip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.dwtedx.income.R;
import com.dwtedx.income.alipay.AlipayModel;
import com.dwtedx.income.alipay.AuthResult;
import com.dwtedx.income.alipay.PayResult;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.UserVipInfo;
import com.dwtedx.income.service.ExpExcelService;
import com.dwtedx.income.service.VipInfoService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VipInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.m_title_back_btn)
    LinearLayout mTitleBackBtn;
    @BindView(R.id.m_head_image_view)
    CircleImageView mHeadImageView;
    @BindView(R.id.m_user_name_text_view)
    TextView mUserNameTextView;
    @BindView(R.id.m_user_vip_time_text_view)
    TextView mUserVipTimeTextView;
    @BindView(R.id.m_vip_users_text_view)
    TextView mVipUsersTextView;
    @BindView(R.id.m_vip_month_1_del_text_view)
    TextView mVipMonth1DelTextView;
    @BindView(R.id.m_vip_month_1_view)
    LinearLayout mVipMonth1View;
    @BindView(R.id.m_vip_month_3_del_text_view)
    TextView mVipMonth3DelTextView;
    @BindView(R.id.m_vip_month_3_view)
    LinearLayout mVipMonth3View;
    @BindView(R.id.m_vip_month_12_del_text_view)
    TextView mVipMonth12DelTextView;
    @BindView(R.id.m_vip_month_12_view)
    LinearLayout mVipMonth12View;
    @BindView(R.id.m_save_btn)
    Button registerBtn;
    @BindView(R.id.m_vip_month_1_image_view)
    ImageView mVipMonth1ImageView;
    @BindView(R.id.m_vip_month_3_image_view)
    ImageView mVipMonth3ImageView;
    @BindView(R.id.m_vip_month_12_image_view)
    ImageView mVipMonth12ImageView;
    @BindView(R.id.m_vip_invite_images)
    ImageView mVipinviteImages;

    double mPayAccount;//支付金额
    int mMonths;//月份

    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Date date = new Date();
                        if (ApplicationData.mDiUserInfo.getVipflag() == CommonConstants.VIP_TYPE_VIP) {
                            date = CommonUtility.stringToDate(ApplicationData.mDiUserInfo.getVipendtimeStr() + " 00:00:00");
                        }
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);//设置起时间 
                        cal.add(Calendar.MONTH, mMonths);//增加一个月
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String dateString = formatter.format(cal.getTime());

                        ApplicationData.mDiUserInfo.setVipflag(CommonConstants.VIP_TYPE_VIP);
                        ApplicationData.mDiUserInfo.setVipendtime(dateString);
                        ApplicationData.mDiUserInfo.setVipendtimeStr(dateString);

                        mUserVipTimeTextView.setVisibility(View.VISIBLE);
                        mUserVipTimeTextView.setText(getString(R.string.vip__user_vip_time) + ApplicationData.mDiUserInfo.getVipendtimeStr());
                        registerBtn.setText(R.string.vip_button_fee);
                        Toast.makeText(VipInfoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(VipInfoActivity.this, "支付失败" + payResult, Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_info);
        ButterKnife.bind(this);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.common_body_color));

        //状态栏与背景图完美沉浸
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        //添加删除线
        mVipMonth1DelTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mVipMonth3DelTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mVipMonth12DelTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mPayAccount = 18.8;
        mMonths = 3;

        mVipMonth1View.setOnClickListener(this);
        mVipMonth3View.setOnClickListener(this);
        mVipMonth12View.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        mVipinviteImages.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (super.isLogin()) {
            Glide.with(this).load(ApplicationData.mDiUserInfo.getHead()).into(mHeadImageView);
            mUserNameTextView.setText(ApplicationData.mDiUserInfo.getName());

            // 会员等级
            if (super.isVIP()) {
                mUserVipTimeTextView.setVisibility(View.VISIBLE);
                mUserVipTimeTextView.setText(getString(R.string.vip__user_vip_time) + ApplicationData.mDiUserInfo.getVipendtimeStr());
                registerBtn.setText(R.string.vip_button_fee);
            } else {
                mUserVipTimeTextView.setVisibility(View.GONE);
                registerBtn.setText(R.string.vip_button);
            }
        } else {
            Glide.with(this).load(R.mipmap.userhead).into(mHeadImageView);
            mUserNameTextView.setText(getString(R.string.profile_login));

            // 会员等级
            mUserVipTimeTextView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_name_head_view:

                break;
            case R.id.m_vip_month_1_view:
                chooseMonth(1);
                break;
            case R.id.m_vip_month_3_view:
                chooseMonth(3);
                break;
            case R.id.m_vip_month_12_view:
                chooseMonth(12);
                break;
            case R.id.m_save_btn:
                saveVip();
                break;
            case R.id.m_vip_invite_images:
                Intent intent = new Intent(this, VipInviteActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void saveVip() {
        UserVipInfo userVipInfo = new UserVipInfo();
        userVipInfo.setUserid(ApplicationData.mDiUserInfo.getId());
        userVipInfo.setMonths(mMonths);
        userVipInfo.setType(CommonConstants.VIP_TYPE_BUY);
        userVipInfo.setPayaccount(mPayAccount);
        userVipInfo.setPaytype(CommonConstants.PAY_TYPE_ALIPAY);

        //订单
        SaDataProccessHandler<Void, Void, AlipayModel> dataVerHandler = new SaDataProccessHandler<Void, Void, AlipayModel>(this) {
            @Override
            public void onSuccess(AlipayModel data) {
                //支付
                final Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(VipInfoActivity.this);
                        Map<String, String> result = alipay.payV2(data.getOrderParam(), true);
                        Log.i("msp", result.toString());

                        android.os.Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }
        };
        VipInfoService.getInstance().getVipOrderInfo(userVipInfo, dataVerHandler);
    }

    private void chooseMonth(int months) {
        switch (months) {
            case 1:
                mVipMonth1View.setBackgroundResource(R.drawable.vip_black_line_shape);
                mVipMonth3View.setBackgroundResource(R.drawable.vip_gray_line_shape);
                mVipMonth12View.setBackgroundResource(R.drawable.vip_gray_line_shape);
                mVipMonth1ImageView.setVisibility(View.VISIBLE);
                mVipMonth3ImageView.setVisibility(View.GONE);
                mVipMonth12ImageView.setVisibility(View.GONE);
                mPayAccount = 6.8;
                mMonths = 1;
                break;
            case 3:
                mVipMonth1View.setBackgroundResource(R.drawable.vip_gray_line_shape);
                mVipMonth3View.setBackgroundResource(R.drawable.vip_black_line_shape);
                mVipMonth12View.setBackgroundResource(R.drawable.vip_gray_line_shape);
                mVipMonth1ImageView.setVisibility(View.GONE);
                mVipMonth3ImageView.setVisibility(View.VISIBLE);
                mVipMonth12ImageView.setVisibility(View.GONE);
                mPayAccount = 18.8;
                mMonths = 3;
                break;
            case 12:
                mVipMonth1View.setBackgroundResource(R.drawable.vip_gray_line_shape);
                mVipMonth3View.setBackgroundResource(R.drawable.vip_gray_line_shape);
                mVipMonth12View.setBackgroundResource(R.drawable.vip_black_line_shape);
                mVipMonth1ImageView.setVisibility(View.GONE);
                mVipMonth3ImageView.setVisibility(View.GONE);
                mVipMonth12ImageView.setVisibility(View.VISIBLE);
                mPayAccount = 68.8;
                mMonths = 12;
                break;
        }
    }
}
