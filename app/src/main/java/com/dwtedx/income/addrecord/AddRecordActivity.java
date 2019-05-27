package com.dwtedx.income.addrecord;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dwtedx.income.R;
import com.dwtedx.income.addrecord.adapter.RecordFragmentAdapter;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.provider.AddTipSharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class AddRecordActivity extends BaseActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private Button mAddTipButton;
    private RelativeLayout mAddTipLayout;

    private BaseFragment mPayingFragment;
    private BaseFragment mIncomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        ImageView imageView = (ImageView)findViewById(R.id.tabs_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRecordActivity.this.finish();
            }
        });

        //是否有提示
        AddTipSharedPreferences.init(this);
        if(AddTipSharedPreferences.getIsTip()){
            mAddTipLayout = (RelativeLayout) findViewById(R.id.add_tip_layout);
            mAddTipLayout.setVisibility(View.VISIBLE);
            mAddTipButton = (Button) findViewById(R.id.add_tip_button);
            mAddTipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAddTipLayout.setVisibility(View.GONE);
                    AddTipSharedPreferences.setIsTip(ApplicationData.mAppVersionCode);
                }
            });
        }

        mPayingFragment = new PayingFragment();
        mIncomeFragment = new IncomeFragment();
        setupViewPager();
    }


    private void setupViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(mPayingFragment);
        fragments.add(mIncomeFragment);
        RecordFragmentAdapter adapter = new RecordFragmentAdapter(getSupportFragmentManager(), this, fragments);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
        //mTabLayout.setTabsFromPagerAdapter(adapter);
    }

}
