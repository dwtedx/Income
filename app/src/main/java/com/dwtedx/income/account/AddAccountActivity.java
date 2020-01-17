package com.dwtedx.income.account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.entity.IdInfo;
import com.dwtedx.income.service.IncomeService;
import com.dwtedx.income.sqliteservice.DIAccountService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.theme.ColorChooserDialog;
import com.dwtedx.income.widget.theme.ColorPalette;

public class AddAccountActivity extends BaseActivity implements OnClickListener, ColorChooserDialog.ColorCallback, AppTitleBar.OnTitleClickListener{

    private AppTitleBar mAppTitleBar;
    private EditText mAddAccountMoneysum, mAddAccountName;
    private LinearLayout mAddAccountColorLayout;
    private RelativeLayout mAddAccountImageLayout;
    private TextView mAddAccountColor;
    private ImageView mAddAccountImage;
    private Button mSaveButton;
    private EditText mAccountRemark;

    private int mAccountPreselect;
    private String mSelectedColor;

    private int mAccountId;
    private DiAccount mAccountUpdate;

    private String mAccountTypeImage= CommonConstants.INCOME_ACCOUNT_TYPE_ADD;
    private static int REQUEST_CODE_CHOOSE_IMAGE = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        mAppTitleBar = (AppTitleBar) findViewById(R.id.app_title);
        mAppTitleBar.setOnTitleClickListener(this);

