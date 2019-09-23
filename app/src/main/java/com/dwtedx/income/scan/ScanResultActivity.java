package com.dwtedx.income.scan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.dwtedx.income.R;
import com.dwtedx.income.accounttype.ChoosePayingTypeActivity;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.ProgressDialog;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.entity.DiBudget;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.entity.DiParacontent;
import com.dwtedx.income.entity.DiScan;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.entity.OcrResultInfo;
import com.dwtedx.income.entity.OcrWordsResultInfo;
import com.dwtedx.income.entity.ScanTicketInfo;
import com.dwtedx.income.provider.AccountSharedPreferences;
import com.dwtedx.income.provider.ScanTipSharedPreferences;
import com.dwtedx.income.scan.adapter.ScanResultAdapter;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanResultActivity extends BaseActivity implements RecognizeService.ServiceListener, AppTitleBar.OnTitleClickListener, View.OnClickListener, ScanResultAdapter.OnValueEditAfterTextChangeListener {

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

    private String mCameraFileNamePath;
    private ProgressDialog mProgressDialog;

    //帐户类型
    private MaterialDialog mTypeMaterialDialog;
    private int mAccountID;

    private Calendar mCalendar;
    private double mAllMoneySum;
    private DiType diType;
    private static int REQUEST_CODE_CHOOSE_TYPE = 70;
    private static int REQUEST_CODE_CHOOSE_LOCAL = 71;
    private static int ACCESS_COARSE_LOCALHOST_REQUEST_CODE = 72;

    private List<ScanTicketInfo> mScanTicketInfoInfo;
    private ScanResultAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        ButterKnife.bind(this);
        mAppTitle.setOnTitleClickListener(this);
        mTypeRecordLayout.setOnClickListener(this);

        mCameraFileNamePath = getIntent().getStringExtra(CameraActivity.KEY_OUTPUT_FILE_PATH);
        mProgressDialog = getProgressDialog();

        mCalendar = Calendar.getInstance();
        diType = DITypeService.getInstance(this).find(1);//1表示一般支出
        setDiType();

        //账户设置
        AccountSharedPreferences.init(this);
        String[] accoutArr = AccountSharedPreferences.getAccountStr().split("\\|");
        mAccountID = Integer.parseInt(accoutArr[0]);
        mScanRecordTypeCreditCard.setText(accoutArr[1]);
        mScanRecordTypeCreditCard.setOnClickListener(this);

        //定位
        mRecordIconLocation.setOnClickListener(this);
        initLocation();

        mScanTicketInfoInfo = new ArrayList<>();
        mScanTicketInfoInfo.add(new ScanTicketInfo(true));
        reScanReceipt();//照片读取

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        //自定义分割线的样式
        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 0, ContextCompat.getColor(this, R.color.common_division_line)));
        mAdapter = new ScanResultAdapter(this, mScanTicketInfoInfo);
        mAdapter.setmOnValueEditAfterTextChangeListener(this);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void reScanReceipt() {
        mProgressDialog.show();
        //调用通用票据识别服务
        //RecognizeService.recReceipt(this, mCameraFileNamePath, this);
        //通用文字识别 recGeneralBasic（普通）recAccurateBasic（高精度）
        RecognizeService.recAccurateBasic(this, mCameraFileNamePath, this);
    }

    private void setDiType() {
        mTypeImageView.setImageResource(CommonUtility.getImageIdByName(this, diType.getIcon()));
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
        Intent intent;
        switch (v.getId()) {
            case R.id.m_type_record_layout:
                intent = new Intent(this, ChoosePayingTypeActivity.class);
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
    public void onResult(String result) {
        mProgressDialog.cancel();
        //Toast.makeText(ScanResuleActivity.this, result, Toast.LENGTH_LONG).show();
        //识别扫描结果（初步版本）
        try {
            OcrResultInfo resultData = ParseJsonToObject.getObject(OcrResultInfo.class, new JSONObject(result));
            if (resultData.getWords_result_num() == 0) {
                Snackbar.make(mRecyclerView, getString(R.string.scan_result_not_tip), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }
            Log.i(getLocalClassName(), result);
            readWordName(resultData);

            //引导
            showGuide();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showGuide() {
        ScanTipSharedPreferences.init(this);
        if (ScanTipSharedPreferences.getIsTip() && mScanTicketInfoInfo.size() > 1) {
            Animation enterAnimation = new AlphaAnimation(0f, 1f);
            enterAnimation.setDuration(600);
            enterAnimation.setFillAfter(true);
            Animation exitAnimation = new AlphaAnimation(1f, 0f);
            exitAnimation.setDuration(300);
            exitAnimation.setFillAfter(true);
            NewbieGuide.with(this)
                    .setLabel("delete_guide")
                    .alwaysShow(true)//总是显示，调试时可以打开
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(mRecyclerView.getChildAt(0).findViewById(R.id.m_item_delete_icon_view), HighLight.Shape.CIRCLE, 5)
                            .setLayoutRes(R.layout.scan_delete_guide)
                            .setEnterAnimation(enterAnimation)//进入动画
                            .setExitAnimation(exitAnimation))//退出动画
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(mRecyclerView.getChildAt(0).findViewById(R.id.m_item_value_edit_text), HighLight.Shape.OVAL, 0)
                            .setLayoutRes(R.layout.scan_edit_guide)
                            .setEnterAnimation(enterAnimation)//进入动画
                            .setExitAnimation(exitAnimation))//退出动画
                    .setOnGuideChangedListener(new OnGuideChangedListener() {
                        @Override
                        public void onShowed(Controller controller) {
                            hideInput(ScanResultActivity.this, mRecyclerView.getChildAt(0).findViewById(R.id.m_item_value_edit_text));
                        }

                        @Override
                        public void onRemoved(Controller controller) {
                            ScanTipSharedPreferences.setIsTip(ApplicationData.mAppVersionCode);
                        }
                    })
                    .show();
        }
    }

    /**
     * 强制隐藏输入法键盘
     *
     * @param context Context
     * @param view    EditText
     */
    public static void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onError(OCRError error) {
        mProgressDialog.cancel();
        Toast.makeText(ScanResultActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * 尝试读取消费名称
     *
     * @param resultData
     */
    private void readWordName(OcrResultInfo resultData) {
        OcrWordsResultInfo ocrWords;
        OcrWordsResultInfo ocrWordsNubmer;
        ScanTicketInfo scanTicketInfo;
        boolean boolVal;
        int j;
        for (int i = 0; i < resultData.getWords_result_num(); i++) {
            ocrWords = resultData.getWords_result().get(i);
            if (!CommonUtility.isEmpty(ocrWords.getWords())
                    && !CommonUtility.isNumerics(ocrWords.getWords())
                    & ocrWords.getWords().length() > 1
                    && !isExcludeContains(ocrWords.getWords())) {
                //检查所有信息是否一行返回
                if (isContainNumerics(ocrWords.getWords())) {
                    scanTicketInfo = new ScanTicketInfo();
                    scanTicketInfo.setName(ocrWords.getWords());
                    double account = getNumberFromStrVal(ocrWords.getWords());
                    scanTicketInfo.setValue(CommonUtility.doubleFormat(String.valueOf(account)));
                    mAllMoneySum += scanTicketInfo.getValue();//计算总金额
                } else {
                    scanTicketInfo = new ScanTicketInfo();
                    scanTicketInfo.setName(ocrWords.getWords());
                    //尝试获取金额
                    do {
                        if (i + 1 >= resultData.getWords_result_num()) {
                            boolVal = true;
                            break;
                        }
                        j = ++i;//i自增
                        ocrWordsNubmer = resultData.getWords_result().get(j);
                        boolVal = readAccount(ocrWordsNubmer, scanTicketInfo);
                    } while (!boolVal);
                    mAllMoneySum += scanTicketInfo.getValue();//计算总金额
                }
                mScanTicketInfoInfo.add(mScanTicketInfoInfo.size() - 1, scanTicketInfo);
            }
        }
        mRecordAccountText.setText(String.valueOf(mAllMoneySum));
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 尝试读取消费金额和数量
     * 只要读取到金额返回true
     *
     * @param ocrWordsNubmer
     * @param scanTicketInfo
     * @return
     */
    private boolean readAccount(OcrWordsResultInfo ocrWordsNubmer, ScanTicketInfo scanTicketInfo) {
        if (CommonUtility.isEmpty(ocrWordsNubmer.getWords())) {
            return false;
        }
        if (ocrWordsNubmer.getWords().length() >= 8) {
            ocrWordsNubmer.setWords(CommonUtility.getNumericPoints(ocrWordsNubmer.getWords()));
        }
        //是否有小数点
        if (ocrWordsNubmer.getWords().indexOf(".") > 0) {
            if (ocrWordsNubmer.getWords().length() > 1
                    && CommonUtility.isNumerics(ocrWordsNubmer.getWords())) {
                double account = getNumberFromStrVal(ocrWordsNubmer.getWords());
                scanTicketInfo.setValue(CommonUtility.doubleFormat(String.valueOf(account)));
                return true;
            }
        } else {
            if (ocrWordsNubmer.getWords().length() > 1
                    && ocrWordsNubmer.getWords().length() < 8
                    && CommonUtility.isNumerics(ocrWordsNubmer.getWords())) {
                double account = 0;
                try {
                    account = CommonUtility.doubleFormat(ocrWordsNubmer.getWords());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                scanTicketInfo.setValue(account);
                return true;
            } else if (ocrWordsNubmer.getWords().length() == 1 && CommonUtility.isNumeric(ocrWordsNubmer.getWords())) {
                scanTicketInfo.setQuantity(Integer.parseInt(ocrWordsNubmer.getWords()));
                //只要读取到金额返回true
                return false;
            }
        }
        return false;
    }

    private double getNumberFromStrVal(String ocrWords) {
        double account = 0;
        try {
            //找到小数点位置然后截取
            String[] numberArray = ocrWords.split("\\.");
            int numLength = numberArray.length - 1;
            if (numberArray[numLength].length() > 2) {
                account = Double.parseDouble(numberArray[numLength - 1] + "." + numberArray[numLength].substring(0, 2));
            } else {
                account = Double.parseDouble(numberArray[numLength - 1] + "." + numberArray[numLength]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            account = 0;
        }
        return account;
    }

    /**
     * 是否包含数字和小数点
     *
     * @param str
     * @return
     */
    public boolean isContainNumerics(String str) {
        int pointArrNum = str.lastIndexOf(".");
        if (pointArrNum < 0) {
            return false;
        }
        if (str.length() <= pointArrNum + 1) {
            return false;
        }
        String pointRightStr = str.substring(pointArrNum + 1, pointArrNum + 2);
        if (!CommonUtility.isNumeric(pointRightStr)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^([\\w\\W]*)[\\d]([\\w\\W]*)*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private boolean isExcludeContains(String words) {
        boolean result;
        for (DiParacontent paracontent : ApplicationData.scanParacontent) {
            result = words.contains(paracontent.getContent());
            if (result) {
                return result;
            }
        }
        return false;
    }

    @Override
    public void OnValueEditAfterTextChanged(String s) {
        mAllMoneySum = 0;
        for (int i = 0; i < mScanTicketInfoInfo.size(); i++) {
            mAllMoneySum += mScanTicketInfoInfo.get(i).getValue();//计算总金额
        }
        mRecordAccountText.setText(CommonUtility.doubleFormat(mAllMoneySum));
    }

    private void save() {
        mRecordAccountText.requestFocus();//获取焦点 光标出现
        if (CommonUtility.isEmpty(mRecordAccountText.getText().toString())) {
            //Toast.makeText(getContext(), getContext().getString(R.string.record_money_error) , Toast.LENGTH_SHORT).show();
            Snackbar.make(mRecyclerView, R.string.record_money_error, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        //开始节点第一条记录处理 by sinyuu 20190920
        DiIncome beForDiIncome = DlIncomeService.getInstance(this).findBeForTime();
        if(null != beForDiIncome) {
            Date beForTime = CommonUtility.stringToDate(beForDiIncome.getRecordtime());
            if (null != beForTime && mCalendar.getTime().before(beForTime)) {
                if (CommonConstants.INCOME_RECORD_UPDATEED == beForDiIncome.getIsupdate()) {
                    //同步数据库
                    SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(this) {
                        @Override
                        public void onSuccess(Void data) {
                            DlIncomeService.getInstance(ScanResultActivity.this).updateBeForTime(CommonUtility.stringDateFormartAddHours(mCalendar.getTime()));
                            saveIncome();
                        }
                    };
                    IncomeService.getInstance().updateIncomeBeforTime(CommonUtility.stringDateFormartAddHours(mCalendar.getTime()), dataVerHandler);
                } else {
                    DlIncomeService.getInstance(this).updateBeForTime(CommonUtility.stringDateFormartAddHours(mCalendar.getTime()));
                    saveIncome();
                }
            }else{
                saveIncome();
            }
        }else{
            saveIncome();
        }
        //开始节点第一条记录处理 结束 by sinyuu 20190920
    }

    private void saveIncome(){
        String chosedate = CommonUtility.stringDateFormart(mCalendar.getTime());
        String nowTime = CommonUtility.getCurrentTime();
        double money = Double.parseDouble(mRecordAccountText.getText().toString());
        mRecordAccountText.setText("");
        DiIncome income = new DiIncome(
                0, null, 0, CommonConstants.INCOME_ROLE_PAYING, money,
                diType.getName(), diType.getId(), mScanRecordTypeCreditCard.getText().toString(), mAccountID,
                null, mRecordIconLocation.getText().toString(), null, null, CommonConstants.INCOME_RECORD_TYPE_1, chosedate, nowTime, nowTime, CommonConstants.INCOME_RECORD_NOT_UPDATE, 0, CommonConstants.DELETEFALAG_NOTDELETE);
        DlIncomeService.getInstance(this).save(income);

        //帐户金额修改
        DiAccount account = DIAccountService.getInstance(this).find(mAccountID);
        account.setMoneysum(account.getMoneysum() - money);
        account.setUpdatetime(CommonUtility.getCurrentTime());
        DIAccountService.getInstance(this).update(account);

        //预算
        DiBudget budget = DIBudgetService.getInstance(this).findLastRow();
        budget.setMoneylast(budget.getMoneylast() - money);
        budget.setUpdatetime(CommonUtility.getCurrentTime());
        DIBudgetService.getInstance(this).update(budget);

        //scan扫描记录保存
        DiScan diScan;
        for (int i = 0; i < mScanTicketInfoInfo.size(); i++) {
            if (!mScanTicketInfoInfo.get(i).isAdd()) {
                diScan = new DiScan(0, null, 0, DlIncomeService.getInstance(this).getLastId(),
                        mScanTicketInfoInfo.get(i).getValue(), mScanTicketInfoInfo.get(i).getName(), null, null, 1,
                        diType.getId(), i, null, nowTime, nowTime, 0, CommonConstants.DELETEFALAG_NOTDELETE, CommonConstants.INCOME_RECORD_NOT_UPDATE);
                DIScanService.getInstance(this).save(diScan);
            }
        }

        Toast.makeText(this, this.getString(R.string.save_success), Toast.LENGTH_SHORT).show();
        //Snackbar.make (mRecyclerView, R.string.save_success, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        //更新时间
        mCalendar = Calendar.getInstance();
        finish();
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
                                .icon(CommonUtility.getImageIdByName(this, diAccountList.get(i).getIcon()))
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
            if(null == location){
                Toast.makeText(ScanResultActivity.this, R.string.scan_location_tip, Toast.LENGTH_SHORT).show();
                return;
            }
            String cityStr = "中国";
            if (!CommonUtility.isEmpty(location.getCity())) {
                cityStr = location.getCity();
            }
            if (null != location.getPoiList() && location.getPoiList().size() > 0) {
                //位置
                mPoiList = new ArrayList<String>();
                mPoiList.add(cityStr);
                for (Poi poi : location.getPoiList()) {
                    mPoiList.add(cityStr + "·" + poi.getName());
                }
                cityStr += "·" + location.getPoiList().get(0).getName();
            }
            mRecordIconLocation.setText(cityStr);
            //获取周边POI信息
            //POI信息包括POI ID、名称等，具体信息请参照类参考中POI类的相关说明
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
