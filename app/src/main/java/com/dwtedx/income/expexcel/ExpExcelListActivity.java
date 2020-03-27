package com.dwtedx.income.expexcel;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.DiExpexcel;
import com.dwtedx.income.entity.DiTopic;
import com.dwtedx.income.expexcel.adapter.ExpExcelRecyclerAdapter;
import com.dwtedx.income.service.ExpExcelService;
import com.dwtedx.income.service.TopicService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.RecycleViewDivider;
import com.dwtedx.income.widget.springheader.AliFooter;
import com.dwtedx.income.widget.springheader.AliHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ExpExcelListActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, SpringView.OnFreshListener {

    @BindView(R.id.m_app_title)
    AppTitleBar mAppTitle;
    @BindView(R.id.m_recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.m_springview)
    SpringView mSpringView;
    @BindView(R.id.m_excel_no_data)
    TextView mExcelNoData;

    List<DiExpexcel> mDiExpexcel;
    ExpExcelRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_excel_list);
        ButterKnife.bind(this);

        mAppTitle.setOnTitleClickListener(this);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, R.color.common_division_line));
        mDiExpexcel = new ArrayList<DiExpexcel>();
        mAdapter = new ExpExcelRecyclerAdapter(this, mDiExpexcel);
        mRecyclerview.setAdapter(mAdapter);

        mSpringView.setListener(this);
        mSpringView.setHeader(new AliHeader(this, R.mipmap.spring_ali, true));   //参数为：logo图片资源，是否显示文字
        mSpringView.setFooter(new AliFooter(this, false));
        mSpringView.setType(SpringView.Type.OVERLAP);

        getExpExcelInfo(true, true);

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
    public void onRefresh() {
        getExpExcelInfo(false, true);
    }

    @Override
    public void onLoadmore() {
        getExpExcelInfo(false, false);
    }

    private void getExpExcelInfo(final boolean isShow, final boolean isClear) {

        SaDataProccessHandler<Void, Void, List<DiExpexcel>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<DiExpexcel>>(this) {
            @Override
            public void onSuccess(List<DiExpexcel> data) {
                //第一次请求才缓存
                if (isClear) {
                    mDiExpexcel.clear();
                }
                mDiExpexcel.addAll(data);
                mAdapter.notifyDataSetChanged();
                mSpringView.onFinishFreshAndLoad();
                //显示空态
                if(mDiExpexcel.size() > 0) {
                    mExcelNoData.setVisibility(View.GONE);
                }else{
                    mExcelNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPreExecute() {
                if (isShow) {
                    super.onPreExecute();
                }
            }
        };
        ExpExcelService.getInstance().getMyExpExcelInfo(isClear ? 0 : mDiExpexcel.size(), CommonConstants.PAGE_LENGTH_NUMBER, dataVerHandler);
    }

}
