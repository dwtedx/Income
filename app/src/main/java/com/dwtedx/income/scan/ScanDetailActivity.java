package com.dwtedx.income.scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.dwtedx.income.R;
import com.dwtedx.income.accounttype.ChoosePayingTypeActivity;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.entity.DiBudget;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.entity.DiScan;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.provider.AccountSharedPreferences;
import com.dwtedx.income.scan.adapter.ScanDetailAdapter;
import com.dwtedx.income.service.IncomeService;
import com.dwtedx.income.sqliteservice.DIAccountService;
import com.dwtedx.income.sqliteservice.DIBudgetService;
import com.dwtedx.income.sqliteservice.DIScanService;
import com.dwtedx.income.sqliteservice.DITypeService;
import com.dwtedx.income.sqliteservice.DlIncomeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.ParseJsonToObject;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.RecycleViewDivider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanDetailActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, View.OnClickListener, ScanDetailAdapter.OnValueEditAfterTextChangeListener {

    @BindView(R.id.m_app_title)
    AppTitleBar mAppTitle;
    @BindView(R.id.m_type_imageView)
    ImageView mTypeImageView;
    @BindView(R.id.m_type_textView)
    TextView mTypeTextView;
    @BindView(R.id.m_type_record_layout)
    LinearLayout mTypeRecordLayout;
    @BindView(R.id.m_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.m_record_account_text)
    EditText mRecordAccountText;
    @BindView(R.id.m_record_icon_location)
    TextView mRecordIconLocation;
    @BindView(R.id.m_scan_record_type_credit_card)
    TextView mScanRecordTypeCreditCard;

    //百度定位7.6相关
    public LocationClient mLocationClient;
    private MyLocationListener myListener;
    private List<String> mPoiList;

    //帐户类型
    private MaterialDialog mTypeMaterialDialog;
    private int mAccountID;

    private DiIncome mIncome;
    private List<DiScan> mDiScanList;

    private Calendar mCalendar;
    private double mAllMoneySum;
    private DiType diType;
    private static int REQUEST_CODE_CHOOSE_TYPE = 70;
    private static int REQUEST_CODE_CHOOSE_LOCAL = 71;
    private static int ACCESS_COARSE_LOCALHOST_REQUEST_CODE = 72;

    private ScanDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_detail);
        ButterKnife.bind(this);
        mAppTitle.setOnTitleClickListener(this);
        mTypeRecordLayout.setOnClickListener(this);

        try {
            mIncome = ParseJsonToObject.getObject(DiIncome.class, new JSONObject(getIntent().getExtras().getString("income")));
            mRecordAccountText.setText(CommonUtility.doubleFormat(mIncome.getMoneysum()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mCalendar = Calendar.getInstance();
        diType = DITypeService.getInstance(this).find(mIncome.getTypeid());//1表示一般支出
        setDiType();

        //账户设置
        AccountSharedPreferences.init(this);
        mAccountID = mIncome.getAccountid();
        mScanRecordTypeCreditCard.setText(mIncome.getAccount());
        mScanRecordTypeCreditCard.setOnClickListener(this);

        //定位
        if(!CommonUtility.isEmpty(mIncome.getLocation())) {
            mRecordIconLocation.setText(mIncome.getLocation());
        }
        mRecordIconLocation.setOnClickListener(this);
        initLocation();

        mDiScanList = DIScanService.getInstance(this).findByIncomeId(mIncome.getId());
        mDiScanList.add(new DiScan(CommonConstants.INCOME_SCAN_ADDBUTTON));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        //自定义分割线的样式
        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 0, ContextCompat.getColor(this, R.color.common_division_line)));
        mAdapter = new ScanDetailAdapter(this, mDiScanList);
        mAdapter.setmOnValueEditAfterTextChangeListener(this);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void setDiType() {
        mTypeImageView.setImageResource(CommonUtility.getImageIdByName(diType.getIcon()));
        mTypeImageView.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        mTypeTextView.setText(diType.getName());
        mTypeTextView.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                save();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_type_record_layout:
                Intent intent = new Intent(this, ChoosePayingTypeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CHOOSE_TYPE);
                break;

            case R.id.m_record_icon_location:
                mRecordAccountText.requestFocus();//获取焦点 光标出现
                intent = new Intent(this, ChooseLocationActivity.class);
                intent.putExtra("POI", ParseJsonToObject.getJsonFromObjList(mPoiList).toString());
                startActivityForResult(intent, REQUEST_CODE_CHOOSE_LOCAL);
                break;

            case R.id.m_scan_record_type_credit_card:
                mRecordAccountText.requestFocus();//获取焦点 光标出现
                showTypeDialog();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_TYPE) {
            try {
                diType = ParseJsonToObject.getObject(DiType.class, new JSONObject(data.getStringExtra("ditype")));
                setDiType();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_LOCAL) {
            mRecordIconLocation.setText(data.getStringExtra("POI"));
        }
    }

    @Override
    public void OnValueEditAfterTextChanged(String s) {
        mAllMoneySum = 0;
        for (int i = 0; i < mDiScanList.size(); i++) {
            mAllMoneySum += mDiScanList.get(i).getMoneysum();//计算总金额
        }
        mRecordAccountText.setText(CommonUtility.doubleFormat(mAllMoneySum));
    }

    private void save() {
        mRecordAccountText.requestFocus();//获取焦点 光标出现
        if(CommonUtility.isEmpty(mRecordAccountText.getText().toString())){
            //Toast.makeText(this, this.getString(R.string.record_money_error) , Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.app_title), R.string.record_money_error, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        Date beForTime = CommonUtility.stringToDate(DlIncomeService.getInstance(this).findBeForTime());
        if(null != beForTime && mCalendar.getTime().before(beForTime)){
            //Toast.makeText(this, this.getString(R.string.record_money_error_time) , Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.app_title), R.string.record_money_error_time, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }

        final String nowTime = CommonUtility.getCurrentTime();
        final double money = Double.parseDouble(mRecordAccountText.getText().toString());
        mRecordAccountText.setText("");
        final DiIncome income = new DiIncome(
                mIncome.getId(), mIncome.getUsername(), mIncome.getUserid(), mIncome.getRole(), money,
                diType.getName(), diType.getId(), mScanRecordTypeCreditCard.getText().toString(), mAccountID,
                mIncome.getRemark(), mRecordIconLocation.getText().toString(), mIncome.getVoicepath(), mIncome.getImagepath(), CommonConstants.INCOME_RECORD_TYPE_1, mIncome.getRecordtime(), mIncome.getCreatetime(), nowTime, mIncome.getIsupdate(), mIncome.getServerid(), mIncome.getDeletefalag());
        //添加扫单数据
        final List<DiScan> updateScan = new ArrayList<>();
        for (int i = 0; i < mDiScanList.size(); i++) {
            if(CommonConstants.INCOME_SCAN_ADDBUTTON != mDiScanList.get(i).getAddbutton()) {
                updateScan.add(mDiScanList.get(i));
            }
        }
        income.setScanList(updateScan);

        //同步服务器数据库
        if(mIncome.getIsupdate() != CommonConstants.INCOME_RECORD_NOT_UPDATE){
            //同步数据库
            SaDataProccessHandler<Void, Void, DiIncome> dataVerHandler = new SaDataProccessHandler<Void, Void, DiIncome>(ScanDetailActivity.this) {
                @Override
                public void onSuccess(DiIncome data) {
                    DlIncomeService.getInstance(ScanDetailActivity.this).update(income);
                    //scan扫描记录保存
                    DiScan diScan;
                    for (int i = 0; i < data.getScanList().size(); i++) {
                        diScan = new DiScan(data.getScanList().get(i).getId(), mIncome.getUsername(), mIncome.getUserid(), mIncome.getId(),
                                data.getScanList().get(i).getMoneysum(), data.getScanList().get(i).getName(), data.getScanList().get(i).getStore(), data.getScanList().get(i).getBrand(), data.getScanList().get(i).getQuantity(),
                                diType.getId(), i, data.getScanList().get(i).getRemark(), nowTime, nowTime, data.getScanList().get(i).getServerid(), data.getScanList().get(i).getDeletefalag(), CommonConstants.INCOME_RECORD_UPDATEED);
                        if(data.getScanList().get(i).getId() > 0) {
                            DIScanService.getInstance(ScanDetailActivity.this).update(diScan);
                        }else {
                            DIScanService.getInstance(ScanDetailActivity.this).save(diScan);
                        }
                    }

                    //金额还原再修改
                    if(mIncome.getRole() == CommonConstants.INCOME_ROLE_PAYING) {
                        //还原支出金额
                        DiAccount account = DIAccountService.getInstance(ScanDetailActivity.this).find(mIncome.getAccountid());
                        account.setMoneysum(account.getMoneysum() + mIncome.getMoneysum() );
                        account.setUpdatetime(CommonUtility.getCurrentTime());
                        DIAccountService.getInstance(ScanDetailActivity.this).update(account);

                        //重新支出
                        DiAccount accountnow = DIAccountService.getInstance(ScanDetailActivity.this).find(mAccountID);
                        accountnow.setMoneysum(accountnow.getMoneysum() - money);
                        accountnow.setUpdatetime(CommonUtility.getCurrentTime());
                        DIAccountService.getInstance(ScanDetailActivity.this).update(accountnow);

                        //还原支出预算
                        Date incomeTime = CommonUtility.stringToDate(income.getRecordtime());
                        final Calendar calendar = Calendar.getInstance();
                        calendar.setTime(incomeTime);
                        final DiBudget budget = DIBudgetService.getInstance(ScanDetailActivity.this).findByYearMonth(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1));
                        budget.setMoneylast(budget.getMoneylast() + mIncome.getMoneysum());
                        budget.setUpdatetime(CommonUtility.getCurrentTime());
                        DIBudgetService.getInstance(ScanDetailActivity.this).update(budget);

                        //重新支出预算
                        final DiBudget budgetnew = DIBudgetService.getInstance(ScanDetailActivity.this).findByYearMonth(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1));
                        budgetnew.setMoneylast(budgetnew.getMoneylast() - money);
                        budgetnew.setUpdatetime(CommonUtility.getCurrentTime());
                        if(budget.getIsupdate() !=  CommonConstants.INCOME_RECORD_NOT_UPDATE){
                            //同步数据库
                            SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(ScanDetailActivity.this) {
                                @Override
                                public void onSuccess(Void data) {
                                    DIBudgetService.getInstance(ScanDetailActivity.this).update(budgetnew);
                                    ScanDetailActivity.this.finish();
                                }

                            };
                            IncomeService.getInstance().budgetCynchronizeSingle(budgetnew, dataVerHandler);
                        }else {
                            DIBudgetService.getInstance(ScanDetailActivity.this).update(budgetnew);
                            ScanDetailActivity.this.finish();
                        }

                    }

                    Toast.makeText(ScanDetailActivity.this, ScanDetailActivity.this.getString(R.string.save_success) , Toast.LENGTH_SHORT).show();
                    //Snackbar.make(findViewById(R.id.app_title), R.string.save_success, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    //更新时间
                    mCalendar = Calendar.getInstance();
                }

            };

            IncomeService.getInstance().saveIncomeScanSingleByServerId(income, dataVerHandler);
        }else {

            DlIncomeService.getInstance(this).update(income);
            //scan扫描记录保存
            DiScan diScan;
            for (int i = 0; i < updateScan.size(); i++) {
                diScan = new DiScan(updateScan.get(i).getId(), mIncome.getUsername(), mIncome.getUserid(), mIncome.getId(),
                        updateScan.get(i).getMoneysum(), updateScan.get(i).getName(), updateScan.get(i).getStore(), updateScan.get(i).getBrand(), updateScan.get(i).getQuantity(),
                        diType.getId(), i, updateScan.get(i).getRemark(), nowTime, nowTime, updateScan.get(i).getServerid(), updateScan.get(i).getDeletefalag(), updateScan.get(i).getIsupdate());
                if(updateScan.get(i).getId() > 0) {
                    DIScanService.getInstance(ScanDetailActivity.this).update(diScan);
                }else {
                    DIScanService.getInstance(ScanDetailActivity.this).save(diScan);
                }
            }

            //金额还原再修改
            if(mIncome.getRole() == CommonConstants.INCOME_ROLE_PAYING) {
                //还原支出金额
                DiAccount account = DIAccountService.getInstance(this).find(mIncome.getAccountid());
                account.setMoneysum(account.getMoneysum() + mIncome.getMoneysum() );
                account.setUpdatetime(CommonUtility.getCurrentTime());
                DIAccountService.getInstance(this).update(account);

                //重新支出
                DiAccount accountnow = DIAccountService.getInstance(this).find(mAccountID);
                accountnow.setMoneysum(accountnow.getMoneysum() - money);
                accountnow.setUpdatetime(CommonUtility.getCurrentTime());
                DIAccountService.getInstance(this).update(accountnow);

                //还原支出预算
                Date incomeTime = CommonUtility.stringToDate(income.getRecordtime());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(incomeTime);
                DiBudget budget = DIBudgetService.getInstance(ScanDetailActivity.this).findByYearMonth(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1));
                budget.setMoneylast(budget.getMoneylast() + mIncome.getMoneysum() );
                budget.setUpdatetime(CommonUtility.getCurrentTime());
                DIBudgetService.getInstance(ScanDetailActivity.this).update(budget);

                //重新支出预算
                DiBudget budgetnew = DIBudgetService.getInstance(ScanDetailActivity.this).findByYearMonth(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1));
                budgetnew.setMoneylast(budgetnew.getMoneylast() - money);
                budgetnew.setUpdatetime(CommonUtility.getCurrentTime());
                DIBudgetService.getInstance(ScanDetailActivity.this).update(budgetnew);
            }

            Toast.makeText(this, this.getString(R.string.save_success), Toast.LENGTH_SHORT).show();
            //Snackbar.make(findViewById(R.id.app_title), R.string.save_success, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            //更新时间
            mCalendar = Calendar.getInstance();

            finish();
        }
    }

    private void showTypeDialog() {
        if (null != mTypeMaterialDialog) {
            mTypeMaterialDialog.show();
        } else {
            final List<DiAccount> diAccountList = DIAccountService.getInstance(this).findAll();
            final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
                @Override
                public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                    mScanRecordTypeCreditCard.setText(item.getContent());
                    for (DiAccount acctout : diAccountList) {
                        if (acctout.getId() == item.getId()) {
                            mAccountID = acctout.getId();
                            AccountSharedPreferences.setAccountSt(acctout.getId() + "|" + item.getContent());
                            break;
                        }
                    }
                    mTypeMaterialDialog.dismiss();
                }
            });
            String showColor = null;
            for (int i = 0; i < diAccountList.size(); i++) {
                showColor = "170,170,170";
                if (!CommonUtility.isEmpty(diAccountList.get(i).getColor())) {
                    showColor = diAccountList.get(i).getColor();
                }
                String[] colorArr = showColor.split(",");
                adapter.add(
                        new MaterialSimpleListItem.Builder(this)
                                .content(diAccountList.get(i).getName())
                                .id(diAccountList.get(i).getId())
                                .icon(CommonUtility.getImageIdByName(diAccountList.get(i).getIcon()))
                                .iconPaddingDp(8)
                                .backgroundColor(Color.rgb(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2])))
                                .build());
            }
            mTypeMaterialDialog = new MaterialDialog.Builder(this)
                    .title(R.string.account_type_title)
                    .adapter(adapter, null)
                    .show();
        }
    }


    ///////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关///////////////
    private void initLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCALHOST_REQUEST_CODE);
        } else {
            initLocationCode();
        }
    }

    private void initLocationCode() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        //第三步，配置定位SDK参数
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setIsNeedLocationPoiList(true);
        //可选，是否需要周边POI信息，默认为不需要，即参数为false
        //如果开发者需要获得周边POI信息，此处必须为true
        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取周边POI信息相关的结果
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //获取周边POI信息
            //POI信息包括POI ID、名称等，具体信息请参照类参考中POI类的相关说明
            List<Poi> poiList = location.getPoiList();
            String cityStr = "中国";
            if (!CommonUtility.isEmpty(location.getCity())) {
                cityStr = location.getCity();
            }
            if (poiList.size() > 0) {
                //位置
                mPoiList = new ArrayList<>();
                mPoiList.add(cityStr);
                for (Poi poi : location.getPoiList()) {
                    mPoiList.add(cityStr + "·" + poi.getName());
                }
                cityStr += "·" + poiList.get(0).getName();
            }
            if(CommonUtility.isEmpty(mIncome.getLocation())) {
                mRecordIconLocation.setText(cityStr);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_COARSE_LOCALHOST_REQUEST_CODE) {
            try {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    initLocationCode();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "访问被拒绝！会导致很多功能异常！请到设置里面开启", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "访问被拒绝！会导致很多功能异常！请到设置里面开启", Toast.LENGTH_SHORT).show();
            }
        }
    }

    ///////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关///////////////

}
