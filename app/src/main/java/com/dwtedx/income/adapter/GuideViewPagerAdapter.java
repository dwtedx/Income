/**   
 * @Title: GuideViewPagerAdapter.java 
 * @Package com.jsl.gt.qhstudent.adapter 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author qinyl http://dwtedx.com 
 * @date 2015年6月30日 下午7:24:49 
 * @version V1.0   
 */
package com.dwtedx.income.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dwtedx.income.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: GuideViewPagerAdapter
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author qinyl http://dwtedx.com
 * @date 2015年6月30日 下午7:24:49
 * 
 */
public class GuideViewPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragmentList;
	private Context context;

	public GuideViewPagerAdapter(FragmentManager fm) {
		super(fm);
		this.context = context;
		//将fragment装进列表中
		mFragmentList = new ArrayList<>();
		mFragmentList.add(new ImageFragment0());
		mFragmentList.add(new ImageFragment1());
		mFragmentList.add(new ImageFragment2());
		mFragmentList.add(new ImageFragment3());

	}

	@Override
	public Fragment getItem(int position) {
		return mFragmentList.get(position);
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
	}

	//此方法用来显示tab上的名字
	@Override
	public CharSequence getPageTitle(int position) {
		return null;
	}

}
