package com.dwtedx.income.connect;

import android.annotation.SuppressLint;
import android.util.Log;


import com.dwtedx.income.connect.inf.IHttpRequestExecutor;
import com.dwtedx.income.connect.inf.IHttpResponseHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 上午9:09.
 * @Company 路之遥网络科技有限公司
 * @Description HTTP 请求
 */
@SuppressWarnings("deprecation")
public class BaseHttpRequestExecutor implements IHttpRequestExecutor {
	public final static String TAG = "BaseHttpRequestExecutor";

	private HttpURLConnection mHttpURLConnection;

	private URL url;

	public BaseHttpRequestExecutor(String urlStr) {
		try {
			url = new URL(urlStr);
			mHttpURLConnection = (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			//Log.i(TAG, e.getMessage());
			//throw new SaException(SaError.ERROR_TYPE_MALFORMED_URL, e);
		} catch (IOException e) {
			e.printStackTrace();
			//Log.i(TAG, e.getMessage());
			//throw new SaException(SaError.ERROR_TYPE_IO, e);
		}

	}

	@Override
	@SuppressLint("NewApi")
    public <Result> Result executeRequest(JSONObject parameters, IHttpResponseHandler<Result> responseHandler) throws SaException {
		Result result = null;
		//ObjectOutputStream objOutputStrm = null;
		OutputStream outStrm = null;
		try {
			initRequest();
			if (null != parameters) {
				Log.i(TAG, "executeRequest, request url=" + url + "; data=" + parameters.toString());
				// 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，
				// 所以在开发中不调用上述的connect()也可以)。
				outStrm = mHttpURLConnection.getOutputStream();
				outStrm.write(parameters.toString().getBytes("UTF-8"));
				outStrm.flush();
				outStrm.close();

				// 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
				//objOutputStrm = new ObjectOutputStream(outStrm);

				// 向对象输出流写出数据，这些数据将存到内存缓冲区中
				//objOutputStrm.writeObject(parameters.toString());
				//objOutputStrm.writeUTF(parameters.toString());
				//objOutputStrm.writeBytes(parameters.toString());
				//objOutputStrm.write(parameters.toString().getBytes("UTF-8"));

				// 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream）
				//objOutputStrm.flush();

				// 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,
				// 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器
				//objOutputStrm.close();

				// 上边的httpConn.getInputStream()方法已调用,本次HTTP请求已结束,下边向对象输出流的输出已无意义，
				// 既使对象输出流没有调用close()方法，下边的操作也不会向对象输出流写入任何数据.
				// 因此，要重新发送数据时需要重新创建连接、重新设参数、重新创建流对象、重新写数据、
				// 重新发送数据objOutputStm(至于是否不用重新这些操作需要再研究)
				//objOutputStm.writeObject(new String(""));
				//httpConn.getInputStream()
			}

			// 调用HttpURLConnection连接对象的getInputStream()函数,
			// 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
			//InputStream mInputStream = mHttpURLConnection.getInputStream(); // 注意，实际发送请求的代码段就在这里

			result = responseHandler.handleResponse(mHttpURLConnection);
		} catch (SaException e){
			throw e;
		} catch (SocketException e){
			e.printStackTrace();
			//Log.i(TAG, e.getMessage());
			throw new SaException(SaError.ERROR_TYPE_SOCKET, e);
		} catch(SocketTimeoutException e){
			e.printStackTrace();
			throw new SaException(SaError.ERROR_TYPE_TIMEOUT, e);
		}catch (IOException e) {
			e.printStackTrace();
			//Log.i(TAG, e.getMessage());
			throw new SaException(SaError.ERROR_TYPE_IO, e);
		} catch (Exception e) {
			e.printStackTrace();
			//Log.i(TAG, e.getMessage());
			throw new SaException(SaError.ERROR_TYPE_UNKNOWN, e);
		}finally {
			try {
				if(null != outStrm) {
					outStrm.close();
					mHttpURLConnection = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				//Log.i(TAG, e.getMessage());
				throw new SaException(SaError.ERROR_TYPE_IO, e);
			}
		}
		return result;
	}

	private void initRequest() throws SaException {
		if (mHttpURLConnection == null) {
			throw new SaException(SaError.ERROR_TYPE_MALFORMED_URL, new Exception("ddframe url malformed"));
		}
		try {
			// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
			// http正文内，因此需要设为true, 默认情况下是false;
			mHttpURLConnection.setDoOutput(true);
			// 设置是否从httpUrlConnection读入，默认情况下是true;
			mHttpURLConnection.setDoInput(true);
			// Post 请求不能使用缓存
			mHttpURLConnection.setUseCaches(false);
			// 设定传送的内容类型是可序列化的java对象
			// (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
			//mHttpURLConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");
			mHttpURLConnection.setRequestProperty("Content-type", "application/json");
			// 设定请求的方法为"POST"，默认是GET
			mHttpURLConnection.setRequestMethod("POST");
			// 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
			//mHttpURLConnection.connect();
			mHttpURLConnection.setConnectTimeout(30000);
			mHttpURLConnection.setReadTimeout(30000);
		} catch (ProtocolException e) {
			e.printStackTrace();
			//Log.i(TAG, e.getMessage());
			throw new SaException(SaError.ERROR_TYPE_PROTOCOL, e);
		}
	}

}
