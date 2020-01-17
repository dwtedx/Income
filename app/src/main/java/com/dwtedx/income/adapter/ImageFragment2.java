package com.dwtedx.income.adapter;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseFragment;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/16.
 * Company 路之遥网络科技有限公司
 * Description TODO(这里用一句话描述这个类的作用)
 */
public class ImageFragment2 extends BaseFragment {

    ImageView mImageView;
    View mView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_image, null, false);
        mImageView = mView.findViewById(R.id.guide_image_view);
        mImageView.setImageResource(R.mipmap.guide2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mView;
    }

    public void setImageResource(int resourceId){
        mImageView.setImageResource(resourceId);
    }

    public ImageView getmImageView() {
        return mImageView;
    }
}
