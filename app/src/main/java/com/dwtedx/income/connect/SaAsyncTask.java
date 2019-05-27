package com.dwtedx.income.connect;

import android.os.AsyncTask;

/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 上午9:15.
 * @Company 路之遥网络科技有限公司
 * @Description AsyncTask 任务
 */
public abstract class SaAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	protected SaDataProccessHandler<Params, Progress, Result> handler;
	private SaException e = null;

	public SaAsyncTask(SaDataProccessHandler<Params, Progress, Result> handler) {
		this.handler = handler;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		handler.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Progress... values) {
		super.onProgressUpdate(values);
		handler.onProgressUpdate(values);
	}
	
	protected void setErrorObj(SaException e) {
		this.e = e;
	}


	@Override
	protected void onPostExecute(Result result) {
		if (e == null) {
			handler.onSuccess(result);
			handler.onSuccessed();
		} else {
			handler.handlerError(e);
		}
	};

	@Override
	protected void onCancelled() {
		super.onCancelled();
		handler.onCanceled();
	}
}
