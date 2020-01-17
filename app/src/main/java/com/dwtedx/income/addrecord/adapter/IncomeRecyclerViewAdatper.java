package com.dwtedx.income.addrecord.adapter;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.utility.CommonUtility;

import java.util.List;

/**
 * 继承RecyclerView的Adapter，它会强制开发者使用ViewHolder模式,所以继承适配器的时候传入的泛型是一个继承自ViewHolder的实现类
 * 不过RecyclerView中没有提供给我们Click和onLongClick监听器，需要我们自己实现
 */
public class IncomeRecyclerViewAdatper extends RecyclerView.Adapter<IncomeRecyclerViewAdatper.IncomeRecyclerViewHolder> {

    private Context context;
    private List<DiType> mDiTypeList;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int postion);

        void onItemLongClick(View view, int postion);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {//对外提供的一个监听方法
        this.mOnItemClickListener = listener;
    }

    public IncomeRecyclerViewAdatper(Context context, List<DiType> diTypeList) {
        this.context = context;
        this.mDiTypeList = diTypeList;
    }

    @Override
    public IncomeRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {//创建一个ViewHolder
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_income_recycler_item, viewGroup, false);//填充这个item布局
        IncomeRecyclerViewHolder viewHolder = new IncomeRecyclerViewHolder(itemView);//创建ViewHolder
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final IncomeRecyclerViewHolder myViewHolder, int pos) {//绑定ViewHolder

        DiType diType = mDiTypeList.get(pos);

        myViewHolder.mTextView.setText(diType.getName());//为ViewHolder里的控件设置值
        myViewHolder.mImageView.setImageResource(CommonUtility.getImageIdByName(context, diType.getIcon()));//为ViewHolder里的控件设置值
        if (mOnItemClickListener != null) {//如果设置了监听那么它就不为空，然后回调相应的方法
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postion = myViewHolder.getLayoutPosition();//得到当前点击item的位置postion
                    mOnItemClickListener.onItemClick(myViewHolder.itemView, postion);
                }
            });
            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int postion = myViewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(myViewHolder.itemView, postion);
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mDiTypeList.size();
    }

    public class IncomeRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;

        public IncomeRecyclerViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.textView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

}

