package com.dwtedx.income.expexcel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.entity.DiExpexcel;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.sqliteservice.DIAccountService;
import com.dwtedx.income.sqliteservice.DITypeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.AppTitleBar;

import java.util.ArrayList;
import java.util.List;

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

    DiExpexcel mDiExpexcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_excel);
        ButterKnife.bind(this);

        mAppTitle.setOnTitleClickListener(this);

        mExpExcelRole.setOnClickListener(this);
        mExpExcelType.setOnClickListener(this);
        mExpExcelAccount.setOnClickListener(this);
        mExpExcelRecordtimestart.setOnClickListener(this);
        mExpExcelRecordtimeend.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);

        mDiExpexcel = new DiExpexcel();

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
    public void onClick(View v) {
        switch (v.getId()){
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

                break;
            case R.id.m_exp_excel_recordtimeend:

                break;
            case R.id.m_save_btn:

                break;
        }
    }

    private void showAccount() {
        if(0 == mDiExpexcel.getRole()){
            Toast.makeText(this, R.string.exp_excel_type_tip, Toast.LENGTH_SHORT).show();
            return;
        }
        List<DiAccount> accounts = DIAccountService.getInstance(this).findAll();
        List<String> accountStrs = new ArrayList<>();
        for (DiAccount account:accounts) {
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
                        if(!CommonUtility.isEmpty(accountId)){
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
        if(0 == mDiExpexcel.getRole()){
            Toast.makeText(this, R.string.exp_excel_type_tip, Toast.LENGTH_SHORT).show();
            return;
        }
        List<DiType> types = DITypeService.getInstance(this).findAll(mDiExpexcel.getRole());
        List<String> typeStrs = new ArrayList<>();
        for (DiType type:types) {
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
                        if(!CommonUtility.isEmpty(typeId)){
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
                            mDiExpexcel.setRole(CommonConstants.INCOME_ROLE_PAYING);
                        } else if (which == 1) {
                            mDiExpexcel.setRole(CommonConstants.INCOME_ROLE_INCOME);
                        }
                        mExpExcelRole.setText(text.toString());
                    }
                }).show();
    }

}
