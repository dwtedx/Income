package com.dwtedx.income.report;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.report.adapter.ReportFragmentAdapter;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/8/19.
 * Company DD博客
 * Description TODO(这里用一句话描述这个类的作用)
 */
public class ReportActivity extends BaseActivity implements View.OnClickListener{

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ReportFragmentAdapter mReportFragmentAdapter;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tablayouts);
        mReportFragmentAdapter = new ReportFragmentAdapter(getSupportFragmentManager(), this);
        //viewpager加载adapter
        mViewPager.setAdapter(mReportFragmentAdapter);
        //TabLayout加载viewpager
        mTabLayout.setupWithViewPager(mViewPager);
        //设置TabLayout的模式
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(mReportFragmentAdapter.getTabView(i));
        }

        imageView = (ImageView)findViewById(R.id.tabs_back);
        imageView.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tabs_back:
                ReportActivity.this.finish();
                break;
        }
    }
}
