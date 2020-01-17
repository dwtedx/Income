package com.dwtedx.income.accounttype.adapter;


import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.accounttype.ChoosePayingTypeActivity;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.ParseJsonToObject;

import java.util.List;

/**
 * 继承RecyclerView的Adapter，它会强制开发者使用ViewHolder模式,所以继承适配器的时候传入的泛型是一个继承自ViewHolder的实现类
 * 不过RecyclerView中没有提供给我们Click和onLongClick监听器，需要我们自己实现
 */
public class ChoosePayRecyclerViewAdatper extends RecyclerView.Adapter<ChoosePayRecyclerViewAdatper.ChoosePayRecyclerViewHolder> {

    private ChoosePayingTypeActivity context;
    private List<DiType> mDiTypeList;

    public ChoosePayRecyclerViewAdatper(ChoosePayingTypeActivity context, List<DiType> diTypeList) {
        this.context = context;
        this.mDiTypeList = diTypeList;
    }

    @Override
    public ChoosePayRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {//创建一个ViewHolder
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_choose_pay_recycler_item, viewGroup, false);//填充这个item布局
        ChoosePayRecyclerViewHolder viewHolder = new ChoosePayRecyclerViewHolder(itemView);//创建ViewHolder
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ChoosePayRecyclerViewHolder myViewHolder, int pos) {//绑定ViewHolder

        final DiType diType = mDiTypeList.get(pos);

        myViewHolder.mTextView.setText(diType.getName());//为ViewHolder里的控件设置值
        myViewHolder.mImageView.setImageResource(CommonUtility.getImageIdByName(context, diType.getIcon()));//为ViewHolder里的控件设置值
        myViewHolder.mTypeLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(diType);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDiTypeList.size();
    }

    private void setResult(DiType diType){
        Intent intent = new Intent();
        intent.putExtra("ditype", ParseJsonToObject.getJsonFromObj(diType).toString());
        context.setResult(Activity.RESULT_OK, intent);
        context.finish();
    }

    public class ChoosePayRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        LinearLayout mTypeLayoutView;

        public ChoosePayRecyclerViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.textView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
            mTypeLayoutView = (LinearLayout) itemView.findViewById(R.id.type_layout_view);
        }
    }
}

