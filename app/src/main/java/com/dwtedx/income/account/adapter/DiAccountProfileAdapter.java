package com.dwtedx.income.account.adapter;

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
import com.dwtedx.income.utility.CommonUtility;

import java.util.List;

public class DiAccountProfileAdapter extends ArrayAdapter<DiAccount> {

    private Context mContext;
    private final LayoutInflater layoutInflater;

    public DiAccountProfileAdapter(Context context, List<DiAccount> mDiAccountItems) {
        super(context, 0, mDiAccountItems);
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_account_profile_item, parent, false);
            viewHolder = new ViewHolderItem();
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.account_name = (TextView) convertView.findViewById(R.id.account_name);
            viewHolder.account_name_tip = (TextView) convertView.findViewById(R.id.account_name_tip);
            viewHolder.account_name_money = (TextView) convertView.findViewById(R.id.account_name_money);
            viewHolder.mAccountLayout = (RelativeLayout) convertView.findViewById(R.id.account_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        DiAccount mDiAccount = getItem(position);
        Glide.with(mContext).load(CommonUtility.getImageIdByName(mDiAccount.getIcon())).into(viewHolder.mImageView);

        String[] colorArr = mDiAccount.getColor().split(",");
        int color = Color.rgb(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2]));
        if (Build.VERSION.SDK_INT >= 16)
        {
            Drawable mDrawable = ContextCompat.getDrawable(mContext, R.drawable.account_bg);
            mDrawable.setColorFilter(color, PorterDuff.Mode.SRC);
            viewHolder.mAccountLayout.setBackground(mDrawable);
        }else{
            viewHolder.mAccountLayout.setBackgroundColor(color);
        }

        viewHolder.account_name.setText(mDiAccount.getName());
        viewHolder.account_name_tip.setText(mDiAccount.getName() + "帐户余额");
        viewHolder.account_name_money.setText("￥" + CommonUtility.doubleFormat(mDiAccount.getMoneysum()));
        return convertView;
    }

    class ViewHolderItem {
        TextView account_name, account_name_tip, account_name_money;
        ImageView mImageView;
        RelativeLayout mAccountLayout;
    }
}
