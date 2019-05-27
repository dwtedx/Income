package com.dwtedx.income.home.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by A150189 on 2016/4/1.
 */
public class SearchListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<DiIncome> mList;
    private Context mContext;

    /**
     * @param
     */
    public SearchListAdapter(Context mContext, List<DiIncome> mCarsList) {
        super();
        this.mList = mCarsList;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder mHolder;
        if (convertView == null) {
            mHolder = new Holder();
            convertView = mInflater.inflate(R.layout.adapter_searchlist, null);
            mHolder.time_textview = (TextView) convertView.findViewById(R.id.time_textview);
            mHolder.account_textview = (TextView) convertView.findViewById(R.id.account_textview);
            mHolder.role_textview = (TextView) convertView.findViewById(R.id.role_textview);
            mHolder.type_textView = (TextView) convertView.findViewById(R.id.type_textView);
            mHolder.money_textview = (TextView) convertView.findViewById(R.id.money_textview);
            mHolder.remark_textview = (TextView) convertView.findViewById(R.id.remark_textview);
            mHolder.type_imageView = (ImageView) convertView.findViewById(R.id.type_imageView);
            mHolder.remark_textview_layout = convertView.findViewById(R.id.remark_textview_layout);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        if (mList != null && mList.size() > 0) {
            DiIncome mDiIncome = mList.get(position);
            mDiIncome.setRecordtimeformat(getDate(mDiIncome.getRecordtime(), "MM-dd HH:mm"));

            if(mDiIncome.getRole() != CommonConstants.INCOME_ROLE_START) {

                mHolder.time_textview.setText(mDiIncome.getRecordtimeformat());//为ViewHolder里的控件设置值
                mHolder.time_textview.setTextColor(ContextCompat.getColor(mContext, R.color.common_body_tip_colors));
                mHolder.type_imageView.setImageResource(CommonUtility.getImageIdByName(mDiIncome.getIcon()));//为ViewHolder里的控件设置值
                mHolder.account_textview.setText(mDiIncome.getAccount());

                //支出或收入
                switch (mDiIncome.getRole()) {
                    case CommonConstants.INCOME_ROLE_INCOME:
                        mHolder.role_textview.setText(mHolder.role_textview.getContext().getString(R.string.record_income));
                        if(null == mDiIncome.getIcon()){
                            mHolder.type_imageView.setImageResource(R.mipmap.icon_shouru_type_diy);
                        }
                        break;

                    case CommonConstants.INCOME_ROLE_PAYING:
                        mHolder.role_textview.setText(mHolder.role_textview.getContext().getString(R.string.record_pay));
                        if(null == mDiIncome.getIcon()){
                            mHolder.type_imageView.setImageResource(R.mipmap.icon_zhichu_type_diy);
                        }
                        break;
                }

                mHolder.type_textView.setText(mDiIncome.getType());
                mHolder.type_textView.setTextColor(ContextCompat.getColor(mContext, R.color.common_body_color));
                mHolder.money_textview.setText("￥" + CommonUtility.doubleFormat(mDiIncome.getMoneysum()));
                if(!TextUtils.isEmpty(mDiIncome.getRemark())) {
                    mHolder.remark_textview.setText(mDiIncome.getRemark());
                    mHolder.remark_textview_layout.setVisibility(View.VISIBLE);
                }else{
                    mHolder.remark_textview_layout.setVisibility(View.GONE);
                }
            }else{
                mDiIncome.setRecordtimeformat(getDate(mDiIncome.getRecordtime(), "yyyy-MM-dd HH:mm:ss"));
                mHolder.time_textview.setText(mDiIncome.getRecordtimeformat());//为ViewHolder里的控件设置值
                mHolder.time_textview.setTextColor(ContextCompat.getColor(mContext, R.color.common_body_tip_color));
                mHolder.type_imageView.setImageResource(R.mipmap.time_line_start);//为ViewHolder里的控件设置值
                mHolder.account_textview.setText("");
                mHolder.role_textview.setText("");
                mHolder.type_textView.setText(mHolder.type_textView.getContext().getString(R.string.money_line_start_tip));
                mHolder.type_textView.setTextColor(ContextCompat.getColor(mContext, R.color.common_body_tip_color));
                mHolder.money_textview.setText("");
                mHolder.remark_textview.setText("");
                mHolder.remark_textview_layout.setVisibility(View.GONE);
            }
        }
        return convertView;

    }

    private String getDate(String date, String format) {
        Date d = CommonUtility.stringToDate(date);
        if (null == d) {
            return null;
        }
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        String srt = format1.format(d);
        return srt;
    }

    class Holder{
        TextView time_textview, account_textview, role_textview, type_textView, money_textview, remark_textview;
        ImageView type_imageView;
        View remark_textview_layout;
    }
}
