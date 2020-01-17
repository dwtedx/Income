package com.dwtedx.income.profile;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.afollestad.materialdialogs.util.DialogUtils;
import com.bumptech.glide.Glide;
import com.dwtedx.income.R;
import com.dwtedx.income.account.AccountActivity;
import com.dwtedx.income.accounttype.IncomeTypeActivity;
import com.dwtedx.income.accounttype.PayingTypeActivity;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.discovery.DiscoveryActivity;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.expexcel.ExpExcelActivity;
import com.dwtedx.income.report.ShareActivity;
import com.dwtedx.income.topic.MyTopicActivity;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.CircleImageView;
import com.dwtedx.income.widget.theme.CircleView;
import com.dwtedx.income.widget.theme.ColorChooserDialog;

public class ProfileFragment extends BaseFragment implements View.OnClickListener, ColorChooserDialog.ColorCallback {

    private View mView;
    private CircleImageView mHeadImageView;
    private TextView mHeadName;
    private TextView mSignatureView;
    private int[] mClickView = {R.id.nav_header_view, R.id.profile_type_pay, R.id.profile_type_income, R.id.profile_account,
            R.id.setup_reset_pass, R.id.profile_theme, R.id.profile_accent, R.id.profile_setup, R.id.profile_share, R.id.profile_expexcel,
            R.id.profile_budget, R.id.profile_nav_setup, R.id.m_profile_discover_button, R.id.m_profile_topic_button };

    // color chooser dialog
    private int primaryPreselect;
    private int accentPreselect;

    private LinearLayout mRightLayout;
    private RelativeLayout mProfileSellLayout;
    private LinearLayout mProfileSellLayoutLine;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRightLayout = (LinearLayout) view.findViewById(R.id.home_item_layout);
        mRightLayout.setOnClickListener(this);

        if(CommonConstants.APP_VERSION_AUDIT_PASS == ApplicationData.mAppVersionAudit){
            mProfileSellLayout = (RelativeLayout) view.findViewById(R.id.ic_profile_sell_layout);
            mProfileSellLayoutLine = (LinearLayout) view.findViewById(R.id.ic_profile_sell_layout_line);
            mProfileSellLayout.setVisibility(View.VISIBLE);
            mProfileSellLayoutLine.setVisibility(View.VISIBLE);
        }

        mHeadImageView = (CircleImageView) mView.findViewById(R.id.imageView);
        mHeadName = (TextView) mView.findViewById(R.id.usernameView);
        mSignatureView = (TextView) mView.findViewById(R.id.userSignatureView);

        for (int id : mClickView) {
            mView.findViewById(id).setOnClickListener(this);
        }

        primaryPreselect = DialogUtils.resolveColor(mFragmentContext, R.attr.colorPrimary);
        accentPreselect = DialogUtils.resolveColor(mFragmentContext, R.attr.colorAccent);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (super.isLogin()) {
            Glide.with(this).load(ApplicationData.mDiUserInfo.getHead()).into(mHeadImageView);
            String userName = ApplicationData.mDiUserInfo.getName();
            if(CommonUtility.isEmpty(userName)){
                userName = ApplicationData.mDiUserInfo.getPhone();
            }
            if(CommonUtility.isEmpty(userName)){
                userName = ApplicationData.mDiUserInfo.getUsername();
            }
            mHeadName.setText(userName);

            String signature = ApplicationData.mDiUserInfo.getSignature();
            if(CommonUtility.isEmpty(signature)){
                signature = getString(R.string.app_slogan);
            }
            mSignatureView.setText(signature);
        } else {
            Glide.with(this).load(R.mipmap.userhead).into(mHeadImageView);
            mHeadName.setText(getString(R.string.profile_login));
            mSignatureView.setText(getString(R.string.app_slogan));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header_view:
                if (isLogin()) {
                    startActivity(new Intent(mFragmentContext, ProfileInfoActivity.class));
                } else {
                    startActivity(new Intent(mFragmentContext, LoginV2Activity.class));
                }
                break;
            case R.id.profile_type_pay:
                startActivity(new Intent(mFragmentContext, PayingTypeActivity.class));
                break;
            case R.id.profile_type_income:
                startActivity(new Intent(mFragmentContext, IncomeTypeActivity.class));
                break;
            case R.id.profile_share:
                startActivity(new Intent(mFragmentContext, ShareActivity.class));
                break;
            case R.id.profile_expexcel:
                startActivity(new Intent(mFragmentContext, ExpExcelActivity.class));
                break;
            case R.id.profile_account:
                startActivity(new Intent(mFragmentContext, AccountActivity.class));
                break;
            case R.id.setup_reset_pass:
                break;
//            case R.id.profile_theme:
//                new ColorChooserDialog.Builder(mFragmentContext, R.string.color_palette)
//                        .titleSub(R.string.app_name)
//                        .preselect(primaryPreselect)
//                        .show();
//                break;
//            case R.id.profile_accent:
//                new ColorChooserDialog.Builder(mFragmentContext, R.string.color_palette)
//                        .titleSub(R.string.app_name)
//                        .accentMode(true)
//                        .preselect(accentPreselect)
//                        .show();
//                break;
            case R.id.profile_setup:
                startActivity(new Intent(mFragmentContext, ProfileInfoActivity.class));
                break;

            case R.id.profile_budget:
                startActivity(new Intent(mFragmentContext, BudgetActivity.class));
                break;

            case R.id.profile_nav_setup:
                startActivity(new Intent(mFragmentContext, SetupActivity.class));
                break;

            case R.id.home_item_layout:
                startActivity(new Intent(mFragmentContext, SetupActivity.class));
                break;

            case R.id.m_profile_discover_button:
                startActivity(new Intent(mFragmentContext, DiscoveryActivity.class));
                break;

            case R.id.m_profile_topic_button:
                if (isLogin()) {
                    startActivity(new Intent(mFragmentContext, MyTopicActivity.class));
                } else {
                    startActivity(new Intent(mFragmentContext, LoginV2Activity.class));
                }
                break;
        }
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int color) {
        if (dialog.isAccentMode()) {
            accentPreselect = color;
            ThemeSingleton.get().positiveColor = DialogUtils.getActionTextStateList(mFragmentContext, color);
            ThemeSingleton.get().neutralColor = DialogUtils.getActionTextStateList(mFragmentContext, color);
            ThemeSingleton.get().negativeColor = DialogUtils.getActionTextStateList(mFragmentContext, color);
            ThemeSingleton.get().widgetColor = color;
        } else {
            primaryPreselect = color;
            if (((BaseActivity)mFragmentContext).getSupportActionBar() != null)
                ((BaseActivity)mFragmentContext).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mFragmentContext.getWindow().setStatusBarColor(CircleView.shiftColorDown(color));
                mFragmentContext.getWindow().setNavigationBarColor(color);
            }
        }
    }
}
