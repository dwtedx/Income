package com.dwtedx.income.home.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dwtedx.income.R;
import com.dwtedx.income.report.HomeReportPayingPieFragment;
import com.dwtedx.income.home.IncomeLineFragment;
import com.dwtedx.income.profile.ProfileFragment;
import com.dwtedx.income.topic.TopicFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A150189 on 2016/5/27.
 */
public class HomeFragmetAdapter extends FragmentPagerAdapter {
    private int[] mTitleList = {R.string.nav_moneyv2, R.string.nav_report, R.string.nav_discovery, R.string.nav_profilev2};
    private List<Fragment> mFragmentList;
    private Context context;

    public HomeFragmetAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        //将fragment装进列表中 
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new IncomeLineFragment());
        mFragmentList.add(new HomeReportPayingPieFragment());
        mFragmentList.add(new TopicFragment());
        mFragmentList.add(new ProfileFragment());

    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mTitleList.length;
    }

    //此方法用来显示tab上的名字  
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(mTitleList[position]);
    }
}
