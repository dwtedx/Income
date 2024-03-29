package com.dwtedx.income.addrecord.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwtedx.income.R;

import java.util.List;

public class RecordFragmentAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private int[] tabTitles = {R.string.record_pay, R.string.record_income};
    private int[] imageResId = {R.mipmap.pay_new, R.mipmap.income_new};
    private List<Fragment> mFragments;
    private Context context;

    public RecordFragmentAdapter(FragmentManager fm, Context context, List<Fragment> fragments) {
        super(fm);
        this.context = context;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //第一次的代码
        return String.valueOf(tabTitles[position]);
        //第二次的代码
//         Drawable image = context.getResources().getDrawable(imageResId[position]);
//         image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//         SpannableString sb = new SpannableString(String.valueOf(tabTitles[position]));
//         ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//         sb.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//         return sb;

    }

    public View getTabView(int position){
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_record_tab, null);
        TextView tv= (TextView) view.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setImageResource(imageResId[position]);
        return view;
    }
}
