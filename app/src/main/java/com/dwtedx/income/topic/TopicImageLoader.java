package com.dwtedx.income.topic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dwtedx.income.R;
import com.dwtedx.income.entity.DiTopicimg;
import com.previewlibrary.loader.IZoomMediaLoader;
import com.previewlibrary.loader.MySimpleTarget;

/**
 * Created by yangc on 2017/9/4.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public class TopicImageLoader implements IZoomMediaLoader {

    @Override
    public void displayImage(@NonNull Fragment context, @NonNull String path, ImageView imageView, @NonNull final MySimpleTarget simpleTarget) {
        Glide.with(context).load(path)
                .error(R.mipmap.imageloader_default_smll)
                //.placeholder(R.mipmap.imageloader_default_smll)
                //.placeholder(R.color.common_body_tip_colors)
                .fitCenter()
                //.centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        simpleTarget.onLoadFailed(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        simpleTarget.onResourceReady();
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public void displayGifImage(@NonNull Fragment context, @NonNull String path, ImageView imageView, @NonNull final MySimpleTarget simpleTarget) {
        Glide.with(context)
                .asGif()
                .load(path)
                //可以解决gif比较几种时 ，加载过慢  //DiskCacheStrategy.NONE
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.mipmap.imageloader_default_smll)
                //.placeholder(R.mipmap.imageloader_default_smll)
                //.placeholder(R.color.common_body_tip_colors)
                //.dontAnimate() //去掉显示动画
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        simpleTarget.onLoadFailed(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        simpleTarget.onResourceReady();
                        return false;
                    }
                })
                .into(imageView);
    }
    @Override
    public void onStop(@NonNull Fragment context) {
          Glide.with(context).onStop();
    }

    @Override
    public void clearMemory(@NonNull Context c) {
             Glide.get(c).clearMemory();
    }


    public static void loadImage(Context mContext, DiTopicimg beanViewInfo, ImageView imageView){
        if (beanViewInfo.getUrl().toLowerCase().contains(".gif")) {
            Glide.with(mContext).asGif()
                    .load(beanViewInfo.getUrl())
                    //可以解决gif比较几种时 ，加载过慢  //DiskCacheStrategy.NONE
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .error(R.mipmap.imageloader_default_smll)
                    //.placeholder(R.mipmap.imageloader_default_smll)
                    //.placeholder(R.color.common_body_tip_colors)
                    .into(imageView);
        } else {
            //加载图
            Glide.with(mContext).load(beanViewInfo.getUrl())
                    .error(R.mipmap.imageloader_default_smll)
                    //.placeholder(R.mipmap.imageloader_default_smll)
                    //.placeholder(R.color.common_body_tip_colors)
                    .fitCenter()
                    //.centerCrop()
                    .into(imageView);
        }
    }

    /**
     *
     * @param userPath
     * @param imageView
     */
    public static void loadImageUser(Context mContext, String userPath, ImageView imageView){
        if (userPath.toLowerCase().contains(".gif")) {
            Glide.with(mContext).asGif()
                    .load(userPath)
                    //可以解决gif比较几种时 ，加载过慢  //DiskCacheStrategy.NONE
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .error(R.mipmap.userhead)
                    .placeholder(R.mipmap.userhead)
                    .into(imageView);
        } else {
            //加载图
            Glide.with(mContext).load(userPath)
                    .error(R.mipmap.userhead)
                    .placeholder(R.mipmap.userhead)
                    .fitCenter()
                    //.centerCrop()
                    .into(imageView);
        }
    }

    public static void loadImageAdd(Context mContext, DiTopicimg beanViewInfo, ImageView imageView){
        //加载图
        Glide.with(mContext).load(R.mipmap.image_add)
                .error(R.mipmap.image_add)
                //.placeholder(R.mipmap.imageloader_default_smll)
                //.placeholder(R.color.common_body_tip_colors)
                .fitCenter()
                //.centerCrop()
                .into(imageView);
    }
}
