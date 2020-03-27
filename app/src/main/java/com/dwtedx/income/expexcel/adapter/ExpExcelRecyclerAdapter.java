package com.dwtedx.income.expexcel.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;
import com.dwtedx.income.entity.DiExpexcel;
import com.dwtedx.income.expexcel.ExpExcelActivity;
import com.dwtedx.income.expexcel.ExpExcelListActivity;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class ExpExcelRecyclerAdapter extends RecyclerView.Adapter<ExpExcelRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<DiExpexcel> mList;

    public ExpExcelRecyclerAdapter(Context mContext, List<DiExpexcel> infoList) {
        this.mContext = mContext;
        this.mList = infoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exp_excel_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DiExpexcel data = mList.get(position);
        holder.mItemNameTextView.setText(data.getName());
        holder.mItemStatusTextView.setText(getStatusStr(data.getStatus()));
        holder.mItemRoleTextView.setText(getRoleStr(data.getRole()));
        holder.mItemMoneyTextView.setText(getMoneyStr(data.getMoneysumstart(), data.getMoneysumend()));
        holder.mItemTypeTextView.setText(CommonUtility.isEmpty(data.getType()) ? "不限" : data.getType());
        holder.mItemAccountTextView.setText(CommonUtility.isEmpty(data.getAccount()) ? "不限" : data.getAccount());
        holder.mItemRecoretimeTextView.setText(getRecoreTime(data.getRecordtimestart(), data.getRecordtimeend()));
        holder.mItemTimeTextView.setText(data.getCreatetime());
        if (CommonConstants.STATUS_EXPDONE == data.getStatus()) {
            holder.mItemExcelDownView.setVisibility(View.VISIBLE);
            holder.mItemExcelTextView.setText(data.getFilepath());
            holder.mItemExcelTextView.setOnClickListener(v->{
                openBrowser(mContext, data.getFilepath());
            });
        } else {
            holder.mItemExcelDownView.setVisibility(View.GONE);
        }
    }

    /**
     * 调用第三方浏览器打开
     *
     * @param context
     * @param url     要浏览的资源地址
     */
    public static void openBrowser(Context context, String url) {
        new MaterialDialog.Builder(context)
                .title(R.string.tip)
                .content(R.string.exp_excel_down_tip)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        final Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
                        // 官方解释 : Name of the component implementing an activity that can display the intent
                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                            final ComponentName componentName = intent.resolveActivity(context.getPackageManager()); // 打印Log   ComponentName到底是什么 L.d("componentName = " + componentName.getClassName());
                            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
                        } else {
                            Toast.makeText(context.getApplicationContext(), "请下载浏览器", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }

    private String getRecoreTime(String start, String end) {
        String str = "";
        if (!CommonUtility.isEmpty(start)) {
            str += start + " - ";
        } else {
            str += "不限 - ";
        }
        if (!CommonUtility.isEmpty(end)) {
            str += end;
        } else {
            str += "不限";
        }
        return str;
    }

    private String getMoneyStr(Double start, Double end) {
        String str = "";
        if (null != start && start > 0) {
            str += start + " - ";
        } else {
            str += "不限 - ";
        }
        if (null != end && end > 0) {
            str += end;
        } else {
            str += "不限";
        }
        return str;
    }

    private String getRoleStr(int role) {
        String str = null;
        switch (role) {
            case CommonConstants.INCOME_ROLE_ALL:
                str = "全部";
                break;
            case CommonConstants.INCOME_ROLE_INCOME:
                str = "收入";
                break;
            case CommonConstants.INCOME_ROLE_PAYING:
                str = "支出";
                break;
        }
        return str;
    }

    private String getStatusStr(int satus) {
        String str = null;
        switch (satus) {
            case CommonConstants.STATUS_EXPNEW:
                str = "新建";
                break;
            case CommonConstants.STATUS_EXPING:
                str = "导出中";
                break;
            case CommonConstants.STATUS_EXPDONE:
                str = "导出完成";
                break;
            case CommonConstants.STATUS_EXPERR:
                str = "导出失败";
                break;
        }
        return str;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.m_item_name_text_view)
        TextView mItemNameTextView;
        @BindView(R.id.m_item_status_text_view)
        TextView mItemStatusTextView;
        @BindView(R.id.m_item_role_text_view)
        TextView mItemRoleTextView;
        @BindView(R.id.m_item_money_text_view)
        TextView mItemMoneyTextView;
        @BindView(R.id.m_item_type_text_view)
        TextView mItemTypeTextView;
        @BindView(R.id.m_item_account_text_view)
        TextView mItemAccountTextView;
        @BindView(R.id.m_item_recoretime_text_view)
        TextView mItemRecoretimeTextView;
        @BindView(R.id.m_item_time_text_view)
        TextView mItemTimeTextView;
        @BindView(R.id.m_item_excel_text_view)
        TextView mItemExcelTextView;
        @BindView(R.id.m_item_excel_down_view)
        LinearLayout mItemExcelDownView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
