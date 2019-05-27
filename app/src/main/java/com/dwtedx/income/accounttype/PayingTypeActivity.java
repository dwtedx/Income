package com.dwtedx.income.accounttype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dwtedx.income.R;
import com.dwtedx.income.accounttype.adapter.IncomeProfileRecyclerViewAdatper;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.helper.MyItemTouchCallback;
import com.dwtedx.income.helper.OnRecyclerItemClickListener;
import com.dwtedx.income.profile.LoginV2Activity;
import com.dwtedx.income.accounttype.adapter.PayProfileRecyclerViewAdatper;
import com.dwtedx.income.provider.PayingTypeTipSharedPreferences;
import com.dwtedx.income.sqliteservice.DITypeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.VibratorUtil;
import com.dwtedx.income.widget.AppTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/26 下午4:51.
 * Company 路之遥网络科技有限公司
 * Description 帐户管理
 */
public class PayingTypeActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, MyItemTouchCallback.OnDragListener  {
    private AppTitleBar mAppTitleBar;
    private RecyclerView mRecyclerView;
    private List<DiType> mDiTypeList;
    private PayProfileRecyclerViewAdatper mAdapter;
    private boolean isShowAdd;

    //拖拽
    private ItemTouchHelper itemTouchHelper;

    private Button mTipButton;
    private RelativeLayout mTipLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paying_type);
        mAppTitleBar = (AppTitleBar) findViewById(R.id.app_title);
        mAppTitleBar.setOnTitleClickListener(this);


        isShowAdd = getIntent().getBooleanExtra("isShowAdd", false);
        if (isShowAdd) {
            showAdd();
        }

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

        mDiTypeList = new ArrayList<>();
        mAdapter = new PayProfileRecyclerViewAdatper(this, mDiTypeList);
        mRecyclerView.setAdapter(mAdapter);

        //拖拽功能
        itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(mAdapter).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView){
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                itemTouchHelper.startDrag(vh);
                VibratorUtil.Vibrate(PayingTypeActivity.this, 70);   //震动70ms
            }
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                final int postion = vh.getLayoutPosition();
                final DiType mDiType = mDiTypeList.get(postion);
                if (mDiType.getUserid() != 0) {
                    if (!isLogin()) {
                        startActivity(new Intent(PayingTypeActivity.this, LoginV2Activity.class));
                        return;
                    }
                    Intent intent = new Intent(PayingTypeActivity.this, AddAccountTypeActivity.class);
                    intent.putExtra("TYPEROLE",CommonConstants.INCOME_ROLE_PAYING);
                    intent.putExtra("TYPEID",mDiType.getId());
                    startActivity(intent);
                }else{
                    Snackbar.make(findViewById(R.id.app_title), getString(R.string.profile_type_tip), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        //是否有提示
        PayingTypeTipSharedPreferences.init(this);
        if(PayingTypeTipSharedPreferences.getIsTip()){
            mTipLayout = (RelativeLayout) findViewById(R.id.tip_layout);
            mTipLayout.setVisibility(View.VISIBLE);
            mTipButton = (Button) findViewById(R.id.tip_button);
            mTipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTipLayout.setVisibility(View.GONE);
                    PayingTypeTipSharedPreferences.setIsTip(ApplicationData.mAppVersionCode);
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mDiTypeList.clear();
        mDiTypeList.addAll(DITypeService.getInstance(this).findAll(CommonConstants.INCOME_ROLE_PAYING));
        mAdapter.notifyDataSetChanged();
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
            startActivity(new Intent(PayingTypeActivity.this, LoginV2Activity.class));
            return;
        }
        Intent intent = new Intent(PayingTypeActivity.this, AddAccountTypeActivity.class);
        intent.putExtra("TYPEROLE",CommonConstants.INCOME_ROLE_PAYING);
        intent.putExtra("TYPEID",-1);
        startActivity(intent);
    }

    @Override
    public void onFinishDrag() {
        for(int i = 0; i < mDiTypeList.size() ; i++){
            mDiTypeList.get(i).setSequence(i);
            DITypeService.getInstance(PayingTypeActivity.this).update(mDiTypeList.get(i));
        }

    }
}
