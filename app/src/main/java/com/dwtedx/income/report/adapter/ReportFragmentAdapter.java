package com.dwtedx.income.report.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.report.ReportIncomeBarFragment;
import com.dwtedx.income.report.ReportIncomePieFragment;
import com.dwtedx.income.report.ReportPayingLineFragment;
import com.dwtedx.income.report.ReportPayingPieFragment;

import java.util.ArrayList;
import java.util.List;

public class ReportFragmentAdapter extends FragmentPagerAdapter {
    private int[] mTitleList = {R.string.report_paying_pie_title, R.string.report_paying_line_title, R.string.report_income_pie_title, R.string.report_income_bar_title};
    private List<Fragment> mFragments;
    private Context context;

    public ReportFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        //将fragment装进列表中
        mFragments = new ArrayList<>();
        mFragments.add(new ReportPayingPieFragment());
        mFragments.add(new ReportPayingLineFragment());
        mFragments.add(new ReportIncomePieFragment());
        mFragments.add(new ReportIncomeBarFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mTitleList.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(mTitleList[position]);
    }

    public View getTabView(int position){
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_reprrt_tab, null);
        TextView tv= (TextView) view.findViewById(R.id.textView);
        tv.setText(getPageTitle(position));
        return view;
    }

}
