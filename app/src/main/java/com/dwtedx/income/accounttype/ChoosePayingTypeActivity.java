package com.dwtedx.income.accounttype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.entity.IdInfo;
import com.dwtedx.income.profile.LoginV2Activity;
import com.dwtedx.income.accounttype.adapter.ChoosePayRecyclerViewAdatper;
import com.dwtedx.income.service.IncomeService;
import com.dwtedx.income.sqliteservice.DITypeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.AppTitleBar;

import java.util.List;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/26 下午4:51.
 * Company 路之遥网络科技有限公司
 * Description 帐户管理
 */
public class ChoosePayingTypeActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener {
    private AppTitleBar mAppTitleBar;
    private RecyclerView mRecyclerView;
    private List<DiType> mDiTypeList;
    private ChoosePayRecyclerViewAdatper mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_paying_type);
        mAppTitleBar = (AppTitleBar) findViewById(R.id.app_title);
        mAppTitleBar.setOnTitleClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAdd();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, getSpanCount()));//设置RecyclerView的布局管理
        //mRecyclerView.addItemDecoration();//设置RecyclerView中item的分割线，用的少，一般都用在item中设置margin分隔两个item
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置item的添加删除动画，采用默认的动画效果

        mDiTypeList = DITypeService.getInstance(this).findAll(CommonConstants.INCOME_ROLE_PAYING);
        mAdapter = new ChoosePayRecyclerViewAdatper(this, mDiTypeList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                showAdd();
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

    private void showAdd() {
        if (!isLogin()) {
            startActivity(new Intent(ChoosePayingTypeActivity.this, LoginV2Activity.class));
            return;
        }
        new MaterialDialog.Builder(ChoosePayingTypeActivity.this)
                .title(R.string.profile_type_add)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(2, 4)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .input("自定义类别名称", null, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        addType(input.toString());
                    }
                }).show();
    }

    private void addType(final String typeName) {
        DiType diType = new DiType(ApplicationData.mDiUserInfo.getName(), ApplicationData.mDiUserInfo.getId(), typeName,
                CommonConstants.INCOME_ROLE_PAYING, CommonConstants.PAYTYPE_DIY_ICON, CommonConstants.PAYTYPE_DIY_COLOR,
                100, ApplicationData.mDiUserInfo.getName() + "自定义", CommonUtility.getCurrentTime(), CommonUtility.getCurrentTime(), 0, CommonConstants.DELETEFALAG_NOTDELETE);

        SaDataProccessHandler<Void, Void, IdInfo> dataVerHandler = new
                SaDataProccessHandler<Void, Void, IdInfo>(ChoosePayingTypeActivity.this) {
                    @Override
                    public void onSuccess(IdInfo data) {
                        DiType diType1 = new DiType(ApplicationData.mDiUserInfo.getName(), ApplicationData.mDiUserInfo.getId(), typeName,
                                CommonConstants.INCOME_ROLE_PAYING, CommonConstants.PAYTYPE_DIY_ICON, CommonConstants.PAYTYPE_DIY_COLOR,
                                100, ApplicationData.mDiUserInfo.getName() + "自定义", CommonUtility.getCurrentTime(), CommonUtility.getCurrentTime(), data.getId(), CommonConstants.DELETEFALAG_NOTDELETE);

                        DITypeService.getInstance(ChoosePayingTypeActivity.this).save(diType1);
                        mDiTypeList.clear();
                        mDiTypeList.addAll(DITypeService.getInstance(ChoosePayingTypeActivity.this).findAll(CommonConstants.INCOME_ROLE_PAYING));
                        mAdapter.notifyDataSetChanged();
                        Snackbar.make(findViewById(R.id.app_title), getString(R.string.profile_type_add_sess), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                };
        IncomeService.getInstance().addType(diType, dataVerHandler);
    }

}
