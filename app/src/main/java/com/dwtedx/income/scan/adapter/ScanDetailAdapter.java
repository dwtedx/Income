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
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.entity.DiScan;
import com.dwtedx.income.scan.ScanDetailActivity;
import com.dwtedx.income.service.IncomeService;
import com.dwtedx.income.sqliteservice.DIScanService;
import com.dwtedx.income.utility.CommonConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by A150189 on 2016/11/24.
 */
public class ScanDetailAdapter extends RecyclerView.Adapter<ScanDetailAdapter.ViewHolder> {

    private Context mContext;
    private List<DiScan> mDiScanList;

    private OnValueEditAfterTextChangeListener mOnValueEditAfterTextChangeListener;

    public ScanDetailAdapter(Context mContext, List<DiScan> infoList) {
        this.mContext = mContext;
        this.mDiScanList = infoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_scan_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DiScan data = mDiScanList.get(position);
        if(CommonConstants.INCOME_SCAN_ADDBUTTON == data.getAddbutton()){
            holder.mItemDetailView.setVisibility(View.GONE);
            holder.mItemAddView.setVisibility(View.VISIBLE);
            holder.mItemAddIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDiScanList.add(holder.getAdapterPosition(), new DiScan());
                    notifyItemInserted(holder.getAdapterPosition());
                }
            });
        }else {
            holder.mItemDetailView.setVisibility(View.VISIBLE);
            holder.mItemAddView.setVisibility(View.GONE);
            holder.mItemQuantityEditText.setText(String.valueOf(data.getQuantity()));
            holder.mItemNameEditText.setText(data.getName());
            holder.mItemValueEditText.setText(String.valueOf(data.getMoneysum()));
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
                            data.setMoneysum(Double.parseDouble(((EditText)v).getText().toString()));
                        }catch (Exception e){
                            e.printStackTrace();
                            data.setMoneysum(0);
                        }
                        mOnValueEditAfterTextChangeListener.OnValueEditAfterTextChanged(((EditText)v).getText().toString());
                    }
                }
            });
            //删除
            holder.mItemDeleteIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //同步服务器数据库
                    if(data.getIsupdate() == CommonConstants.INCOME_RECORD_UPDATEED){
                        //同步数据库
                        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>((ScanDetailActivity) mContext) {
                            @Override
                            public void onSuccess(Void voidData) {
                                DIScanService.getInstance(mContext).delete(data.getId());
                                //从界面移除
                                mDiScanList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                if(null != mOnValueEditAfterTextChangeListener){
                                    mOnValueEditAfterTextChangeListener.OnValueEditAfterTextChanged(null);
                                }
                            }
                        };
                        IncomeService.getInstance().scanDeleteByServerId(data, dataVerHandler);
                    }else {
                        DIScanService.getInstance(mContext).delete(data.getId());
                        //从界面移除
                        mDiScanList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        if(null != mOnValueEditAfterTextChangeListener){
                            mOnValueEditAfterTextChangeListener.OnValueEditAfterTextChanged(null);
                        }
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mDiScanList != null ? mDiScanList.size() : 0;
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
