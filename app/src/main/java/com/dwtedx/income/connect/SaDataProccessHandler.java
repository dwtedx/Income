package com.dwtedx.income.connect;

import android.app.Activity;
import android.content.Context;


import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.inf.IHttpRequestExecutor;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 上午9:16.
 * @Company 路之遥网络科技有限公司
 * @Description AsyncTask 任务  处理机制
 */
public abstract class SaDataProccessHandler<Params, Progress, Result> {
    private WeakReference<Activity> ctx;
    private ProgressDialog dialog = null;
    private IHttpRequestExecutor requestExecutor;

    public SaDataProccessHandler(BaseActivity ctx) {
        if (null != ctx) {
            this.ctx = new WeakReference<Activity>(ctx);
            dialog = ctx.getProgressDialog();
        }
    }


    public void setUrl(String url) {
        requestExecutor = new BaseHttpRequestExecutor(url);

    }

    public IHttpRequestExecutor getRequestExecutor() {
        return requestExecutor;
    }

    /**
     * 请求中
     */
    public void onPreExecute() {
        if (dialog != null) {
            dialog.show();
        }
    }

    /**
     * 请求成功
     */
    public abstract void onSuccess(Result data);

    /**
     * 成功之后
     */
    public void onSuccessed() {
        release();
    }

    /**
     * 请求更新
     */
    @SafeVarargs
    public final void onProgressUpdate(Progress... progress) {

    }

    /**
     * 错误消息
     */
    public void handlerError(SaException e) {

        onError(e);
    }

    /**
     * 异常处理
     * @param e
     */
    public final void onError(SaException e) {
        release();
        if (ctx != null && !ctx.get().isFinishing()) {
            if (ctx.get() instanceof BaseActivity) {
                ((BaseActivity) ctx.get()).handlerError(e);
            }
        }
    }

    /**
     * 释放请求
     */
    public void release() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    /**
     * 请求取消
     */
    public void onCanceled() {
        if (requestExecutor instanceof HttpURLConnection) {
            ((HttpURLConnection) requestExecutor).disconnect();
        }
        release();
    }

    public Context getContext() {
        return ctx.get();
    }
}
