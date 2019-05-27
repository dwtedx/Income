/**   
* @Title: DispatchApplication.java 
* @Package com.jsl.nfc.dispatch 
* @Description: TODO(用一句话描述该文件做什么) 
* @author qinyl http://dwtedx.com 
* @date 2015年6月16日 下午8:03:53 
* @version V1.0   
*/
package com.dwtedx.income;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.updateapp.UpdateService;
import com.dwtedx.income.utility.CommonConstants;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/** 
 * @ClassName: DispatchApplication 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author qinyl http://dwtedx.com
 * @date 2015年6月16日 下午8:03:53 
 *  
 */
public class IncomeApplication extends Application {
	public final static String TAG = "IncomeApplication";
	private boolean DEVELOPER_MODE = false;

	//推送
	public static final String APP_ID = "2882303761517188784";
	public static final String APP_KEY = "5831718889784";
	//百度 ocr
	public static final String ORC_API_KEY = "hPPMaV1yM5uE4HmtWBgLaxw8";
	public static final String ORC_SECRET_KEY = "C34yAHb3vOcxDV0717W8DdX2xycm28CF";
	public static boolean mHasGotToken = false;

	@Override
	public void onLowMemory() {
		Log.i(TAG, "++ onLowMemory");
		super.onLowMemory();
	}

	@SuppressLint("NewApi")
	@Override
	public void onTrimMemory(int level) {
		Log.i(TAG, "++ onTrimMemory, Level = " + level);
		super.onTrimMemory(level);
	}

	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate() {
		Log.i(TAG, "++ onCreate");
		if (DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());

		}
		super.onCreate();

		ApplicationData.mClientSID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		ApplicationData.mAppVersion = UpdateService.getAPKVersion(getApplicationContext());
		ApplicationData.mAppVersionCode = UpdateService.getAPKVersionCode(getApplicationContext());
		ApplicationData.mIncomeApplication = this;

		initUMeng();

		initPush();

		initAlibcTrade();

		//百度OCR
		initAccessTokenWithAkSk();

		closeAndroidPDialog();
	}

	@Override
	public void onTerminate() {
		Log.i(TAG, "++ onTerminate");
		super.onTerminate();
	}

	private void initAlibcTrade() {
		//初始化百川SDK
		AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
			@Override
			public void onSuccess() {
				//初始化成功，设置相关的全局配置参数
				//Toast.makeText(getApplicationContext(), "初始化成功", Toast.LENGTH_SHORT).show();
				//AlibcTradeSDK.setTaokeParams(AlibcTaokeParams taokeParams);
			}

			@Override
			public void onFailure(int code, String msg) {
				//初始化失败，可以根据code和msg判断失败原因，详情参见错误说明
				Toast.makeText(getApplicationContext(), "初始化失败,错误码="+code+" / 错误消息="+msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void initPush() {
		//初始化push推送服务
		if(shouldInit()) {
			MiPushClient.registerPush(this, APP_ID, APP_KEY);
		}
		//打开Log
		LoggerInterface newLogger = new LoggerInterface() {
			@Override
			public void setTag(String tag) {
				// ignore
			}

			@Override
			public void log(String content, Throwable t) {
				Log.d(TAG, content, t);
			}

			@Override
			public void log(String content) {
				Log.d(TAG, content);
			}
		};
		Logger.setLogger(this, newLogger);
	}

	private boolean shouldInit() {
		ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		String mainProcessName = getPackageName();
		int myPid = Process.myPid();
		if(null != processInfos) {
			for (ActivityManager.RunningAppProcessInfo info : processInfos) {
				if (info.pid == myPid && mainProcessName.equals(info.processName)) {
					return true;
				}
			}
		}
		return false;
	}

	private void initUMeng() {
		//8b7ac86ea0aebdf27f289bda478ebc99
		UMConfigure.init(getApplicationContext(), UMConfigure.DEVICE_TYPE_PHONE, null);
		//初始化第三方帐号
		PlatformConfig.setWeixin("wx1b69112b9d811eb4", "f573c434c2e046dbe4263ee354af7f5a");//微信 appid appsecret
		PlatformConfig.setSinaWeibo("3559270156", "5c321a16f3c541047694a4c7704576fc", "http://sns.whalecloud.com");//新浪微博 appkey appsecret
		PlatformConfig.setQQZone("1105404596", "Qy2l9sXiZPsCaRVV");// QQ和Qzone appid appkey
		PlatformConfig.setAlipay("2016052401436053");//支付宝 appid
	}

	/**
	 * 用明文ak，sk初始化
	 */
	private void initAccessTokenWithAkSk() {
		OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
			@Override
			public void onResult(AccessToken result) {
				String token = result.getAccessToken();
				mHasGotToken = true;
			}

			@Override
			public void onError(OCRError error) {
				error.printStackTrace();
				//Toast.makeText(getApplicationContext(), "AK，SK方式获取token失败 / 错误消息="+error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}, getApplicationContext(),  ORC_API_KEY, ORC_SECRET_KEY);
	}

	/**
	 * 解决在Android P上的提醒弹窗
	 */
	private void closeAndroidPDialog(){
		try {
			Class aClass = Class.forName("android.content.pm.PackageParser$Package");
			Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
			declaredConstructor.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Class cls = Class.forName("android.app.ActivityThread");
			Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
			declaredMethod.setAccessible(true);
			Object activityThread = declaredMethod.invoke(null);
			Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
			mHiddenApiWarningShown.setAccessible(true);
			mHiddenApiWarningShown.setBoolean(activityThread, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
