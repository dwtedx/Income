/**   
* @Title: AllWebChromeClient.java 
* @Package com.jsl.songsong.allwebview 
* @Description: TODO(用一句话描述该文件做什么) 
* @author qinyl http://dwtedx.com 
* @date 2015年8月11日 下午1:43:32 
* @version V1.0   
*/
package com.dwtedx.income.widget.x5webview;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;


/**
 * @ClassName: AllWebChromeClient 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author qinyl http://dwtedx.com
 * @date 2015年8月11日 下午1:43:32 
 *  
 */
public class X5WebChromeClient extends WebChromeClient {

	View myVideoView;
	View myNormalView;
	IX5WebChromeClient.CustomViewCallback callback;

	private ProgressBar bar;

	/**
	* <p>Title: </p>
	* <p>Description: </p>
	*/
	public X5WebChromeClient(ProgressBar bar) {
		super();
		this.bar = bar;
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		super.onProgressChanged(view, newProgress);
		if(null != bar) {
			if (newProgress == 100) {
				bar.setVisibility(View.INVISIBLE);
			} else {
				if (View.INVISIBLE == bar.getVisibility()) {
					bar.setVisibility(View.VISIBLE);
				}
				bar.setProgress(newProgress);
			}
		}
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message,
                             JsResult result) {
		//return super.onJsAlert(view, url, message, result);
		new MaterialDialog.Builder(view.getContext())
				.title(R.string.tip)
				.content(message)
				.positiveText(R.string.ok)
				.negativeText(R.string.cancel)
				.onAny(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						//NEGATIVE   POSITIVE
						if(which.name().equals("POSITIVE")){

						}
					}
				})
				.show();
		result.confirm();//因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。  
		return true;
	}

	@Override
	public boolean onJsConfirm(WebView view, String url, String message,
                               JsResult result) {
		return super.onJsConfirm(view, url, message, result);
	}


	/**
	 * 全屏播放配置
	 */
	@Override
	public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
		super.onShowCustomView(view, customViewCallback);
	}

	@Override
	public void onHideCustomView() {
		if (callback != null) {
			callback.onCustomViewHidden();
			callback = null;
		}
		if (myVideoView != null) {
			ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
			viewGroup.removeView(myVideoView);
			viewGroup.addView(myNormalView);
		}
	}


	
}
