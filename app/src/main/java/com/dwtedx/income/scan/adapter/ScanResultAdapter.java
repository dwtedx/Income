package com.dwtedx.income.scan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dwtedx.income.R;
import com.dwtedx.income.entity.ScanTicketInfo;
import com.dwtedx.income.sqliteservice.DIScanService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by A150189 on 2016/11/24.
 */
public class ScanResultAdapter extends RecyclerView.Adapter<ScanResultAdapter.ViewHolder> {

    private Context mContext;
    private List<ScanTicketInfo> mScanTicketInfoList;

    private OnValueEditAfterTextChangeListener mOnValueEditAfterTextChangeListener;

    public ScanResultAdapter(Context mContext, List<ScanTicketInfo> infoList) {
        this.mContext = mContext;
        this.mScanTicketInfoList = infoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_scan_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ScanTicketInfo data = mScanTicketInfoList.get(position);
        if(data.isAdd()){
            holder.mItemDetailView.setVisibility(View.GONE);
            holder.mItemAddView.setVisibility(View.VISIBLE);
            holder.mItemAddIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mScanTicketInfoList.add(holder.getAdapterPosition(), new ScanTicketInfo());
                    notifyItemInserted(holder.getAdapterPosition());
                }
            });
        }else {
            holder.mItemDetailView.setVisibility(View.VISIBLE);
            holder.mItemAddView.setVisibility(View.GONE);
            holder.mItemNameEditText.setText(data.getName());
            holder.mItemQuantityEditText.setText(String.valueOf(data.getQuantity()));
            holder.mItemValueEditText.setText(String.valueOf(data.getValue()));
            //设置name
            holder.mItemNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    try {
                        data.setName(((EditText)v).getText().toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            //设置数量
            holder.mItemQuantityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    try {
                        data.setQuantity(Integer.parseInt(((EditText)v).getText().toString()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            //设置金额
            holder.mItemValueEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(null != mOnValueEditAfterTextChangeListener){
                        try {
                            data.setValue(Double.parseDouble(((EditText)v).getText().toString()));
                        }catch (Exception e){
                            e.printStackTrace();
                            data.setValue(0);
                        }
                        mOnValueEditAfterTextChangeListener.OnValueEditAfterTextChanged(((EditText)v).getText().toString());
                    }
                }
            });
            //删除
            holder.mItemDeleteIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mScanTicketInfoList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    if(null != mOnValueEditAfterTextChangeListener){
                        mOnValueEditAfterTextChangeListener.OnValueEditAfterTextChanged(null);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mScanTicketInfoList != null ? mScanTicketInfoList.size() : 0;
    }

    public void setmOnValueEditAfterTextChangeListener(OnValueEditAfterTextChangeListener mOnValueEditAfterTextChangeListener) {
        this.mOnValueEditAfterTextChangeListener = mOnValueEditAfterTextChangeListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.m_item_name_edit_text)
        EditText mItemNameEditText;
        @BindView(R.id.m_item_quantity_edit_text)
        EditText mItemQuantityEditText;
        @BindView(R.id.m_item_value_edit_text)
        EditText mItemValueEditText;
        @BindView(R.id.m_item_delete_icon_view)
        ImageView mItemDeleteIconView;
        @BindView(R.id.m_item_detail_view)
        RelativeLayout mItemDetailView;
        @BindView(R.id.m_item_add_icon_view)
        ImageView mItemAddIconView;
        @BindView(R.id.m_item_add_view)
        RelativeLayout mItemAddView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnValueEditAfterTextChangeListener{
        void OnValueEditAfterTextChanged(String s);
    }
}
