package com.dwtedx.income.accounttype;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.dwtedx.income.R;
import com.dwtedx.income.account.adapter.ChooseAccountImageRecyclerViewAdatper;
import com.dwtedx.income.accounttype.adapter.ChooseAccountTypeImageRecyclerViewAdatper;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.widget.AppTitleBar;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/26 下午4:51.
 * Company 路之遥网络科技有限公司
 * Description 帐户管理
 */
public class ChooseAccountTypeImageActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener {
    private AppTitleBar mAppTitleBar;
    private RecyclerView mRecyclerView;
    private ChooseAccountTypeImageRecyclerViewAdatper mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_account_image);
        mAppTitleBar = (AppTitleBar) findViewById(R.id.app_title);
        mAppTitleBar.setOnTitleClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, getSpanCount()));//设置RecyclerView的布局管理
        //mRecyclerView.addItemDecoration();//设置RecyclerView中item的分割线，用的少，一般都用在item中设置margin分隔两个item
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置item的添加删除动画，采用默认的动画效果

        mAdapter = new ChooseAccountTypeImageRecyclerViewAdatper(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
        }
    }

    /**
     * 获取屏幕分辨率
     * 相应的列数
     */
    private int getSpanCount() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float density = displayMetrics.density; //得到密度
        float width = displayMetrics.widthPixels;//得到宽度
        float height = displayMetrics.heightPixels;//得到高度
        Log.i(CommonConstants.INCOME_TAG, "density" + density);
        Log.i(CommonConstants.INCOME_TAG, "width" + width);
        Log.i(CommonConstants.INCOME_TAG, "height" + height);
        if(width < 720){
            return 4;
        }
        return 5;
    }

}
