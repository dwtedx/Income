package com.dwtedx.income.addrecord;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.dwtedx.income.R;
import com.dwtedx.income.addrecord.adapter.PayingRecyclerViewAdatper;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.entity.DiBudget;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.accounttype.PayingTypeActivity;
import com.dwtedx.income.home.IncomeLineFragment;
import com.dwtedx.income.home.IncomeListActivity;
import com.dwtedx.income.provider.AccountSharedPreferences;
import com.dwtedx.income.service.IncomeService;
import com.dwtedx.income.sqliteservice.DIAccountService;
import com.dwtedx.income.sqliteservice.DIBudgetService;
import com.dwtedx.income.sqliteservice.DITypeService;
import com.dwtedx.income.sqliteservice.DlIncomeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.widget.RecordKeyboardView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PayingFragment extends BaseFragment implements RecordKeyboardView.OnKeyboardClickListener{
    //计算
    private TextView mRecordAccountEditText;
    private TextView mRecordAccountCount;

    //支出类型
    private List<DiType> mDiTypeList;
    private RecyclerView mRecyclerView;
    private PayingRecyclerViewAdatper adapter;
    private ImageView mCheckImageView;
    private TextView mCheckTextView;
    private int mCheckTypeId = 1;

    //帐户类型
    private MaterialDialog mTypeMaterialDialog;
    private int mAccountID;

    //时间
    private Calendar mCalendar;

    //备注
    private String mRecordRemarkStr;

    private RecordKeyboardView mRecordKeyboardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_pay, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);

        mDiTypeList = new ArrayList<>();

        mRecordAccountEditText = (TextView) mView.findViewById(R.id.record_account);
        mRecordAccountCount = (TextView) mView.findViewById(R.id.record_account_count);
        mCheckImageView = (ImageView) mView.findViewById(R.id.imageView);
        mCheckTextView = (TextView) mView.findViewById(R.id.textView);

        mRecordKeyboardView = (RecordKeyboardView) mView.findViewById(R.id.record_keyboard_view);
        mRecordKeyboardView.setRecordAccountTextView(mRecordAccountEditText, mRecordAccountCount);
        mRecordKeyboardView.setOnKeyboardClickListener(this);

        AccountSharedPreferences.init(getContext());
        String[] accoutArr = AccountSharedPreferences.getAccountStr().split("\\|");
        mAccountID = Integer.parseInt(accoutArr[0]);
        mRecordKeyboardView.getMrCeditCard().setText(accoutArr[1]);

        mCalendar = Calendar.getInstance();
        mRecordKeyboardView.getmRecordTime().setText(CommonUtility.stringDateFormartMMdd(mCalendar.getTime()));

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(getSpanCount() , StaggeredGridLayoutManager.VERTICAL));//设置RecyclerView的布局管理
//      mRecyclerView.addItemDecoration();//设置RecyclerView中item的分割线，用的少，一般都用在item中设置margin分隔两个item
//      mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置item的添加删除动画，采用默认的动画效果
        adapter = new PayingRecyclerViewAdatper(getContext(), mDiTypeList);
        mRecyclerView.setAdapter(adapter);//设置Adapter
        adapter.setOnItemClickListener(new PayingRecyclerViewAdatper.OnItemClickListener() {//添加监听器
            @Override
            public void onItemClick(View view, int postion) {
                if(CommonConstants.INCOME_ROLE_ADD_TYPE == mDiTypeList.get(postion).getType()){
                    Intent intent = new Intent(getContext(), PayingTypeActivity.class);
                    intent.putExtra("isShowAdd", true);
                    startActivity(intent);
                }else {
                    mCheckImageView.setImageResource(CommonUtility.getImageIdByName(getContext(), mDiTypeList.get(postion).getIcon()));
                    mCheckImageView.setAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                    mCheckTextView.setText(mDiTypeList.get(postion).getName());
                    mCheckTextView.setAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                    mCheckTypeId = mDiTypeList.get(postion).getId();
                }
            }

            @Override
            public void onItemLongClick(View view, int postion) {
                //Toast.makeText(getContext(), "长按的是：" + postion, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取屏幕分辨率
     * 相应的列数
     */
    private int getSpanCount() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
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
    public void onResume() {
        super.onResume();
        mDiTypeList.clear();
        mDiTypeList.addAll(DITypeService.getInstance(getContext()).findAll(CommonConstants.INCOME_ROLE_PAYING));
        mDiTypeList.add(new DiType(getString(R.string.profile_type_add_str), CommonConstants.INCOME_ROLE_ADD_TYPE, CommonConstants.INCOME_ROLE_ADD_TYPE_ICON));
        adapter.notifyDataSetChanged();
    }

    private void save() {
        if(CommonUtility.isEmpty(mRecordAccountEditText.getText().toString())){
            //Toast.makeText(getContext(), getContext().getString(R.string.record_money_error) , Toast.LENGTH_SHORT).show();
            Snackbar.make(mRecyclerView, R.string.record_money_error, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        //开始节点第一条记录处理 by sinyuu 20190920
        DiIncome beForDiIncome = DlIncomeService.getInstance(getContext()).findBeForTime();
        if(null != beForDiIncome) {
            Date beForTime = CommonUtility.stringToDate(beForDiIncome.getRecordtime());
            if (null != beForTime && mCalendar.getTime().before(beForTime)) {
                if (CommonConstants.INCOME_RECORD_UPDATEED == beForDiIncome.getIsupdate()) {
                    //同步数据库
                    SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>((BaseActivity) getActivity()) {
                        @Override
                        public void onSuccess(Void data) {
                            DlIncomeService.getInstance(getContext()).updateBeForTime(CommonUtility.stringDateFormartAddHours(mCalendar.getTime()));
                            saveIncome();
                        }
                    };
                    IncomeService.getInstance().updateIncomeBeforTime(CommonUtility.stringDateFormartAddHours(mCalendar.getTime()), dataVerHandler);
                } else {
                    DlIncomeService.getInstance(getContext()).updateBeForTime(CommonUtility.stringDateFormartAddHours(mCalendar.getTime()));
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

    private void saveIncome() {
        String chosedate = CommonUtility.stringDateFormart(mCalendar.getTime());
        String nowTime = CommonUtility.getCurrentTime();
        double money = Double.parseDouble(mRecordAccountEditText.getText().toString());
        mRecordAccountEditText.setText("");
        DiIncome income = new DiIncome(
                0, null, 0,  CommonConstants.INCOME_ROLE_PAYING, money,
                mCheckTextView.getText().toString(), mCheckTypeId, mRecordKeyboardView.getMrCeditCard().getText().toString(), mAccountID,
                mRecordRemarkStr, null,null, null, CommonConstants.INCOME_RECORD_TYPE_0, chosedate, nowTime, nowTime, CommonConstants.INCOME_RECORD_NOT_UPDATE, 0, CommonConstants.DELETEFALAG_NOTDELETE);
        DlIncomeService.getInstance(getContext()).save(income);

        //帐户金额修改
        DiAccount account = DIAccountService.getInstance(getContext()).find(mAccountID);
        account.setMoneysum(account.getMoneysum() - money);
        account.setUpdatetime(CommonUtility.getCurrentTime());
        DIAccountService.getInstance(getContext()).update(account);

        //预算
        DiBudget budget = DIBudgetService.getInstance(getContext()).findLastRow();
        budget.setMoneylast(budget.getMoneylast() - money);
        budget.setUpdatetime(CommonUtility.getCurrentTime());
        DIBudgetService.getInstance(getContext()).update(budget);

        IncomeLineFragment.mLoadIncome = true;
        IncomeListActivity.mLoadIncome = true;
        Toast.makeText(getContext(), getContext().getString(R.string.save_success) , Toast.LENGTH_SHORT).show();
        //Snackbar.make(mRecyclerView, R.string.save_success, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        //更新时间
        mCalendar = Calendar.getInstance();

        getActivity().finish();
    }

    @Override
    public void OnKeyboardClick(View v) {
        switch (v.getId()) {
            case R.id.m_record_type_credit_card:
                showTypeDialog();
                break;

            case R.id.m_record_type_time:
                DatePickerDialog dd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mCalendar.set(year, monthOfYear, dayOfMonth);
                        mRecordKeyboardView.getmRecordTime().setText(CommonUtility.stringDateFormartMMdd(mCalendar.getTime()));
                    }
                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                dd.show();
                break;

            case R.id.m_record_remark:
                new MaterialDialog.Builder(getContext())
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

    private void showTypeDialog() {
        if (null != mTypeMaterialDialog){
            mTypeMaterialDialog.show();
        }else {
            final List<DiAccount> diAccountList = DIAccountService.getInstance(getContext()).findAll();
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
                        new MaterialSimpleListItem.Builder(getContext())
                                .content(diAccountList.get(i).getName())
                                .id(diAccountList.get(i).getId())
                                .icon(CommonUtility.getImageIdByName(getContext(), diAccountList.get(i).getIcon()))
                                .iconPaddingDp(8)
                                .backgroundColor(Color.rgb(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2])))
                                .build());
            }
            mTypeMaterialDialog = new MaterialDialog.Builder(getContext())
                    .title(R.string.account_type_title)
                    .adapter(adapter, null)
                    .show();
        }
    }

}
