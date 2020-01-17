package com.dwtedx.income.scan.adapter;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.scan.ChooseLocationActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.ViewHolder> {



    private ChooseLocationActivity mContext;
    private List<String> mPoiList;

    public LocationRecyclerAdapter(ChooseLocationActivity mContext, List<String> poiList) {
        this.mContext = mContext;
        this.mPoiList = poiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_choose_location_item, parent, false);
        return new LocationRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String data = mPoiList.get(position);
        holder.mItemTitleView.setText(data);
        holder.mItemTitleLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  mPoiList.size();
    }

    private void setResult(String imgage){
        Intent intent = new Intent();
        intent.putExtra("POI", imgage);
        mContext.setResult(Activity.RESULT_OK, intent);
        mContext.finish();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.m_item_title_view)
        TextView mItemTitleView;
        @BindView(R.id.m_item_title_layout_view)
        LinearLayout mItemTitleLayoutView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
