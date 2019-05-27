package com.dwtedx.income.profile;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.afollestad.materialdialogs.util.DialogUtils;
import com.bumptech.glide.Glide;
import com.dwtedx.income.R;
import com.dwtedx.income.account.AccountActivity;
import com.dwtedx.income.accounttype.IncomeTypeActivity;
import com.dwtedx.income.accounttype.PayingTypeActivity;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.ProgressDialog;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.report.ShareActivity;
import com.dwtedx.income.service.IncomeService;
import com.dwtedx.income.sqliteservice.DIAccountService;
import com.dwtedx.income.sqliteservice.DITypeService;
import com.dwtedx.income.sqliteservice.DlIncomeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.widget.CircleImageView;
import com.dwtedx.income.widget.theme.CircleView;
import com.dwtedx.income.widget.theme.ColorChooserDialog;

import java.util.List;

public class ProfileActivity extends BaseActivity implements View.OnClickListener, ColorChooserDialog.ColorCallback {

    private CircleImageView mHeadImageView;
    private TextView mHeadName;
    private int[] mClickView = {R.id.nav_header_view, R.id.profile_type_pay, R.id.profile_type_income, R.id.profile_account, R.id.profile_data, R.id.setup_reset_pass, R.id.profile_theme, R.id.profile_accent, R.id.profile_setup, R.id.profile_share};


    // color chooser dialog
    private int primaryPreselect;
    private int accentPreselect;

    //数据恢复的记录标记
    private int incomeListSize;
    //数据恢复的show标记
    private boolean isShowData;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置是否有返回箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isShowData = getIntent().getBooleanExtra("isShowData", false);
        if (isShowData) {
            dataRegain();
        }

        mHeadImageView = (CircleImageView) findViewById(R.id.imageView);
        mHeadName = (TextView) findViewById(R.id.usernameView);

        for (int id : mClickView) {
            findViewById(id).setOnClickListener(this);
        }

        primaryPreselect = DialogUtils.resolveColor(this, R.attr.colorPrimary);
        accentPreselect = DialogUtils.resolveColor(this, R.attr.colorAccent);

        incomeListSize = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (super.isLogin()) {
            Glide.with(this).load(ApplicationData.mDiUserInfo.getHead()).into(mHeadImageView);
            mHeadName.setText(ApplicationData.mDiUserInfo.getName());
        } else {
            Glide.with(this).load(R.mipmap.userhead).into(mHeadImageView);
            mHeadName.setText(getString(R.string.profile_login));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //重写ToolBar返回按钮的行为，防止重新打开父Activity重走生命周期方法
            case android.R.id.home:
                finish();
                break;
            case R.id.profile_set:
                startActivity(new Intent(this, SetupActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profie_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header_view:
                if (isLogin()) {
                    //TODO 用户信息
                } else {
                    startActivity(new Intent(ProfileActivity.this, LoginV2Activity.class));
                }
                break;
            case R.id.profile_type_pay:
                startActivity(new Intent(this, PayingTypeActivity.class));
                break;
            case R.id.profile_type_income:
                startActivity(new Intent(this, IncomeTypeActivity.class));
                break;
            case R.id.profile_share:
                startActivity(new Intent(this, ShareActivity.class));
                break;
            case R.id.profile_account:
                startActivity(new Intent(this, AccountActivity.class));
                break;
            case R.id.profile_data:
                dataRegain();
                break;
            case R.id.setup_reset_pass:
                break;
            case R.id.profile_theme:
                new ColorChooserDialog.Builder(this, R.string.color_palette)
                        .titleSub(R.string.app_name)
                        .preselect(primaryPreselect)
                        .show();
                break;
            case R.id.profile_accent:
                new ColorChooserDialog.Builder(this, R.string.color_palette)
                        .titleSub(R.string.app_name)
                        .accentMode(true)
                        .preselect(accentPreselect)
                        .show();
                break;
            case R.id.profile_setup:
                startActivity(new Intent(this, SetupActivity.class));
                break;
        }
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int color) {
        if (dialog.isAccentMode()) {
            accentPreselect = color;
            ThemeSingleton.get().positiveColor = DialogUtils.getActionTextStateList(this, color);
            ThemeSingleton.get().neutralColor = DialogUtils.getActionTextStateList(this, color);
            ThemeSingleton.get().negativeColor = DialogUtils.getActionTextStateList(this, color);
            ThemeSingleton.get().widgetColor = color;
        } else {
            primaryPreselect = color;
            if (getSupportActionBar() != null)
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(CircleView.shiftColorDown(color));
                getWindow().setNavigationBarColor(color);
            }
        }
    }

