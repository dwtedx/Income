package com.dwtedx.income.accounttype.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.helper.MyItemTouchCallback;
import com.dwtedx.income.utility.CommonUtility;

import java.util.Collections;
import java.util.List;

/**
 * 继承RecyclerView的Adapter，它会强制开发者使用ViewHolder模式,所以继承适配器的时候传入的泛型是一个继承自ViewHolder的实现类
 * 不过RecyclerView中没有提供给我们Click和onLongClick监听器，需要我们自己实现
 */
public class PayProfileRecyclerViewAdatper extends RecyclerView.Adapter<PayProfileRecyclerViewAdatper.PayProfileRecyclerViewHolder>  implements MyItemTouchCallback.ItemTouchAdapter {

    private Context context;
    private List<DiType> mDiTypeList;

    public PayProfileRecyclerViewAdatper(Context context, List<DiType> diTypeList) {
        this.context = context;
        this.mDiTypeList = diTypeList;
    }

    @Override
    public PayProfileRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {//创建一个ViewHolder
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_profile_pay_recycler_item, viewGroup, false);//填充这个item布局
        PayProfileRecyclerViewHolder viewHolder = new PayProfileRecyclerViewHolder(itemView);//创建ViewHolder
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PayProfileRecyclerViewHolder myViewHolder, int pos) {//绑定ViewHolder

        DiType diType = mDiTypeList.get(pos);

        myViewHolder.mTextView.setText(diType.getName());//为ViewHolder里的控件设置值
        myViewHolder.mImageView.setImageResource(CommonUtility.getImageIdByName(context, diType.getIcon()));//为ViewHolder里的控件设置值

    }

    @Override
    public int getItemCount() {
        return mDiTypeList.size();
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
//        if (fromPosition==mDiTypeList.size() || toPosition==mDiTypeList.size()){
//            return;
//        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mDiTypeList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mDiTypeList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        mDiTypeList.remove(position);
        notifyItemRemoved(position);
    }

    public class PayProfileRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;

        public PayProfileRecyclerViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.textView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}