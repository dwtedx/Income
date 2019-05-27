package com.dwtedx.income.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dwtedx.income.R;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.ReportUtil;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordKeyboardView extends RelativeLayout implements OnClickListener {

    View mView;
    TextView mRecordInputBack;

    int mInputType = 0;//0:常规 1:计算模式
    int INPUT_TYPE_ROUTINE = 0;//0:常规 1:加法2:减法
    int INPUT_TYPE_ADD = 1;//0:常规 1:加法2:减法
    int INPUT_TYPE_SUBTRACT = 2;//0:常规 1:加法2:减法

    TextView mrCeditCard;//帐户类型
    TextView mRecordTime;//时间

    TextView mRecordAccountEditText;
    TextView mRecordAccountCount;

    private int[] mClickView = {R.id.m_record_input_0, R.id.m_record_input_1, R.id.m_record_input_2, R.id.m_record_input_3,
            R.id.m_record_input_c, R.id.m_record_input_4, R.id.m_record_input_5, R.id.m_record_input_6, R.id.m_record_input_add,
            R.id.m_record_input_7, R.id.m_record_input_8, R.id.m_record_input_9, R.id.m_record_input_subtract, R.id.m_record_input_point,
            R.id.m_record_input_save, R.id.m_record_input_back, R.id.m_record_type_credit_card, R.id.m_record_type_time, R.id.m_record_remark};

    private OnKeyboardClickListener mListener;

    private boolean mIsC = false;

    public RecordKeyboardView(Context context) {
        super(context);
    }

    public RecordKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mView = LayoutInflater.from(context).inflate(R.layout.record_keyboard_view, this, true);
        mrCeditCard = (TextView) mView.findViewById(R.id.m_record_type_credit_card);
        mRecordTime = (TextView) mView.findViewById(R.id.m_record_type_time);
        for (int clickId : mClickView) {
            mView.findViewById(clickId).setOnClickListener(this);
        }

        mRecordInputBack = mView.findViewById(R.id.m_record_input_back);
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "ic_back_button.ttf");
        mRecordInputBack.setTypeface(customFont);
    }

    public void setRecordAccountTextView(TextView mRecordAccountEditText, TextView mRecordAccountCount) {
        this.mRecordAccountEditText = mRecordAccountEditText;
        this.mRecordAccountCount = mRecordAccountCount;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_record_type_credit_card:
            case R.id.m_record_type_time:
            case R.id.m_record_remark:
            case R.id.m_record_input_save:
                mListener.OnKeyboardClick(v);
                break;

            case R.id.m_record_input_c:
                setC();
                break;

            case R.id.m_record_input_back:
                setBack();
                break;

            case R.id.m_record_input_0:
            case R.id.m_record_input_1:
            case R.id.m_record_input_2:
            case R.id.m_record_input_3:
            case R.id.m_record_input_4:
            case R.id.m_record_input_5:
            case R.id.m_record_input_6:
            case R.id.m_record_input_7:
            case R.id.m_record_input_8:
            case R.id.m_record_input_9:
                setInputVal(v);
                break;
            case R.id.m_record_input_point:
                setPoint();
                break;
            case R.id.m_record_input_add:
                compute("+", INPUT_TYPE_ADD);
                break;
            case R.id.m_record_input_subtract:
                compute("-", INPUT_TYPE_SUBTRACT);
                break;

        }
    }

    /**
     * 0-9  + - . 按键处理
     *
     * @param  v
     */
    private void setInputVal(View v) {
        if(mIsC){
            setC();
            mIsC = false;
        }
        String nomber = (String)v.getTag();
        if(mInputType == INPUT_TYPE_ROUTINE){
            String strVal = mRecordAccountEditText.getText().toString();
            strVal += nomber;
            mRecordAccountEditText.setText(strVal);
            setTwo();
        }else {
            String strVal = mRecordAccountCount.getText().toString();
            strVal = strVal + nomber;
            mRecordAccountCount.setText(strVal);
            setTwo();
            computeNomber();
        }
    }

    /**
     * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/9/3 10:05.
     * Company 路之遥网络科技有限公司
     * Description 保留两位小米
     */
    private void setTwo() {
        if (mInputType == INPUT_TYPE_ROUTINE) {
            String strVal = mRecordAccountEditText.getText().toString();
            if (strVal.contains(".")) {
                int index = strVal.indexOf(".");
                if (index + 3 < strVal.length()) {
                    strVal = strVal.substring(0, index + 3);
                    mRecordAccountEditText.setText(strVal);
                }
            }
        } else {
            //小数位数
            String strVal = mRecordAccountCount.getText().toString();
            if (mInputType == INPUT_TYPE_ADD) {
                int indexofnom = strVal.lastIndexOf("+");
                if (indexofnom > -1) {
                    setTWoCount(strVal, indexofnom);
                }
            } else if (mInputType == INPUT_TYPE_SUBTRACT) {
                int indexofnom = strVal.lastIndexOf("-");
                if (indexofnom > -1) {
                    setTWoCount(strVal, indexofnom);
                }
            }
        }
    }


    private void setTWoCount(String strVal, int indexofnom) {
        String aStr = strVal.substring(0, indexofnom);
        String bStr = strVal.substring(indexofnom);
        if (bStr.contains(".")) {
            int index = bStr.indexOf(".");
            if (index + 3 < bStr.length()) {
                strVal = strVal.substring(0, (strVal.length() - 1));
                mRecordAccountCount.setText(strVal);
            }
        }
    }

    private void setPoint() {
        if (mInputType == INPUT_TYPE_ROUTINE) {
            String strVal = mRecordAccountEditText.getText().toString();
            if (CommonUtility.isEmpty(strVal)) {
                mRecordAccountEditText.setText("0.");
            } else {
                if (strVal.contains(".")) {
                    return;
                } else {
                    strVal += ".";
                    mRecordAccountEditText.setText(strVal);
                }
            }
        } else {
            String strVal = mRecordAccountCount.getText().toString();
            if (CommonUtility.isEmpty(strVal)) {
                mRecordAccountCount.setText(strVal + "0.");
            } else {
                if (isFrontNum(strVal)) {
                    mRecordAccountCount.setText(strVal + ".");
                }
            }
            setTwo();
        }
    }

    private void setBack() {
        if (mInputType == INPUT_TYPE_ROUTINE) {
            String accountStr = mRecordAccountEditText.getText().toString();
            if (!CommonUtility.isEmpty(accountStr)) {
                mRecordAccountEditText.setText(accountStr.substring(0, accountStr.length() - 1));
            }
        } else {
            String accountCountStr = mRecordAccountCount.getText().toString();
            if (!CommonUtility.isEmpty(accountCountStr)) {
                String newAccountCount = accountCountStr.substring(0, accountCountStr.length() - 1);
                mRecordAccountCount.setText(newAccountCount);
                computeNomber();
            }else{
                String accountStr = mRecordAccountEditText.getText().toString();
                if (!CommonUtility.isEmpty(accountStr)) {
                    mRecordAccountEditText.setText(accountStr.substring(0, accountStr.length() - 1));
                }else {
                    mInputType = INPUT_TYPE_ROUTINE;
                }
            }
        }
    }

    /**
     * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/9/2 20:49.
     * Company 路之遥网络科技有限公司
     * Description 最近一位是否是数字
     */
    private boolean isFrontNum(String countStr) {
        if (!CommonUtility.isEmpty(countStr)) {
            String endStr = countStr.substring((countStr.length() - 1));
            if (CommonUtility.isNumeric(endStr)) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    private void compute(String cipher, int inputType) {
        String computeStr = mRecordAccountCount.getText().toString();
        computeNomber();
        if(CommonUtility.isEmpty(computeStr)) {
            String recordNom = mRecordAccountEditText.getText().toString();
            if(!CommonUtility.isEmpty(recordNom)) {
                mRecordAccountCount.setText(recordNom + cipher);
                mInputType = inputType;
            }
        }else{
            if (!isFrontNum(computeStr)) return;
            mRecordAccountCount.setText(computeStr + cipher);
            mInputType = inputType;
        }
    }

    private void computeNomber() {
        try {
            //计算
            String equationg = mRecordAccountCount.getText().toString();
            if (CommonUtility.isEmpty(equationg)) {
                return;
            }if (!isFrontNum(equationg)) {
                return;
            }
            String val = ReportUtil.computeString(equationg);
            double dVal = Double.parseDouble(val);
            mRecordAccountEditText.setText(CommonUtility.twoPlaces(dVal));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), getContext().getString(R.string.add_account_count_tip), Toast.LENGTH_SHORT).show();
            setC();
        }

    }

    private void setC() {
        mInputType = INPUT_TYPE_ROUTINE;
        mRecordAccountEditText.setText("");
        mRecordAccountCount.setText("");
    }

    public TextView getMrCeditCard() {
        return mrCeditCard;
    }

    public TextView getmRecordTime() {
        return mRecordTime;
    }

    public void setmIsC(boolean mIsC) {
        this.mIsC = mIsC;
    }

    public void setOnKeyboardClickListener(OnKeyboardClickListener listener) {
        mListener = listener;
    }

    public interface OnKeyboardClickListener {
        void OnKeyboardClick(View v);

    }

}
