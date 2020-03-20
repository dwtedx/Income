package com.dwtedx.income.vip.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.entity.DiTopicimg;
import com.dwtedx.income.entity.DiUserinviteinfo;
import com.dwtedx.income.topic.TopicImageLoader;
import com.previewlibrary.GPreviewBuilder;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class VipInviteRecyclerAdapter extends RecyclerView.Adapter<VipInviteRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<DiUserinviteinfo> mList;

    public VipInviteRecyclerAdapter(Context mContext, List<DiUserinviteinfo> list) {
        this.mContext = mContext;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_vip_invite_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DiUserinviteinfo data = mList.get(position);

    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.m_phone_text_view)
        TextView mPhoneTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
