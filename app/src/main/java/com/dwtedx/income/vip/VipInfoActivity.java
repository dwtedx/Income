package com.dwtedx.income.vip;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_name_head_view:

                break;
        }
    }
}
