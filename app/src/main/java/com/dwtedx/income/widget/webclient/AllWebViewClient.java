/**   
* @Title: AllWebViewClient.java 
* @Package com.jsl.songsong.allwebview 
* @Description: TODO(用一句话描述该文件做什么) 
* @author qinyl http://dwtedx.com 
* @date 2015年8月11日 下午1:43:07 
* @version V1.0   
*/
package com.dwtedx.income.widget.webclient;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/** 
 * @ClassName: AllWebViewClient 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author qinyl http://dwtedx.com
 * @date 2015年8月11日 下午1:43:07 
 *  
 */
public class AllWebViewClient extends WebViewClient {

	//private boolean isPageError;

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// 在当前的webview中跳转到新的url
		view.loadUrl(url);
		return true;
	}

	@Override
	public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
		super.onReceivedError(view, request, error);
		//view.loadUrl("file:///android_asset/errorpage/error.html");
		//isPageError = true;
	}

	//	@Override
//	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//		super.onReceivedError(view, errorCode, description, failingUrl);
//		view.loadUrl("file:///android_asset/errorpage/error.html");
//		isPageError = true;
//	}


	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
//		String js = "var newscript = document.createElement(\"script\");";
//		js += "newscript.type=\"text/javascript\";";
//		//js += "newscript.innerHTML=document.getElementById(\"header\").style.display=\"none\";";
//		js += "newscript.innerHTML=$('#header').remove();$('#content').css('top','0');";
//		js += "document.body.appendChild(newscript);";
//		view.loadUrl("javascript:" + js);
	}
}
