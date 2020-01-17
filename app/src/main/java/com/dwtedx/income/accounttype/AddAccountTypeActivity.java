package com.dwtedx.income.accounttype;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.entity.IdInfo;
import com.dwtedx.income.service.IncomeService;
import com.dwtedx.income.sqliteservice.DITypeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.AppTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAccountTypeActivity extends BaseActivity implements OnClickListener, AppTitleBar.OnTitleClickListener {


    @BindView(R.id.m_app_title)
    AppTitleBar mAppTitle;
    @BindView(R.id.m_type_name)
    EditText mTypeName;
    @BindView(R.id.m_type_diy_image)
    ImageView mTypeDiyImage;
    @BindView(R.id.m_add_type_image_layout)
    LinearLayout mAddTypeImageLayout;
    @BindView(R.id.m_add_type_color)
    TextView mAddTypeColor;
    @BindView(R.id.m_type_remark)
    EditText mTypeRemark;
    @BindView(R.id.m_save_button)
    Button mSaveButton;
    @BindView(R.id.m_add_type_color_layout)
    LinearLayout mAddTypeColorLayout;

    private int mTypePreselect;
    private String mSelectedColor;

    private int mIncomeRole;
    private int mTypeId;
    private DiType mDiTypeUpdate;

    private String mTypeImage;
    private static int REQUEST_CODE_CHOOSE_IMAGE = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account_type);
        ButterKnife.bind(this);
        mAppTitle.setOnTitleClickListener(this);
        mAddTypeImageLayout.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mAddTypeColorLayout.setOnClickListener(this);

        mIncomeRole = getIntent().getIntExtra("TYPEROLE", CommonConstants.INCOME_ROLE_PAYING);

        mTypeId = getIntent().getIntExtra("TYPEID", -1);
        if (mTypeId > 0) {
            mAppTitle.setRightVisibility(View.VISIBLE);
            mDiTypeUpdate = DITypeService.getInstance(AddAccountTypeActivity.this).find(mTypeId);
            mTypeName.setText(mDiTypeUpdate.getName());
            //图标
            mTypeImage = mDiTypeUpdate.getIcon();
            mTypeDiyImage.setImageResource(CommonUtility.getImageIdByName(this, mDiTypeUpdate.getIcon()));

            //颜色处理
            mSelectedColor = mDiTypeUpdate.getColor();
            String[] colorArr = mSelectedColor.split(",");
            mTypePreselect = Color.rgb(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2]));
            mAddTypeColor.setBackgroundColor(mTypePreselect);
            //备注
            mTypeRemark.setText(mDiTypeUpdate.getRemark());
        } else {
            mAppTitle.setRightVisibility(View.GONE);
            if(mIncomeRole == CommonConstants.INCOME_ROLE_PAYING){
                mSelectedColor = CommonConstants.PAYTYPE_DIY_COLOR;
                mTypeImage = CommonConstants.PAYTYPE_DIY_ICON;
            }else if(mIncomeRole == CommonConstants.INCOME_ROLE_INCOME){
                mSelectedColor = CommonConstants.INCOME_DIY_COLOR;
                mTypeImage = CommonConstants.INCOME_DIY_ICON;
            }
            mTypeDiyImage.setImageResource(CommonUtility.getImageIdByName(this, mTypeImage));
            //初始化颜色
            String[] colorArr = mSelectedColor.split(",");
            mTypePreselect = Color.rgb(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2]));
            mAddTypeColor.setBackgroundColor(mTypePreselect);
        }
    }

    @Override
    public void onTitleClick(int type) {
        switch (type) {
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                if(null != mDiTypeUpdate) {
                    deleteType(mDiTypeUpdate.getServerid(), mDiTypeUpdate.getId());
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_add_type_image_layout:
                Intent intent = new Intent(this, ChooseAccountTypeImageActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CHOOSE_IMAGE);
                break;

            case R.id.m_add_type_color_layout:
                Snackbar.make(findViewById(R.id.m_app_title), R.string.profile_type_color_tip, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;

            case R.id.m_save_button:
                saveType();
                break;
        }
    }

    private void saveType() {
        final String name = mTypeName.getText().toString();
        if (CommonUtility.isEmpty(name)) {
            Snackbar.make(findViewById(R.id.m_app_title), R.string.profile_type_name_tip, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        if (mTypeId > 0) {
            updateType();
        } else {
            addType();
        }
    }

    private void addType() {
        final DiType diType = new DiType(ApplicationData.mDiUserInfo.getName(), ApplicationData.mDiUserInfo.getId(), mTypeName.getText().toString(),
                mIncomeRole, mTypeImage, mSelectedColor, 100, mTypeRemark.getText().toString(), CommonUtility.getCurrentTime(),
                CommonUtility.getCurrentTime(), 0, CommonConstants.DELETEFALAG_NOTDELETE);

        SaDataProccessHandler<Void, Void, IdInfo> dataVerHandler = new
                SaDataProccessHandler<Void, Void, IdInfo>(AddAccountTypeActivity.this) {
                    @Override
                    public void onSuccess(IdInfo data) {
                        diType.setServerid(data.getId());
                        DITypeService.getInstance(AddAccountTypeActivity.this).save(diType);
                        Toast.makeText(getContext(), getString(R.string.profile_type_add_sess), Toast.LENGTH_SHORT).show();
                        AddAccountTypeActivity.this.finish();
                    }
                };
        IncomeService.getInstance().addType(diType, dataVerHandler);
    }

    private void updateType() {
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new
                SaDataProccessHandler<Void, Void, Void>(AddAccountTypeActivity.this) {
                    @Override
                    public void onSuccess(Void data) {
                        mDiTypeUpdate.setName(mTypeName.getText().toString());
                        mDiTypeUpdate.setIcon(mTypeImage);
                        mDiTypeUpdate.setColor(mSelectedColor);
                        mDiTypeUpdate.setRemark(mTypeRemark.getText().toString());
                        mDiTypeUpdate.setUpdatetime(CommonUtility.getCurrentTime());
                        DITypeService.getInstance(AddAccountTypeActivity.this).update(mDiTypeUpdate);
                        Toast.makeText(getContext(), getString(R.string.profile_type_update_sess), Toast.LENGTH_SHORT).show();
                        AddAccountTypeActivity.this.finish();
                    }
                };
        IncomeService.getInstance().updateType(mTypeName.getText().toString(), mTypeImage, mSelectedColor, mTypeRemark.getText().toString(), mDiTypeUpdate.getServerid(), dataVerHandler);
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
            mTypeImage = data.getStringExtra("ditypeimage");
            mTypeDiyImage.setImageResource(CommonUtility.getImageIdByName(this, mTypeImage));
            mSelectedColor = data.getStringExtra("ditypecolor");
            String[] colorArr = mSelectedColor.split(",");
            mTypePreselect = Color.rgb(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2]));
            mAddTypeColor.setBackgroundColor(mTypePreselect);
        }
    }

    private void deleteType(final int id, final int localId) {
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new
                SaDataProccessHandler<Void, Void, Void>(AddAccountTypeActivity.this) {
                    @Override
                    public void onSuccess(Void data) {
                        DITypeService.getInstance(AddAccountTypeActivity.this).delete(localId);
                        Toast.makeText(AddAccountTypeActivity.this, R.string.profile_type_delete_sess, Toast.LENGTH_SHORT).show();
                        AddAccountTypeActivity.this.finish();
                    }
                };
        IncomeService.getInstance().deleteType(id, dataVerHandler);
    }
}
