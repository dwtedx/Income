package com.dwtedx.income.scan;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.scan.adapter.LocationRecyclerAdapter;
import com.dwtedx.income.utility.ParseJsonToObject;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.RecycleViewDivider;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/26 下午4:51.
 * Company 路之遥网络科技有限公司
 * Description 帐户管理
 */
public class ChooseLocationActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener {

    @BindView(R.id.m_app_title)
    AppTitleBar mAppTitle;
    @BindView(R.id.m_recycler_view)
    RecyclerView mRecyclerView;

    //定位
    List<String> mPoiList;
    LocationRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        ButterKnife.bind(this);

        mAppTitle.setOnTitleClickListener(this);

        try {
            mPoiList = ParseJsonToObject.getObjectList(String.class, new JSONArray(getIntent().getStringExtra("POI")));
        } catch (JSONException e) {
            e.printStackTrace();
            mPoiList = new ArrayList<>();
        }

        mRecyclerView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//设置RecyclerView的布局管理
        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, R.color.common_division_line));//设置RecyclerView中item的分割线，用的少，一般都用在item中设置margin分隔两个item

        mAdapter = new LocationRecyclerAdapter(this, mPoiList);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
