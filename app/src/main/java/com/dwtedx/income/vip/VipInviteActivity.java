package com.dwtedx.income.vip;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.entity.DiExpexcel;
import com.dwtedx.income.entity.DiUserinviteinfo;
import com.dwtedx.income.expexcel.adapter.ExpExcelRecyclerAdapter;
import com.dwtedx.income.vip.adapter.VipInviteRecyclerAdapter;
import com.dwtedx.income.widget.RecycleViewDivider;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VipInviteActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.m_title_back_btn)
    LinearLayout mTitleBackBtn;
    @BindView(R.id.m_tabs)
    TabLayout mTabs;
    @BindView(R.id.m_recyclerview)
    RecyclerView mRecyclerview;

    List<DiUserinviteinfo> mDiUserinviteinfoList;
    VipInviteRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_invite_info);
        ButterKnife.bind(this);

        //状态栏与背景图完美沉浸
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, R.color.common_division_line));

        mDiUserinviteinfoList = new ArrayList<DiUserinviteinfo>();
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());
        mDiUserinviteinfoList.add(new DiUserinviteinfo());

        mAdapter = new VipInviteRecyclerAdapter(this, mDiUserinviteinfoList);
        mRecyclerview.setNestedScrollingEnabled(false);//禁止滑动
        mRecyclerview.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_name_head_view:

                break;
        }
    }

}