        init();
        mAccountId = getIntent().getIntExtra("ACCOUNTID", -1);
        if(mAccountId > 0){
            mAppTitleBar.setRightVisibility(View.VISIBLE);
            mAccountUpdate = DIAccountService.getInstance(AddAccountActivity.this).find(mAccountId);
            mAddAccountMoneysum.setText(CommonUtility.doubleFormat(mAccountUpdate.getMoneysum()));
            mAddAccountName.setText(mAccountUpdate.getName());
            mAccountTypeImage = mAccountUpdate.getIcon();
            mAddAccountImage.setImageResource(CommonUtility.getImageIdByName(this, mAccountUpdate.getIcon()));//为ViewHolder里的控件设置值
            //颜色处理
            mSelectedColor = mAccountUpdate.getColor();
            String[] colorArr = mSelectedColor.split(",");
            mAccountPreselect = Color.rgb(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2]));
            mAddAccountColor.setBackgroundColor(mAccountPreselect);
            mAccountRemark.setText(mAccountUpdate.getRemark());
        }else {
            mAppTitleBar.setRightVisibility(View.GONE);
        }

    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                if(null != mAccountUpdate) {
                    deleteAccount(mAccountUpdate.getServerid(), mAccountUpdate.getId());
                }
                break;
        }
    }

    private void init() {
        mAddAccountMoneysum = (EditText) findViewById(R.id.add_account_moneysum);
        accountEditTwoNom();
        mAddAccountName = (EditText) findViewById(R.id.add_account_name);
        mAccountRemark = (EditText) findViewById(R.id.m_account_remark);
        mAddAccountColorLayout = (LinearLayout) findViewById(R.id.add_account_color_layout);
        mAddAccountImageLayout = (RelativeLayout) findViewById(R.id.add_account_image_layout);
        mAddAccountImageLayout.setOnClickListener(this);
        mAddAccountColorLayout.setOnClickListener(this);
        mAddAccountImage = (ImageView) findViewById(R.id.account_diy_image);
        mAddAccountColor = (TextView) findViewById(R.id.add_account_color);
        mSaveButton = (Button) findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(this);

        mAccountPreselect = ContextCompat.getColor(this, R.color.account_add_color);
        colorTorgb(mAccountPreselect);
    }

    private void accountEditTwoNom() {
        mAddAccountMoneysum.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.contains(".")) {
                    int index = text.indexOf(".");
                    if (index + 3 < text.length()) {
                        text = text.substring(0, index + 3);
                        mAddAccountMoneysum.setText(text);
                        mAddAccountMoneysum.setSelection(text.length());
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_account_color_layout:
                new ColorChooserDialog.Builder(this, R.string.add_account_color_select)
                        .titleSub(R.string.app_name)
                        .preselect(mAccountPreselect)
                        .customColors(ColorPalette.PRIMARY_COLORS, null)
                        .show();
                break;
            case R.id.add_account_image_layout:
                Intent intent = new Intent(this, ChooseAccountImageActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CHOOSE_IMAGE);
                break;

            case R.id.save_button:
                saveAccount();
                break;
        }
    }

    private void saveAccount() {
        final String moneysum = mAddAccountMoneysum.getText().toString();
        final String name =  mAddAccountName.getText().toString();
        if(CommonUtility.isEmpty(moneysum)){
            Snackbar.make(findViewById(R.id.app_title), R.string.add_account_moneysum_enter, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        if(CommonUtility.isEmpty(name)){
            Snackbar.make(findViewById(R.id.app_title), R.string.add_account_name_enter, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        final DiAccount diAccount = new DiAccount(Double.parseDouble(moneysum), ApplicationData.mDiUserInfo.getName(),
                ApplicationData.mDiUserInfo.getId(), name, CommonConstants.INCOME_ACCOUNT_TYPE_USER, mAccountTypeImage,
                mSelectedColor, 100, mAccountRemark.getText().toString(), CommonUtility.getCurrentTime(), CommonUtility.getCurrentTime(), 0, CommonConstants.DELETEFALAG_NOTDELETE);
        if(mAccountId > 0){
            diAccount.setId(mAccountId);
            diAccount.setServerid(mAccountUpdate.getServerid());
        }
        SaDataProccessHandler<Void, Void, IdInfo> dataVerHandler = new
                SaDataProccessHandler<Void, Void, IdInfo>(AddAccountActivity.this) {
                    @Override
                    public void onSuccess(IdInfo data) {
                        if(mAccountId > 0){
                            DIAccountService.getInstance(AddAccountActivity.this).update(diAccount);
                            Toast.makeText(getContext(), getString(R.string.title_activity_update_account_sess), Toast.LENGTH_SHORT).show();
                            AddAccountActivity.this.finish();
                        }else {
                            diAccount.setServerid(data.getId());
                            DIAccountService.getInstance(AddAccountActivity.this).save(diAccount);
                            Toast.makeText(getContext(), getString(R.string.title_activity_add_account_sess), Toast.LENGTH_SHORT).show();
                            //Snackbar.make(findViewById(R.id.app_title), getString(R.string.title_activity_add_account_sess), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            AddAccountActivity.this.finish();
                        }
                    }
                };
        IncomeService.getInstance().addAccount(diAccount, dataVerHandler);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        mAddAccountColor.setBackgroundColor(selectedColor);
        colorTorgb(selectedColor);
    }

    private void colorTorgb(@ColorInt int selectedColor) {
        int red = (selectedColor & 0xff0000) >> 16;
        int green = (selectedColor & 0x00ff00) >> 8;
        int blue = (selectedColor & 0x0000ff);
        mSelectedColor = red + "," + green + "," + blue;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_IMAGE) {
            mAccountTypeImage = data.getStringExtra("diaccountimage");
            mAddAccountImage.setImageResource(CommonUtility.getImageIdByName(this, mAccountTypeImage));
        }
    }

    private void deleteAccount(final int id, final int localId) {
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new
                SaDataProccessHandler<Void, Void, Void>(AddAccountActivity.this) {
                    @Override
                    public void onSuccess(Void data) {
                        DIAccountService.getInstance(AddAccountActivity.this).delete(localId);
                        //Snackbar.make(findViewById(R.id.app_title), getString(R.string.profile_type_delete_sess), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        Toast.makeText(AddAccountActivity.this, R.string.title_activity_delete_account_sess, Toast.LENGTH_SHORT).show();
                        AddAccountActivity.this.finish();
                    }
                };
        IncomeService.getInstance().deleteAccount(id, dataVerHandler);
    }
}
