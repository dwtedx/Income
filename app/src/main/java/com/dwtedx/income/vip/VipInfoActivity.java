package com.dwtedx.income.vip;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.widget.CircleImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_info);
        ButterKnife.bind(this);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.common_body_color));

        //添加删除线
        mVipMonth1DelTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mVipMonth3DelTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mVipMonth12DelTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        mVipMonth1View.setOnClickListener(this);
        mVipMonth3View.setOnClickListener(this);
        mVipMonth12View.setOnClickListener(this);
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
        }
    }

    private void chooseMonth(int months){
        switch (months){
            case 1:
                mVipMonth1View.setBackgroundResource(R.drawable.vip_black_line_shape);
                mVipMonth3View.setBackgroundResource(R.drawable.vip_gray_line_shape);
                mVipMonth12View.setBackgroundResource(R.drawable.vip_gray_line_shape);
                mVipMonth1ImageView.setVisibility(View.VISIBLE);
                mVipMonth3ImageView.setVisibility(View.GONE);
                mVipMonth12ImageView.setVisibility(View.GONE);
                break;
            case 3:
                mVipMonth1View.setBackgroundResource(R.drawable.vip_gray_line_shape);
                mVipMonth3View.setBackgroundResource(R.drawable.vip_black_line_shape);
                mVipMonth12View.setBackgroundResource(R.drawable.vip_gray_line_shape);
                mVipMonth1ImageView.setVisibility(View.GONE);
                mVipMonth3ImageView.setVisibility(View.VISIBLE);
                mVipMonth12ImageView.setVisibility(View.GONE);
                break;
            case 12:
                mVipMonth1View.setBackgroundResource(R.drawable.vip_gray_line_shape);
                mVipMonth3View.setBackgroundResource(R.drawable.vip_gray_line_shape);
                mVipMonth12View.setBackgroundResource(R.drawable.vip_black_line_shape);
                mVipMonth1ImageView.setVisibility(View.GONE);
                mVipMonth3ImageView.setVisibility(View.GONE);
                mVipMonth12ImageView.setVisibility(View.VISIBLE);
                break;
        }
    }
}
