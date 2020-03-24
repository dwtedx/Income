package com.dwtedx.income.vip;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.DiUserinviteinfo;
import com.dwtedx.income.service.VipInfoService;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.vip.adapter.VipInviteRecyclerAdapter;
import com.dwtedx.income.widget.RecycleViewDivider;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VipInviteActivity extends BaseActivity implements View.OnClickListener, VipInviteRecyclerAdapter.OnInviteButtonClick, TabLayout.OnTabSelectedListener {

    @BindView(R.id.m_title_back_btn)
    LinearLayout mTitleBackBtn;
    @BindView(R.id.m_tabs)
    TabLayout mTabs;
    @BindView(R.id.m_recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.m_vip_invite_phone_view)
    EditText mVipInvitePhoneView;
    @BindView(R.id.m_vip_invite_btn)
    Button mVipInviteBtn;
    @BindView(R.id.m_nodata_relative_layout)
    RelativeLayout mNodataRelativeLayout;

    List<DiUserinviteinfo> mDiUserinviteinfoList;
    VipInviteRecyclerAdapter mAdapter;

    int status;

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
        mAdapter = new VipInviteRecyclerAdapter(this, mDiUserinviteinfoList);
        mAdapter.setmOnInviteButtonClick(this);
        mRecyclerview.setNestedScrollingEnabled(false);//禁止滑动
        mRecyclerview.setAdapter(mAdapter);

        mTabs.addOnTabSelectedListener(this);

        mTitleBackBtn.setOnClickListener(this);
        mVipInviteBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInvite();
    }

    private void getUserInvite() {
        mDiUserinviteinfoList.clear();
        SaDataProccessHandler<Void, Void, List<DiUserinviteinfo>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<DiUserinviteinfo>>(this) {
            @Override
            public void onSuccess(List<DiUserinviteinfo> data) {
                mDiUserinviteinfoList.addAll(data);
                if (mDiUserinviteinfoList.size() == 0) {
                    mNodataRelativeLayout.setVisibility(View.VISIBLE);
                    mRecyclerview.setVisibility(View.GONE);
                } else {
                    mNodataRelativeLayout.setVisibility(View.GONE);
                    mRecyclerview.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                }
            }
        };

        VipInfoService.getInstance().getUserInvite(status, dataVerHandler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_title_back_btn:
                finish();
                break;

            case R.id.m_vip_invite_btn:
                save();
                break;
        }
    }

    @Override
    public void OnInviteClick(View v) {
        openShare();
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int tabIndex = mTabs.getSelectedTabPosition();
        switch (tabIndex){
            case 0:
                status = 0;
                break;

            case 1:
                status = 2;
                break;
        }
        getUserInvite();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    private void save() {
        String phone = mVipInvitePhoneView.getText().toString();
        if (CommonUtility.isEmpty(phone)) {
            Toast.makeText(this, R.string.vip_invite_phone, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!CommonUtility.isPhoneNumberValid(phone)) {
            Toast.makeText(this, R.string.vip_invite_phone_err, Toast.LENGTH_SHORT).show();
            return;
        }

        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(this) {
            @Override
            public void onSuccess(Void data) {
                openShare();
            }
        };
        VipInfoService.getInstance().saveInvite(phone, dataVerHandler);

    }

    // 用来配置各个平台的SDKF
    private void openShare() {
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.ALIPAY, SHARE_MEDIA.EMAIL, SHARE_MEDIA.SMS};

        String shareContent = "我在使用DD记账、记录生活中的每一笔开支、还有很丰富的报表、感觉真的很不错、特此推荐给您 http://income.dwtedx.com/";
        UMWeb web = new UMWeb("http://income.dwtedx.com");
        web.setTitle("DD记账、记录生活点点滴滴");//标题
        web.setThumb(new UMImage(VipInviteActivity.this, R.mipmap.ic_launcher));//缩略图
        web.setDescription(shareContent);//描述

        new ShareAction(VipInviteActivity.this)
                .setDisplayList(displaylist)
                .withText(shareContent)
                .withMedia(web)
                .setCallback(umShareListener)
                .open();

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Toast.makeText(VipInviteActivity.this, share_media.toString() + "正在分享...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(VipInviteActivity.this, share_media.toString() + "分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(VipInviteActivity.this, share_media.toString() + "分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(VipInviteActivity.this, share_media.toString() + "分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

}
