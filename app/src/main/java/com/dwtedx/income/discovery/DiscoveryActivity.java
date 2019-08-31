package com.dwtedx.income.discovery;

import android.os.Bundle;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;


public class DiscoveryActivity extends BaseActivity {

    DiscoveryFragment mDiscoveryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        mDiscoveryFragment = new DiscoveryFragment();

        // 具体的fragment切换逻辑可以根据应用调整，例如使用show()/hide()
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mDiscoveryFragment).commit();

    }


}
