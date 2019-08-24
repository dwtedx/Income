package com.dwtedx.income.topic;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
                .asBitmap()
                .error(R.mipmap.imageloader_default_smll)
              //  .placeholder(android.R.color.darker_gray)
                .fitCenter()
                //.centerCrop()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        simpleTarget.onLoadFailed(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        simpleTarget.onResourceReady();
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public void displayGifImage(@NonNull Fragment context, @NonNull String path, ImageView imageView, @NonNull final MySimpleTarget simpleTarget) {
        Glide.with(context).load(path)
               .asGif()
                //可以解决gif比较几种时 ，加载过慢  //DiskCacheStrategy.NONE
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.mipmap.imageloader_default_smll)
                .dontAnimate() //去掉显示动画
                .listener(new RequestListener<String, GifDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                        simpleTarget.onResourceReady();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        simpleTarget.onLoadFailed(null);
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

    /**
     *
     * @param userPath
     * @param imageView
     */
    public static void loadImageUser(Context mContext, String userPath, ImageView imageView){
        if (userPath.toLowerCase().contains(".gif")) {
            Glide.with(mContext).load(userPath)
                    .asGif()
                    //可以解决gif比较几种时 ，加载过慢  //DiskCacheStrategy.NONE
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.mipmap.userhead)
                    .dontAnimate() //去掉显示动画
                    .into(imageView);
        } else {
            //加载图
            Glide.with(mContext).load(userPath)
                    .asBitmap()
                    .error(R.mipmap.userhead)
                    //.placeholder(android.R.color.darker_gray)
                    .fitCenter()
                    //.centerCrop()
                    .into(imageView);
        }
    }

    public static void loadImage(Context mContext, DiTopicimg beanViewInfo, ImageView imageView){
        if (beanViewInfo.getUrl().toLowerCase().contains(".gif")) {
            Glide.with(mContext).load(beanViewInfo.getUrl())
                    .asGif()
                    //可以解决gif比较几种时 ，加载过慢  //DiskCacheStrategy.NONE
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.mipmap.imageloader_default_smll)
                    .dontAnimate() //去掉显示动画
                    .into(imageView);
        } else {
            //加载图
            Glide.with(mContext).load(beanViewInfo.getUrl())
                    .asBitmap()
                    .error(R.mipmap.imageloader_default_smll)
                    //.placeholder(android.R.color.darker_gray)
                    .fitCenter()
                    //.centerCrop()
                    .into(imageView);
        }
    }
}
