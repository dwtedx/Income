package com.dwtedx.income.expexcel;

import android.os.Bundle;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.DiExpexcel;
import com.dwtedx.income.entity.DiTopic;
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

    List<DiExpexcel> mDiExpexcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_excel_list);
        ButterKnife.bind(this);

        mAppTitle.setOnTitleClickListener(this);

        mDiExpexcel = new ArrayList<DiExpexcel>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, 1, R.color.common_division_line));

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

    }

    @Override
    public void onLoadmore() {

    }

    private void getExpExcelInfo(final boolean isShow, final boolean isClear) {

        SaDataProccessHandler<Void, Void, List<DiTopic>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<DiTopic>>(this) {
            @Override
            public void onSuccess(List<DiTopic> data) {


            }

            @Override
            public void onPreExecute() {
                if (isShow) {
                    super.onPreExecute();
                }
            }

            @Override
            public void handlerError(SaException e) {
                super.handlerError(e);
            }
        };

        TopicService.getInstance().getMyTopicinfo(isClear ? 0 : mDiExpexcel.size(), CommonConstants.PAGE_LENGTH_NUMBER, dataVerHandler);
    }

}
