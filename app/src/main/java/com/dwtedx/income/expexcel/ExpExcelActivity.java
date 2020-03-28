package com.dwtedx.income.expexcel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.entity.DiExpexcel;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.profile.LoginV2Activity;
import com.dwtedx.income.provider.ExpExcelTipSharedPreferences;
import com.dwtedx.income.service.ExpExcelService;
import com.dwtedx.income.sqliteservice.DIAccountService;
import com.dwtedx.income.sqliteservice.DITypeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.vip.VipInfoActivity;
import com.dwtedx.income.widget.AppTitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ExpExcelActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, View.OnClickListener {

    @BindView(R.id.m_app_title)
    AppTitleBar mAppTitle;
    @BindView(R.id.m_exp_excel_name)
    EditText mExpExcelName;
    @BindView(R.id.m_exp_excel_role)
    Button mExpExcelRole;
    @BindView(R.id.m_exp_excel_moneysumstart)
    EditText mExpExcelMoneysumstart;
    @BindView(R.id.m_exp_excel_moneysumend)
    EditText mExpExcelMoneysumend;
    @BindView(R.id.m_exp_excel_type)
    Button mExpExcelType;
    @BindView(R.id.m_exp_excel_account)
    Button mExpExcelAccount;
    @BindView(R.id.m_exp_excel_recordtimestart)
    Button mExpExcelRecordtimestart;
    @BindView(R.id.m_exp_excel_recordtimeend)
    Button mExpExcelRecordtimeend;
    @BindView(R.id.m_save_btn)
    Button mSaveBtn;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Calendar mStartTimeCalendar;
    Calendar mEndTimeCalendar;

    DiExpexcel mDiExpexcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_excel);
        ButterKnife.bind(this);

        ExpExcelTipSharedPreferences.init(ExpExcelActivity.this);
        mAppTitle.setOnTitleClickListener(this);

        mExpExcelRole.setOnClickListener(this);
        mExpExcelType.setOnClickListener(this);
        mExpExcelAccount.setOnClickListener(this);
        mExpExcelRecordtimestart.setOnClickListener(this);
        mExpExcelRecordtimeend.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);

        mDiExpexcel = new DiExpexcel();
        mDiExpexcel.setRole(-1);

        if (ExpExcelTipSharedPreferences.getIsTip()) {
            //如果有历史记录直接跳转list
            getExpExcelInfo();
        }
    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                Intent intent = new Intent(ExpExcelActivity.this, ExpExcelListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_exp_excel_role:
                showRole();
                break;
            case R.id.m_exp_excel_type:
                showType();
                break;
            case R.id.m_exp_excel_account:
                showAccount();
                break;
            case R.id.m_exp_excel_recordtimestart:
                showStartData();
                break;
            case R.id.m_exp_excel_recordtimeend:
                showEndData();
                break;
            case R.id.m_save_btn:
                pool();
                break;
        }
    }

    private void pool() {
        if (!isLogin()) {
            Intent intent = new Intent(this, LoginV2Activity.class);
            startActivity(intent);
            return;
        }
        if (CommonUtility.isEmpty(mExpExcelName.getText().toString())) {
            Toast.makeText(ExpExcelActivity.this, R.string.exp_excel_error_tip, Toast.LENGTH_SHORT).show();
            return;
        }
        if (-1 == mDiExpexcel.getRole()) {
            Toast.makeText(this, R.string.exp_excel_type_tip, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!CommonUtility.isEmpty(mExpExcelMoneysumstart.getText().toString())) {
            mDiExpexcel.setMoneysumstart(Double.parseDouble(mExpExcelMoneysumstart.getText().toString()));
        } else {
            mDiExpexcel.setMoneysumstart(null);
        }
        if (!CommonUtility.isEmpty(mExpExcelMoneysumend.getText().toString())) {
            mDiExpexcel.setMoneysumend(Double.parseDouble(mExpExcelMoneysumend.getText().toString()));
        } else {
            mDiExpexcel.setMoneysumend(null);
        }
        mDiExpexcel.setUserid(ApplicationData.mDiUserInfo.getId());
        mDiExpexcel.setUsername(ApplicationData.mDiUserInfo.getUsername());
        mDiExpexcel.setName(mExpExcelName.getText().toString());
        //保存提示
        SaDataProccessHandler<Void, Void, Integer> dataVerHandler = new SaDataProccessHandler<Void, Void, Integer>(this) {
            @Override
            public void onSuccess(Integer data) {
                if (null == data || data == 0) {
                    new MaterialDialog.Builder(ExpExcelActivity.this)
                            .title(R.string.tip)
                            .content(R.string.exp_excel_error_tip_z)
                            .positiveText(R.string.ok)
                            .negativeText(R.string.cancel)
                            .show();
                    return;
                }
                if (data > 5000) {
                    new MaterialDialog.Builder(ExpExcelActivity.this)
                            .title(R.string.tip)
                            .content(R.string.exp_excel_error_tip_m)
                            .positiveText(R.string.ok)
                            .negativeText(R.string.cancel)
                            .show();
                    return;
                }
                String tipCount = getString(R.string.exp_excel_save_tip_count);
                tipCount = String.format(tipCount, data);
                new MaterialDialog.Builder(ExpExcelActivity.this)
                        .title(R.string.tip)
                        .content(tipCount)
                        .positiveText(R.string.ok)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                save();
                            }
                        })
                        .show();
            }
        };
        ExpExcelService.getInstance().poolExpExcel(mDiExpexcel, dataVerHandler);
    }

    private void save() {
        if (isVIP()) {
            saveExp();
        }else {
            //免费导出3条
            SaDataProccessHandler<Void, Void, List<DiExpexcel>> dataVerHandler = new SaDataProccessHandler<Void, Void, List<DiExpexcel>>(this) {
                @Override
                public void onSuccess(List<DiExpexcel> data) {
                    if (data.size() >= 3) {
                        new MaterialDialog.Builder(ExpExcelActivity.this)
                                .title(R.string.tip)
                                .content(R.string.exp_excel_vip_down_tip)
                                .positiveText(R.string.ok)
                                .negativeText(R.string.cancel)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent(ExpExcelActivity.this, VipInfoActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }else{
                        saveExp();
                    }
                }
            };
            ExpExcelService.getInstance().getMyExpExcelInfo(0, CommonConstants.PAGE_LENGTH_NUMBER, dataVerHandler);
        }
    }

    private void saveExp() {
        //保存
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(this) {
            @Override
            public void onSuccess(Void data) {
                ExpExcelTipSharedPreferences.setIsTip(true);
                new MaterialDialog.Builder(ExpExcelActivity.this)
                        .title(R.string.tip)
                        .content(R.string.exp_excel_save_tip)
                        .positiveText(R.string.ok)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                startActivity(new Intent(ExpExcelActivity.this, ExpExcelListActivity.class));
                            }
                        })
                        .show();
            }
        };
        ExpExcelService.getInstance().seveExpExcel(mDiExpexcel, dataVerHandler);
    }

    private void showStartData() {
        mStartTimeCalendar = Calendar.getInstance();
        DatePickerDialog pieStartdd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mStartTimeCalendar.set(year, monthOfYear, dayOfMonth);
                mExpExcelRecordtimestart.setText(format.format(mStartTimeCalendar.getTime()));
                mDiExpexcel.setRecordtimestart(format.format(mStartTimeCalendar.getTime()) + " 00:00:00");
            }
        }, mStartTimeCalendar.get(Calendar.YEAR), mStartTimeCalendar.get(Calendar.MONTH), mStartTimeCalendar.get(Calendar.DAY_OF_MONTH));
        pieStartdd.show();
    }

    private void showEndData() {
        mEndTimeCalendar = Calendar.getInstance();
        DatePickerDialog pieStartdd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mEndTimeCalendar.set(year, monthOfYear, dayOfMonth);
                mExpExcelRecordtimeend.setText(format.format(mEndTimeCalendar.getTime()));
                mDiExpexcel.setRecordtimeend(format.format(mEndTimeCalendar.getTime()) + " 24:00:00");
            }
        }, mEndTimeCalendar.get(Calendar.YEAR), mEndTimeCalendar.get(Calendar.MONTH), mEndTimeCalendar.get(Calendar.DAY_OF_MONTH));
        pieStartdd.show();
    }

    private void showAccount() {
        if (-1 == mDiExpexcel.getRole()) {
            Toast.makeText(this, R.string.exp_excel_type_tip, Toast.LENGTH_SHORT).show();
            return;
        }
        List<DiAccount> accounts = DIAccountService.getInstance(this).findAll();
        List<String> accountStrs = new ArrayList<>();
        for (DiAccount account : accounts) {
            accountStrs.add(account.getName());
        }
        new MaterialDialog.Builder(this)
                .title(R.string.exp_excel_account)
                .items(accountStrs)
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        String accountId = "";
                        String accountName = "";
                        for (int w : which) {
                            DiAccount account = accounts.get(w);
                            accountId += account.getServerid() + ",";
                            accountName += account.getName() + ",";
                        }
                        if (!CommonUtility.isEmpty(accountId)) {
                            accountId = accountId.substring(0, accountId.length() - 1);
                            accountName = accountName.substring(0, accountName.length() - 1);
                        }
                        mExpExcelAccount.setText(accountName);
                        mDiExpexcel.setAccount(accountName);
                        mDiExpexcel.setAccountid(accountId);
                        return true;
                    }
                })
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .show();
    }

    private void showType() {
        if (-1 == mDiExpexcel.getRole()) {
            Toast.makeText(this, R.string.exp_excel_type_tip, Toast.LENGTH_SHORT).show();
            return;
        }
        List<DiType> types = DITypeService.getInstance(this).findAll(mDiExpexcel.getRole());
        List<String> typeStrs = new ArrayList<>();
        for (DiType type : types) {
            typeStrs.add(type.getName());
        }
        new MaterialDialog.Builder(this)
                .title(R.string.exp_excel_type)
                .items(typeStrs)
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        String typeId = "";
                        String typeName = "";
                        for (int w : which) {
                            DiType type = types.get(w);
                            typeId += type.getServerid() + ",";
                            typeName += type.getName() + ",";
                        }
                        if (!CommonUtility.isEmpty(typeId)) {
                            typeId = typeId.substring(0, typeId.length() - 1);
                            typeName = typeName.substring(0, typeName.length() - 1);
                        }
                        mExpExcelType.setText(typeName);
                        mDiExpexcel.setType(typeName);
                        mDiExpexcel.setTypeid(typeId);
                        return true;
                    }
                })
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .show();
    }

    private void showRole() {
        new MaterialDialog.Builder(this)
                .title(R.string.exp_excel_role)
                .items(R.array.exp_excel_role)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {
                            mDiExpexcel.setRole(CommonConstants.INCOME_ROLE_ALL);
                        } else if (which == 1) {
                            mDiExpexcel.setRole(CommonConstants.INCOME_ROLE_PAYING);
                        } else if (which == 2) {
                            mDiExpexcel.setRole(CommonConstants.INCOME_ROLE_INCOME);
                        }
                        mExpExcelRole.setText(text.toString());
                    }
                }).show();
    }


    private void getExpExcelInfo() {

        SaDataProccessHandler<Void, Void, DiExpexcel> dataVerHandler = new SaDataProccessHandler<Void, Void, DiExpexcel>(this) {
            @Override
            public void onSuccess(DiExpexcel data) {
                if (null != data && CommonConstants.STATUS_EXPDONE == data.getStatus()) {
                    new MaterialDialog.Builder(ExpExcelActivity.this)
                            .title(R.string.tip)
                            .content(R.string.exp_excel_done_tip)
                            .positiveText(R.string.ok)
                            .negativeText(R.string.cancel)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    ExpExcelTipSharedPreferences.setIsTip(false);
                                    Intent intent = new Intent(ExpExcelActivity.this, ExpExcelListActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
            }
        };
        ExpExcelService.getInstance().getMyLastExpExcelInfo(dataVerHandler);
    }

}
