package com.dwtedx.income.account;

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.sqliteservice.DIAccountService;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.AppTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountTransferActivity extends BaseActivity implements OnClickListener, AppTitleBar.OnTitleClickListener {

    @BindView(R.id.m_app_title_bar)
    AppTitleBar mAppTitleBar;
    @BindView(R.id.m_account_out)
    TextView mAccountOut;
    @BindView(R.id.m_account_in)
    TextView mAccountIn;
    @BindView(R.id.m_account_moneysum)
    EditText mAccountMoneysum;
    @BindView(R.id.save_button)
    Button mSaveButton;

    private int mAccountId;
    private int mCreditCardWhich = 0;
    private DiAccount mDiAccountOut;
    private DiAccount mDiAccountIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_transfer);
        ButterKnife.bind(this);

        mAccountId = getIntent().getIntExtra("ACCOUNTID", 1);
        mDiAccountOut = DIAccountService.getInstance(this).find(mAccountId);
        mAccountOut.setText(mDiAccountOut.getName());

        mAppTitleBar.setOnTitleClickListener(this);
        mAccountIn.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.m_account_in:
                showAccount();
                break;

            case R.id.save_button:
                save();
                break;
        }
    }

    private void save(){
        if(null == mDiAccountIn){
            Snackbar.make(mAccountMoneysum, R.string.add_account_transfer_in_tip, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        String money = mAccountMoneysum.getText().toString();
        if(CommonUtility.isEmpty(money)){
            Snackbar.make(mAccountMoneysum, R.string.add_account_transfer_money_tip, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        //加钱
        mDiAccountIn.setMoneysum(mDiAccountIn.getMoneysum() + Double.parseDouble(money));
        DIAccountService.getInstance(this).update(mDiAccountIn);
        //减钱
        mDiAccountOut.setMoneysum(mDiAccountOut.getMoneysum() - Double.parseDouble(money));
        DIAccountService.getInstance(this).update(mDiAccountOut);
        Toast.makeText(this, R.string.add_account_transfer_success, Toast.LENGTH_SHORT).show();
        finish();
        return;
    }

    private void showAccount(){
        final List<DiAccount> diAccountList= DIAccountService.getInstance(this).findAll();
        final List<String> diAccountString = new ArrayList<>();
        for (DiAccount acctout: diAccountList) {
            diAccountString.add(acctout.getName());
        }
        new MaterialDialog.Builder(this)
                .title(R.string.add_account_transfer_in)
                .items(diAccountString)
                .itemsCallbackSingleChoice(mCreditCardWhich, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        mAccountIn.setText(text);
                        mCreditCardWhich = which;
                        for (DiAccount acctout: diAccountList) {
                            if(acctout.getName().equals(text.toString())){
                                mDiAccountIn = acctout;
                                break;
                            }
                        }


                        return true; // allow selection
                    }
                })
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .show();
    }

}
