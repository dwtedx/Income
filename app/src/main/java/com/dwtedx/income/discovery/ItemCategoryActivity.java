package com.dwtedx.income.discovery;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.discovery.adapter.ItemCategoryFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ItemCategoryActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.m_tabs_back)
    ImageView mTabsBack;
    @BindView(R.id.m_tab_layouts)
    TabLayout mTabLayout;
    @BindView(R.id.m_viewpager)
    ViewPager mViewPager;

    ItemCategoryFragmentAdapter mItemCategoryFragmentAdapter;
    int chose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_category);
        ButterKnife.bind(this);

        chose = getIntent().getIntExtra("cheose", 0);

        mItemCategoryFragmentAdapter = new ItemCategoryFragmentAdapter(getSupportFragmentManager(), this);
        //viewpager加载adapter
        mViewPager.setAdapter(mItemCategoryFragmentAdapter);
        //TabLayout加载viewpager
        mTabLayout.setupWithViewPager(mViewPager);
        //设置TabLayout的模式
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(mItemCategoryFragmentAdapter.getTabView(i));
        }

        mViewPager.setCurrentItem(chose);
        mTabLayout.getTabAt(chose).select();

        mTabsBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.m_tabs_back:
                finish();
                break;
        }
    }

}
