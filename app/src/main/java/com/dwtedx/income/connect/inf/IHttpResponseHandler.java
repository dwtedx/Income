package com.dwtedx.income.connect.inf;

import com.dwtedx.income.connect.SaException;

import java.net.HttpURLConnection;

/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 上午9:01.
 * @Company 路之遥网络科技有限公司
 * @Description Response接口
 */
public interface IHttpResponseHandler<Result> {
	/**
	 * HttpResponse 解析
	 * @param  mHttpURLConnection 响应数据
	 */
	public Result handleResponse(HttpURLConnection mHttpURLConnection) throws SaException;
}