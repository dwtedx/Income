package com.dwtedx.income.connect.inf;

import android.annotation.SuppressLint;


import com.dwtedx.income.connect.SaException;

import org.json.JSONObject;

import java.io.IOException;

/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 上午9:01.
 * @Company 路之遥网络科技有限公司
 * @Description Request接口
 */
public interface IHttpRequestExecutor {

    /**
     * @Description Request接口
     * @param parameters
     * @param responseHandler
     * @param <Result>
     * @return
     * @throws SaException
     * @throws IOException
     */
    @SuppressLint("NewApi")
    public <Result> Result executeRequest(JSONObject parameters, IHttpResponseHandler<Result> responseHandler) throws SaException, IOException;
}
