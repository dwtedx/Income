package com.dwtedx.income.vip;

import android.os.Bundle;
import android.view.View;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import butterknife.ButterKnife;

public class VipInviteActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_invite_info);
        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_name_head_view:

                break;
        }
    }

}
