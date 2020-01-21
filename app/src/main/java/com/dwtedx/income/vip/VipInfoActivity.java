package com.dwtedx.income.vip;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.profile.SetupActivity;
import com.dwtedx.income.widget.AppTitleBar;

import java.io.File;

import androidx.core.content.ContextCompat;

public class VipInfoActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_info);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.common_body_color));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_name_head_view:

                break;
        }
    }
}
