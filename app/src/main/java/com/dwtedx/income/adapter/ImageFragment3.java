package com.dwtedx.income.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.dwtedx.income.R;
import com.dwtedx.income.account.AccountListActivity;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.home.HomeV2Activity;
import com.dwtedx.income.home.adapter.DiAccountAdapter;
import com.dwtedx.income.sqliteservice.DIAccountService;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/16.
 * Company 路之遥网络科技有限公司
 * Description TODO(这里用一句话描述这个类的作用)
 */
public class ImageFragment3 extends BaseFragment {

    ImageView mImageView;
    View mView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_image, null, false);
        mImageView = mView.findViewById(R.id.guide_image_view);
        mImageView.setImageResource(R.mipmap.guide3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mView;
    }

    public void setImageResource(int resourceId){
        mImageView.setImageResource(resourceId);
    }

    public ImageView getmImageView() {
        return mImageView;
    }
}
