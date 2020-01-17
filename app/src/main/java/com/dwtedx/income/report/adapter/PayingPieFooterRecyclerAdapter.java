package com.dwtedx.income.report.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.github.mikephil.charting.components.LegendEntry;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class PayingPieFooterRecyclerAdapter extends RecyclerView.Adapter<PayingPieFooterRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private LegendEntry[] mLegendEntry;//标签文本 颜色集合

    public PayingPieFooterRecyclerAdapter(Context mContext, LegendEntry[] legendEntry) {
        this.mContext = mContext;
        this.mLegendEntry = legendEntry;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_paying_pic_footer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItemColorView.setBackgroundColor(mLegendEntry[position].formColor);
        holder.mItemDescView.setText(mLegendEntry[position].label);
    }

    @Override
    public int getItemCount() {
        return mLegendEntry != null ? mLegendEntry.length : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.m_item_color_view)
        TextView mItemColorView;
        @BindView(R.id.m_item_desc_view)
        TextView mItemDescView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
