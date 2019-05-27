package com.dwtedx.income.connect;

import android.util.Log;

import com.dwtedx.income.connect.inf.IHttpResponseHandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 上午9:12.
 * @Company 路之遥网络科技有限公司
 * @Description 用于处理HTTP响应
 */
public abstract class BaseHttpResponseHandler<Result> implements IHttpResponseHandler<Result> {
	public final static String TAG = "BaseHttpResponseHandler";
	
	@Override
	public Result handleResponse(HttpURLConnection mHttpURLConnection) throws SaException {
		int responseCode = -1;
		Result result = null;
		InputStream mInputStream = null;
		try {
			responseCode = mHttpURLConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				mInputStream = mHttpURLConnection.getInputStream();
				InputStream inStream = new BufferedInputStream(mInputStream);
				//调用数据流处理方法
				byte[] data = readInputStream(inStream);
				String responseData = new String(data);
				Log.i(TAG, "handleResponse, response data=" + responseData);
				ResponseData responseInfo = new ResponseData(responseData);

				handleServerInfoError(responseInfo);
				result = getResult(responseInfo.getmBody());

			} else if(responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){
				mInputStream = mHttpURLConnection.getErrorStream();
				InputStream inStream = new BufferedInputStream(mInputStream);
				//调用数据流处理方法
				byte[] data = readInputStream(inStream);
				String responseData = new String(data);
				Log.i(TAG, "handleResponse, response data=" + responseData);
				ResponseData responseInfo = null;
				try {
					responseInfo = new ResponseData(responseData);
				} catch (SaException e) {
					e.printStackTrace();
					throw new SaException(responseCode);
				}
				handleServerInfoError(responseInfo);
			}else {
				throw new SaException(responseCode);
			}
		} catch (SaException e){
			throw e;
		} catch(EOFException e){
			e.printStackTrace();
			throw new SaException(SaError.ERROR_TYPE_EOF, e);
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			//Log.i(TAG, e.getMessage().toString());
			throw new SaException(SaError.ERROR_TYPE_TIMEOUT, e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//Log.i(TAG, e.getMessage().toString());
			throw new SaException(SaError.ERROR_TYPE_EOF, e);
		} catch (IOException e) {
			e.printStackTrace();
			//Log.i(TAG, e.getMessage().toString());
			throw new SaException(SaError.ERROR_TYPE_IO, e);
		} catch (Exception e) {
			e.printStackTrace();
			//Log.i(TAG, e.getMessage());
			throw new SaException(SaError.ERROR_TYPE_UNKNOWN, e);
		} finally {
			try {
				if (null != mInputStream) {
					mInputStream.close();
					mInputStream = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				//Log.i(TAG, e.getMessage());
				throw new SaException(SaError.ERROR_TYPE_UNKNOWN, e);
			}
		}

		return result;
	}

	/**
	 * 从输入流中获取数据
	 * @param inStream 输入流
	 * @return
	 * @throws Exception
	 */
	private static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len=inStream.read(buffer)) != -1 ){
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	private void handleServerInfoError(ResponseData responseInfo) throws SaException {
		SaError mSaError = SaError.getServerInfoError(responseInfo);
		if (mSaError != null) {
			throw new SaException(mSaError);
		}
	}

	/**
	 * @param jsonObj
	 * @throws SaException
	 */
	public abstract Result getResult(Object jsonObj) throws SaException;
}
