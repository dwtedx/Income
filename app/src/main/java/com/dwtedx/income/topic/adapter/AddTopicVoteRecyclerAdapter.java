package com.dwtedx.income.topic.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.dwtedx.income.R;
import com.dwtedx.income.entity.DiTopicvote;
import com.dwtedx.income.sqliteservice.DIScanService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by A150189 on 2016/11/24.
 */
public class AddTopicVoteRecyclerAdapter extends RecyclerView.Adapter<AddTopicVoteRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<DiTopicvote> mList;

    public AddTopicVoteRecyclerAdapter(Context mContext, List<DiTopicvote> infoList) {
        this.mContext = mContext;
        this.mList = infoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_add_topic_vote_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DiTopicvote data = mList.get(position);
        if(CommonConstants.INCOME_SCAN_ADDBUTTON == data.getAddbutton()){
            holder.mItemDetailView.setVisibility(View.GONE);
            holder.mItemAddView.setVisibility(View.VISIBLE);
            holder.mItemAddIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.add(holder.getAdapterPosition(), new DiTopicvote());
                    notifyItemInserted(holder.getAdapterPosition());
                }
            });
        }else {
            holder.mItemDetailView.setVisibility(View.VISIBLE);
            holder.mItemAddView.setVisibility(View.GONE);
            holder.mItemNameEditText.setText(data.getName());
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
            //删除
            holder.mItemDeleteIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DIScanService.getInstance(mContext).delete(data.getId());
                    //从界面移除
                    mList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public List<DiTopicvote> getTotalVotes(){
        List<DiTopicvote> topicvotes = new ArrayList<>();
        for (DiTopicvote topicvote : mList) {
            if(CommonConstants.INCOME_SCAN_ADDBUTTON != topicvote.getAddbutton()
                && !CommonUtility.isEmpty(topicvote.getName())){
                topicvotes.add(topicvote);
            }
        }
        return topicvotes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.m_item_name_edit_text)
        EditText mItemNameEditText;
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


}
