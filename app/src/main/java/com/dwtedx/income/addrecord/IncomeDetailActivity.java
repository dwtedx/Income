package com.dwtedx.income.addrecord;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.entity.DiBudget;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.home.IncomeLineFragment;
import com.dwtedx.income.home.IncomeListActivity;
import com.dwtedx.income.home.adapter.IncomeDetailRecyclerViewAdatper;
import com.dwtedx.income.provider.AccountSharedPreferences;
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
import com.dwtedx.income.widget.RecordKeyboardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IncomeDetailActivity extends BaseActivity implements RecordKeyboardView.OnKeyboardClickListener, AppTitleBar.OnTitleClickListener {

    private AppTitleBar mAppTitleBar;
    //计算
    private TextView mRecordAccountCount;
    private TextView mRecordAccountEditText;

    private DiIncome mIncome;
    private List<DiType> mDiTypeList;

    //收入类型
    private RecyclerView mRecyclerView;
    private IncomeDetailRecyclerViewAdatper adapter;
    private ImageView mCheckImageView;
    private TextView mCheckTextView;
    private int mCheckTypeId = 0;

    //帐户类型
    private MaterialDialog mTypeMaterialDialog;
    private int mAccountID;

    //时间
    private Calendar mCalendar;

    //备注
    private String mRecordRemarkStr;

    private RecordKeyboardView mRecordKeyboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_detail);
        mAppTitleBar = (AppTitleBar) findViewById(R.id.app_title);
        mAppTitleBar.setOnTitleClickListener(this);

        try {
            mIncome = ParseJsonToObject.getObject(DiIncome.class, new JSONObject(getIntent().getStringExtra("income")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null == mIncome){
            Toast.makeText(getApplicationContext(), "账目异常，请检查后重试", Toast.LENGTH_SHORT).show();
            finish();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecordAccountEditText = (TextView) findViewById(R.id.record_account);
        mRecordAccountEditText.setText(CommonUtility.doubleFormat(mIncome.getMoneysum()));
        mRecordAccountCount = (TextView) findViewById(R.id.record_account_count);
        mRecordKeyboardView = (RecordKeyboardView) findViewById(R.id.record_keyboard_view);
        mRecordKeyboardView.setRecordAccountTextView(mRecordAccountEditText, mRecordAccountCount);
        mRecordKeyboardView.setmIsC(true);
        mRecordKeyboardView.setOnKeyboardClickListener(this);

        mDiTypeList = DITypeService.getInstance(this).findAll(mIncome.getRole());
        mCheckImageView = (ImageView) findViewById(R.id.imageView);
        mCheckImageView.setImageResource(CommonUtility.getImageIdByName(this, DITypeService.getInstance(this).find(mIncome.getTypeid()).getIcon()));
        mCheckTextView = (TextView) findViewById(R.id.textView);
        mCheckTextView.setText(mIncome.getType());
        mCheckTypeId = mIncome.getTypeid();


        AccountSharedPreferences.init(this);
        mRecordKeyboardView.getMrCeditCard().setText(mIncome.getAccount());
        mAccountID = mIncome.getAccountid();

        mCalendar = Calendar.getInstance();
        mCalendar.setTime(CommonUtility.stringToDate(mIncome.getRecordtime()));
        mRecordKeyboardView.getmRecordTime().setText(CommonUtility.stringDateFormartMMdd(mCalendar.getTime()));

        mRecordRemarkStr = mIncome.getRemark();

        mRecyclerView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL));//设置RecyclerView的布局管理
        //mRecyclerView.addItemDecoration();//设置RecyclerView中item的分割线，用的少，一般都用在item中设置margin分隔两个item
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置item的添加删除动画，采用默认的动画效果
        adapter = new IncomeDetailRecyclerViewAdatper(this, mDiTypeList);
        mRecyclerView.setAdapter(adapter);//设置Adapter
        adapter.setOnItemClickListener(new IncomeDetailRecyclerViewAdatper.OnItemClickListener() {//添加监听器
            @Override
            public void onItemClick(View view, int postion) {
                mCheckImageView.setImageResource(CommonUtility.getImageIdByName(IncomeDetailActivity.this, mDiTypeList.get(postion).getIcon()));
                mCheckImageView.setAnimation(AnimationUtils.loadAnimation(IncomeDetailActivity.this, android.R.anim.fade_in));
                mCheckTextView.setText(mDiTypeList.get(postion).getName());
                mCheckTextView.setAnimation(AnimationUtils.loadAnimation(IncomeDetailActivity.this, android.R.anim.fade_in));
                mCheckTypeId = mDiTypeList.get(postion).getId();
            }

            @Override
            public void onItemLongClick(View view, int postion) {
                //Toast.makeText(getContext(), "长按的是：" + postion, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                deleteIncome();
                break;
        }
    }

    private void deleteIncome() {
        new MaterialDialog.Builder(this)
                .title(R.string.tip)
                .content(R.string.delete_income_tip)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //NEGATIVE   POSITIVE
                        if (which.name().equals("POSITIVE")) {
                            //同步服务器数据库
                            if(mIncome.getIsupdate() == CommonConstants.INCOME_RECORD_UPDATEED) {
                                mIncome.setScanList(DIScanService.getInstance(IncomeDetailActivity.this).findByIncomeId(mIncome.getId()));
                                SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(IncomeDetailActivity.this) {
                                    @Override
                                    public void onSuccess(Void data) {
                                        deleteIncomeAccount();
                                        Toast.makeText(IncomeDetailActivity.this, IncomeDetailActivity.this.getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                        IncomeDetailActivity.this.finish();
                                    }
                                };
                                IncomeService.getInstance().deleteIncomeByServerId(mIncome, dataVerHandler);
                            }else {
                                deleteIncomeAccount();
                                Toast.makeText(IncomeDetailActivity.this, IncomeDetailActivity.this.getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                IncomeDetailActivity.this.finish();
                            }
                        }
                    }
                })
                .show();

    }

    private void deleteIncomeAccount(){
        DlIncomeService.getInstance(IncomeDetailActivity.this).delete(mIncome.getId());
        //金额还原再修改
        if(mIncome.getRole() == CommonConstants.INCOME_ROLE_PAYING) {
            //还原支出金额
            DiAccount account = DIAccountService.getInstance(IncomeDetailActivity.this).find(mIncome.getAccountid());
            account.setMoneysum(account.getMoneysum() + mIncome.getMoneysum() );
            account.setUpdatetime(CommonUtility.getCurrentTime());
            DIAccountService.getInstance(IncomeDetailActivity.this).update(account);

            //还原支出预算
            Date incomeTime = CommonUtility.stringToDate(mIncome.getRecordtime());
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(incomeTime);
            final DiBudget budget = DIBudgetService.getInstance(IncomeDetailActivity.this).findByYearMonth(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1));
            budget.setMoneylast(budget.getMoneylast() + mIncome.getMoneysum());
            budget.setUpdatetime(CommonUtility.getCurrentTime());
            DIBudgetService.getInstance(IncomeDetailActivity.this).update(budget);

        }else if(mIncome.getRole() == CommonConstants.INCOME_ROLE_INCOME){
            //还原收入金额
            DiAccount account = DIAccountService.getInstance(IncomeDetailActivity.this).find(mIncome.getAccountid());
            account.setMoneysum(account.getMoneysum() - mIncome.getMoneysum() );
            account.setUpdatetime(CommonUtility.getCurrentTime());
            DIAccountService.getInstance(IncomeDetailActivity.this).update(account);
        }
        //删除扫单数据
        if(CommonConstants.INCOME_RECORD_TYPE_1 == mIncome.getRecordtype()){
            DIScanService.getInstance(this).deleteByIncomeId(mIncome.getId());
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

    @Override
    public void OnKeyboardClick(View v) {
        switch (v.getId()) {
            case R.id.m_record_type_credit_card:
                showTypeDialog();
                break;

            case R.id.m_record_type_time:
                DatePickerDialog dd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mCalendar.set(year, monthOfYear, dayOfMonth);
                        mRecordKeyboardView.getmRecordTime().setText(CommonUtility.stringDateFormartMMdd(mCalendar.getTime()));
                    }
                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                dd.show();
                break;

            case R.id.m_record_remark:
                new MaterialDialog.Builder(this)
                        .title(R.string.record_remark)
                        .inputType(InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                                InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        .inputRange(2, 50)
                        .positiveText(R.string.ok)
                        .negativeText(R.string.cancel)
                        .input("备注", mRecordRemarkStr, false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                mRecordRemarkStr = input.toString();
                            }
                        }).show();
                break;

            case R.id.m_record_input_save:
                save();
                break;
        }
    }

    private void save() {
        if(CommonUtility.isEmpty(mRecordAccountEditText.getText().toString())){
            //Toast.makeText(this, this.getString(R.string.record_money_error) , Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.app_title), R.string.record_money_error, Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
                            DlIncomeService.getInstance(IncomeDetailActivity.this).updateBeForTime(CommonUtility.stringDateFormartAddHours(mCalendar.getTime()));
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
        final double money = Double.parseDouble(mRecordAccountEditText.getText().toString());
        mRecordAccountEditText.setText("");
        final DiIncome income = new DiIncome(
                mIncome.getId(), mIncome.getUsername(), mIncome.getUserid(), mIncome.getRole(), money,
                mCheckTextView.getText().toString(), mCheckTypeId, mRecordKeyboardView.getMrCeditCard().getText().toString(), mAccountID,
                mRecordRemarkStr, null, mIncome.getVoicepath(), mIncome.getImagepath(), mIncome.getRecordtype(), chosedate, mIncome.getCreatetime(), nowTime, mIncome.getIsupdate(), mIncome.getServerid(), mIncome.getDeletefalag());

        //同步服务器数据库
        if(mIncome.getIsupdate() == CommonConstants.INCOME_RECORD_UPDATEED){
            //同步数据库
            SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(IncomeDetailActivity.this) {
                @Override
                public void onSuccess(Void data) {

                    DlIncomeService.getInstance(IncomeDetailActivity.this).update(income);

                    //金额还原再修改
                    if(mIncome.getRole() == CommonConstants.INCOME_ROLE_PAYING) {
                        //还原支出金额
                        DiAccount account = DIAccountService.getInstance(IncomeDetailActivity.this).find(mIncome.getAccountid());
                        account.setMoneysum(account.getMoneysum() + mIncome.getMoneysum() );
                        account.setUpdatetime(CommonUtility.getCurrentTime());
                        DIAccountService.getInstance(IncomeDetailActivity.this).update(account);

                        //重新支出
                        DiAccount accountnow = DIAccountService.getInstance(IncomeDetailActivity.this).find(mAccountID);
                        accountnow.setMoneysum(accountnow.getMoneysum() - money);
                        accountnow.setUpdatetime(CommonUtility.getCurrentTime());
                        DIAccountService.getInstance(IncomeDetailActivity.this).update(accountnow);

                        //还原支出预算
                        Date incomeTime = CommonUtility.stringToDate(income.getRecordtime());
                        final Calendar calendar = Calendar.getInstance();
                        calendar.setTime(incomeTime);
                        final DiBudget budget = DIBudgetService.getInstance(IncomeDetailActivity.this).findByYearMonth(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1));
                        budget.setMoneylast(budget.getMoneylast() + mIncome.getMoneysum());
                        budget.setUpdatetime(CommonUtility.getCurrentTime());
                        DIBudgetService.getInstance(IncomeDetailActivity.this).update(budget);

                        //重新支出预算
                        final DiBudget budgetnew = DIBudgetService.getInstance(IncomeDetailActivity.this).findByYearMonth(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1));
                        budgetnew.setMoneylast(budgetnew.getMoneylast() - money);
                        budgetnew.setUpdatetime(CommonUtility.getCurrentTime());
                        if(budget.getIsupdate() !=  CommonConstants.INCOME_RECORD_NOT_UPDATE){
                            //同步数据库
                            SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(IncomeDetailActivity.this) {
                                @Override
                                public void onSuccess(Void data) {
                                    DIBudgetService.getInstance(IncomeDetailActivity.this).update(budgetnew);
                                    IncomeDetailActivity.this.finish();
                                }

                            };
                            IncomeService.getInstance().budgetCynchronizeSingle(budgetnew, dataVerHandler);
                        }else {
                            DIBudgetService.getInstance(IncomeDetailActivity.this).update(budgetnew);
                            IncomeDetailActivity.this.finish();
                        }

                    }else if(mIncome.getRole() == CommonConstants.INCOME_ROLE_INCOME){
                        //还原支出金额
                        DiAccount account = DIAccountService.getInstance(IncomeDetailActivity.this).find(mIncome.getAccountid());
                        account.setMoneysum(account.getMoneysum() - mIncome.getMoneysum() );
                        account.setUpdatetime(CommonUtility.getCurrentTime());
                        DIAccountService.getInstance(IncomeDetailActivity.this).update(account);

                        //重新支出
                        DiAccount accountnow = DIAccountService.getInstance(IncomeDetailActivity.this).find(mAccountID);
                        accountnow.setMoneysum(accountnow.getMoneysum() + money);
                        accountnow.setUpdatetime(CommonUtility.getCurrentTime());
                        DIAccountService.getInstance(IncomeDetailActivity.this).update(accountnow);
                        IncomeDetailActivity.this.finish();
                    }

                    IncomeLineFragment.mLoadIncome = true;
                    IncomeListActivity.mLoadIncome = true;
                    Toast.makeText(IncomeDetailActivity.this, IncomeDetailActivity.this.getString(R.string.save_success) , Toast.LENGTH_SHORT).show();
                    //Snackbar.make(findViewById(R.id.app_title), R.string.save_success, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    //更新时间
                    mCalendar = Calendar.getInstance();
                }

            };

            IncomeService.getInstance().incomeCynchronizeSingleByServerId(income, dataVerHandler);
        }else {

            DlIncomeService.getInstance(this).update(income);

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
                DiBudget budget = DIBudgetService.getInstance(IncomeDetailActivity.this).findByYearMonth(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1));
                budget.setMoneylast(budget.getMoneylast() + mIncome.getMoneysum() );
                budget.setUpdatetime(CommonUtility.getCurrentTime());
                DIBudgetService.getInstance(IncomeDetailActivity.this).update(budget);

                //重新支出预算
                DiBudget budgetnew = DIBudgetService.getInstance(IncomeDetailActivity.this).findByYearMonth(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1));
                budgetnew.setMoneylast(budgetnew.getMoneylast() - money);
                budgetnew.setUpdatetime(CommonUtility.getCurrentTime());
                DIBudgetService.getInstance(IncomeDetailActivity.this).update(budgetnew);
            }else if(mIncome.getRole() == CommonConstants.INCOME_ROLE_INCOME){
                //还原支出金额
                DiAccount account = DIAccountService.getInstance(this).find(mIncome.getAccountid());
                account.setMoneysum(account.getMoneysum() - mIncome.getMoneysum() );
                account.setUpdatetime(CommonUtility.getCurrentTime());
                DIAccountService.getInstance(this).update(account);

                //重新支出
                DiAccount accountnow = DIAccountService.getInstance(this).find(mAccountID);
                accountnow.setMoneysum(accountnow.getMoneysum() + money);
                accountnow.setUpdatetime(CommonUtility.getCurrentTime());
                DIAccountService.getInstance(this).update(accountnow);
            }

            IncomeLineFragment.mLoadIncome = true;
            IncomeListActivity.mLoadIncome = true;
            Toast.makeText(this, this.getString(R.string.save_success), Toast.LENGTH_SHORT).show();
            //Snackbar.make(findViewById(R.id.app_title), R.string.save_success, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            //更新时间
            mCalendar = Calendar.getInstance();

            finish();
        }
    }

    private void showTypeDialog() {
        if (null != mTypeMaterialDialog){
            mTypeMaterialDialog.show();
        }else {
            final List<DiAccount> diAccountList = DIAccountService.getInstance(this).findAll();
            final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
                @Override
                public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                    mRecordKeyboardView.getMrCeditCard().setText(item.getContent());
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
                                .icon(CommonUtility.getImageIdByName(IncomeDetailActivity.this, diAccountList.get(i).getIcon()))
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

}
