package com.dwtedx.income.profile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dwtedx.income.R;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.entity.DiBudget;
import com.dwtedx.income.utility.CommonUtility;

import java.util.List;

public class DiBudgetAdapter extends ArrayAdapter<DiBudget> {

    private Context mContext;
    private final LayoutInflater layoutInflater;

    public DiBudgetAdapter(Context context, List<DiBudget> mDiAccountItems) {
        super(context, 0, mDiAccountItems);
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_budget_item, parent, false);
            viewHolder = new ViewHolderItem();
            viewHolder.time_textview = (TextView) convertView.findViewById(R.id.time_textview);
            viewHolder.money_textView = (TextView) convertView.findViewById(R.id.money_textView);
            viewHolder.money_last_textView = (TextView) convertView.findViewById(R.id.money_last_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        DiBudget mDiBudget = getItem(position);
        viewHolder.time_textview.setText(mDiBudget.getYearnom() + mContext.getString(R.string.budget_year) + mDiBudget.getMonthnom() + mContext.getString(R.string.budget_month_str));
        viewHolder.money_textView.setText("￥" + CommonUtility.twoPlaces(mDiBudget.getMoneysum()));
        viewHolder.money_last_textView.setText("￥" + CommonUtility.twoPlaces(mDiBudget.getMoneylast()));
        return convertView;
    }

    class ViewHolderItem {
        TextView time_textview, money_textView, money_last_textView;
    }
}
