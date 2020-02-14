package com.dwtedx.income.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.fragment.app.Fragment;

import com.dwtedx.income.broadcast.CommonBroadcast;
import com.dwtedx.income.entity.ApplicationData;
import com.umeng.analytics.MobclickAgent;

/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 上午9:01.
 * @Company 路之遥网络科技有限公司
 * @Description 应用程序中Activity的基类，用于定义Activity共有方法
 */
public abstract class BaseFragment extends Fragment {

    // 写一个广播的内部类，
    private BroadcastReceiver mProadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceive(intent.getIntExtra(CommonBroadcast.BROADCAST_ACTION_TYPE, -1), intent);
        }
    };

    public BaseActivity mFragmentContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //缓存context
        mFragmentContext = (BaseActivity)context;
        // 在当前的activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(CommonBroadcast.BROADCAST_ACTION);
        context.registerReceiver(mProadcastReceiver, filter);//注册
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(mProadcastReceiver);
        super.onDestroy();
    }

	@Override
    public void onResume() {
        super.onResume();
        //统计页面，"MainScreen"为页面名称，可自定义
        MobclickAgent.onPageStart(getActivity().getLocalClassName());
    }
    
    @Override
    public void onPause() {
        super.onPause();
        //友盟统计页面
        MobclickAgent.onPageEnd(getActivity().getLocalClassName());
    }

    protected void onBroadcastReceive(int type, Intent intent) {

    }

    public boolean isLogin(){
        if(null != ApplicationData.mDiUserInfo && ApplicationData.mDiUserInfo.getId() > 0){
            return true;
        }
        return false;
    }

    public boolean isVIP(){
        if(isLogin()){
            if(ApplicationData.mDiUserInfo.getVipflag() == 1){
                return true;
            }
        }
        return false;
    }
}
