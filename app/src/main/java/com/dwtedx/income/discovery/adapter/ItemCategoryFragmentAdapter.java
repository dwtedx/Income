package com.dwtedx.income.discovery.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.discovery.ChildrenFragment;
import com.dwtedx.income.discovery.DigitalFragment;
import com.dwtedx.income.discovery.LiveHomeFragment;
import com.dwtedx.income.discovery.LuggageFragment;
import com.dwtedx.income.discovery.MakeupsFragment;
import com.dwtedx.income.discovery.ManClothingFragment;
import com.dwtedx.income.discovery.MotherBabyFragment;
import com.dwtedx.income.discovery.OtherFragment;
import com.dwtedx.income.discovery.SnacksFragment;
import com.dwtedx.income.discovery.SpecialOfferFragment;
import com.dwtedx.income.discovery.UnderwearFragment;
import com.dwtedx.income.discovery.WomensClothingFragment;

import java.util.ArrayList;
import java.util.List;

public class ItemCategoryFragmentAdapter extends FragmentPagerAdapter {
    private int[] mTitleList = {R.string.discovery_special_offer, R.string.discovery_womens_clothing,
            R.string.discovery_snacks, R.string.discovery_category_digital, R.string.discovery_man_clothing,
            R.string.discovery_makeups, R.string.discovery_category_underwear, R.string.discovery_category_motherbaby, R.string.discovery_category_children,
            R.string.discovery_category_home, R.string.discovery_category_luggage, R.string.discovery_category_other};
    private List<Fragment> mFragments;
    private Context context;

    public ItemCategoryFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        //将fragment装进列表中
        mFragments = new ArrayList<>();
        mFragments.add(new SpecialOfferFragment());
        mFragments.add(new WomensClothingFragment());
        mFragments.add(new SnacksFragment());
        mFragments.add(new DigitalFragment());
        mFragments.add(new ManClothingFragment());
        mFragments.add(new MakeupsFragment());
        mFragments.add(new UnderwearFragment());
        mFragments.add(new MotherBabyFragment());
        mFragments.add(new ChildrenFragment());
        mFragments.add(new LiveHomeFragment());
        mFragments.add(new LuggageFragment());
        mFragments.add(new OtherFragment());
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
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_cate_tab, null);
        TextView tv= (TextView) view.findViewById(R.id.textView);
        tv.setText(getPageTitle(position));
        return view;
    }

}