    private void dataRegain() {
        if (!isLogin()) {
            startActivity(new Intent(ProfileActivity.this, LoginV2Activity.class));
            return;
        }
        new MaterialDialog.Builder(this)
                .title(R.string.tip)
                .content(R.string.profile_data_tip)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //NEGATIVE   POSITIVE
                        if (which.name().equals("POSITIVE")) {
                            showProgressDialog();
                            DlIncomeService.getInstance(ProfileActivity.this).deleteAll();
                            getIncomeCynchronizeSingleList();
                        }
                    }
                })
                .show();
    }

    public void showProgressDialog() {
        mProgressDialog = getProgressDialog();
        mProgressDialog.show();
    }

    public void cancelProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.cancel();
        }
    }

    /**
     * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/8/19 上午10:24.
     * Company 路之遥网络科技有限公司
     * Description 恢复记账数据
     */
    private void getIncomeCynchronizeSingleList() {
        SaDataProccessHandler<Void, Void, List<DiIncome>> dataVerHandler = new
                SaDataProccessHandler<Void, Void, List<DiIncome>>(ProfileActivity.this) {
                    @Override
                    public void onSuccess(List<DiIncome> data) {
                        if (data.size() > 0) {
                            incomeListSize = incomeListSize + data.size();
                            for (DiIncome income : data) {
                                DlIncomeService.getInstance(ProfileActivity.this).saveSynchronizeEgain(income);
                            }
                            getIncomeCynchronizeSingleList();
                        } else {
                            getTypeCynchronizeSingleList();
                        }
                    }

                    @Override
                    public void onPreExecute() {}
                };
        IncomeService.getInstance().incomeCynchronizeSingleList(incomeListSize, CommonConstants.PAGE_LENGTH_NUMBER, dataVerHandler);
    }

    /**
     * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/8/19 上午10:25.
     * Company 路之遥网络科技有限公司
     * Description恢复类型
     */
    private void getTypeCynchronizeSingleList() {
        SaDataProccessHandler<Void, Void, List<DiType>> dataVerHandler = new
                SaDataProccessHandler<Void, Void, List<DiType>>(ProfileActivity.this) {
                    @Override
                    public void onSuccess(List<DiType> data) {
                        for (DiType diType : data) {
                            DITypeService.getInstance(ProfileActivity.this).saveSynchronizeEgain(diType);
                        }
                        getAccountCynchronizeSingleList();
                    }

                    @Override
                    public void onPreExecute() {}
                };
        IncomeService.getInstance().typeCynchronizeSingleList(dataVerHandler);
    }

    /**
     * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/8/19 上午10:25.
     * Company 路之遥网络科技有限公司
     * Description 恢复帐户
     */
    private void getAccountCynchronizeSingleList() {
        SaDataProccessHandler<Void, Void, List<DiAccount>> dataVerHandler = new
                SaDataProccessHandler<Void, Void, List<DiAccount>>(ProfileActivity.this) {
                    @Override
                    public void onSuccess(List<DiAccount> data) {
                        for (DiAccount account : data) {
                            DIAccountService.getInstance(ProfileActivity.this).saveSynchronizeEgain(account);
                        }
                        cancelProgressDialog();
                        Snackbar.make(findViewById(R.id.app_title), R.string.profile_data_success, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }

                    @Override
                    public void onPreExecute() {}

                };
        IncomeService.getInstance().accountCynchronizeSingleList(dataVerHandler);
    }
}
