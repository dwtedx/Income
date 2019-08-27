package com.dwtedx.income.account.adapter;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dwtedx.income.R;
import com.dwtedx.income.account.ChooseAccountImageActivity;
import com.dwtedx.income.utility.CommonUtility;

import java.util.List;

/**
 * 继承RecyclerView的Adapter，它会强制开发者使用ViewHolder模式,所以继承适配器的时候传入的泛型是一个继承自ViewHolder的实现类
 * 不过RecyclerView中没有提供给我们Click和onLongClick监听器，需要我们自己实现
 */
public class ChooseAccountImageRecyclerViewAdatper extends RecyclerView.Adapter<ChooseAccountImageRecyclerViewAdatper.ChoosePayRecyclerViewHolder> {

    public static final String[] ACCOUNT_IMAGE_ICON = {"account_diy.png", "account_diy_baitiao.png", "account_diy_fanka.png",
            "account_diy_gongjiao.png", "account_diy_huabei.png", "account_diy_lingqian.png", "account_diy_qq.png", "account_diy_yhk1.png",
            "account_diy_yhk2.png", "account_diy_yhk3.png", "account_diy_yhk4.png", "account_diy_yhk5.png", "account_diy_yuebao.png"};

    private ChooseAccountImageActivity context;

    public ChooseAccountImageRecyclerViewAdatper(ChooseAccountImageActivity context) {
        this.context = context;
    }

    @Override
    public ChoosePayRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {//创建一个ViewHolder
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_choose_account_image_recycler_item, viewGroup, false);//填充这个item布局
        ChoosePayRecyclerViewHolder viewHolder = new ChoosePayRecyclerViewHolder(itemView);//创建ViewHolder
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ChoosePayRecyclerViewHolder myViewHolder, int pos) {//绑定ViewHolder

        myViewHolder.mImageView.setImageResource(CommonUtility.getImageIdByName(context, ACCOUNT_IMAGE_ICON[pos]));//为ViewHolder里的控件设置值
        myViewHolder.mTypeLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(ACCOUNT_IMAGE_ICON[myViewHolder.getAdapterPosition()]);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ACCOUNT_IMAGE_ICON.length;
    }

    private void setResult(String imgage){
        Intent intent = new Intent();
        intent.putExtra("diaccountimage", imgage);
        context.setResult(Activity.RESULT_OK, intent);
        context.finish();
    }

    public class ChoosePayRecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        LinearLayout mTypeLayoutView;

        public ChoosePayRecyclerViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
            mTypeLayoutView = (LinearLayout) itemView.findViewById(R.id.type_layout_view);
        }
    }
}

