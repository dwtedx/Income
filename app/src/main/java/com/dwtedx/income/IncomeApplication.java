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
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.util.Log;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.provider.HomePrivacySharedPreferences;
import com.dwtedx.income.updateapp.UpdateService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.ToastUtil;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.tauth.Tencent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.listener.OnGetOaidListener;
import com.umeng.commonsdk.utils.UMUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.api.UPushRegisterCallback;
import com.umeng.socialize.PlatformConfig;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.oppo.OppoRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

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
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }
        super.onCreate();

        initAppInfo();
        initUMengPre();//预初始化函数UMConfigure
        initUMeng();//预初始化函数UM
        //initAlibcTrade();//阿里百川
        initAccessTokenWithAkSk();//百度OCR
        initX5WebView();
    }

    private void initAppInfo() {
        try {
            ApplicationData.mClientSID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
            ApplicationData.mAppVersion = UpdateService.getAPKVersion(getApplicationContext());
            ApplicationData.mAppVersionCode = UpdateService.getAPKVersionCode(getApplicationContext());
            ApplicationData.mIncomeApplication = this;
            //渠道
            ApplicationInfo info = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            ApplicationData.mChannel = info.metaData.getString("UMENG_CHANNEL");
            HomePrivacySharedPreferences.init(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        Log.i(TAG, "++ onTerminate");
        super.onTerminate();
    }

    public void initAlibcTrade() {
        if(HomePrivacySharedPreferences.getIsTip()){
            return;
        }
        //初始化百川SDK
        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                //初始化成功，设置相关的全局配置参数
                //ToastUtil.toastShortShow("初始化成功", ToastUtil.ICON.SUCCESS);
                //AlibcTradeSDK.setTaokeParams(AlibcTaokeParams taokeParams);
            }

            @Override
            public void onFailure(int code, String msg) {
                //初始化失败，可以根据code和msg判断失败原因，详情参见错误说明
                ToastUtil.toastShow("初始化失败,错误码=" + code + " / 错误消息=" + msg, ToastUtil.ICON.WARNING);
            }
        });
    }

    /**
     * 预初始化函数UMConfigure.preInit()
     */
    private void initUMengPre(){
        UMConfigure.preInit(getApplicationContext(), CommonConstants.UMENG_APP_KEY, ApplicationData.mChannel);
    }

    /**
     * 在用户阅读您的《隐私政策》并取得用户授权之后，才调用正式初始化函数UMConfigure.init()初始化统计SDK，此时SDK才会真正采集设备信息并上报数据。
     * 反之，如果用户不同意《隐私政策》授权，则不能调用UMConfigure.init()初始化函数。请注意调用正式初始化函数UMConfigure.init()之前，不要调用UMShareAPI接口类的任何API方法。
     */
    public void initUMeng() {
        if(HomePrivacySharedPreferences.getIsTip()){
            return;
        }
        //日志开关
        if (BuildConfig.DEBUG) {
            UMConfigure.setLogEnabled(true);
        }
        UMConfigure.getOaid(getApplicationContext(), new OnGetOaidListener() {
            @Override
            public void onGetOaid(String oaid) {
                android.util.Log.i("mob", "oaid" + oaid);
            }
        });

        //推送初始化
        boolean isMainProcess = UMUtils.isMainProgress(this);
        if (!isMainProcess) {
            return;
        }
        //App启动速度优化：可以在子线程中调用SDK初始化接口
        new Thread(new Runnable() {
            @Override
            public void run() {
                UMConfigure.init(getApplicationContext(), CommonConstants.UMENG_APP_KEY, ApplicationData.mChannel, UMConfigure.DEVICE_TYPE_PHONE, CommonConstants.UMENG_MESSAGE_SECRET);
                //注册推送服务，每次调用register方法都会回调该接口
                PushAgent.getInstance(getApplicationContext()).register(new UPushRegisterCallback() {

                    @Override
                    public void onSuccess(String deviceToken) {
                        //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                        Log.i(TAG, "推送注册成功：deviceToken：-------->  " + deviceToken);
                    }

                    @Override
                    public void onFailure(String errCode, String errDesc) {
                        Log.e(TAG, "注册失败 " + "code:" + errCode + ", desc:" + errDesc);
                    }
                });
            }
        }).start();
        //小米Push初始化
        MiPushRegistar.register(getApplicationContext(), "2882303761517188784", "5831718889784");
        //华为Push初始化
        HuaWeiRegister.register(this);
        //OPPO通道，参数1为app key，参数2为app secret
        OppoRegister.register(this, "3V92Jju57xycs8Cskg40c0Wo8", "69f0677dab2fe19ecAA590C138c770C0");


        //QQ官方sdk授权
        Tencent.setIsPermissionGranted(true);
        //初始化第三方帐号
        PlatformConfig.setWeixin("wx1b69112b9d811eb4", "f573c434c2e046dbe4263ee354af7f5a");//微信 appid appsecret
        PlatformConfig.setWXFileProvider("com.dwtedx.income.fileprovider");
        PlatformConfig.setSinaWeibo("3559270156", "5c321a16f3c541047694a4c7704576fc", "http://sns.whalecloud.com");//新浪微博 appkey appsecret
        PlatformConfig.setSinaFileProvider("com.dwtedx.income.fileprovider");
        PlatformConfig.setQQZone("1105404596", "Qy2l9sXiZPsCaRVV");// QQ和Qzone appid appkey
        PlatformConfig.setQQFileProvider("com.dwtedx.income.fileprovider");
        PlatformConfig.setAlipay("2016052401436053");//支付宝 appid
    }

    /**
     * 用明文ak，sk初始化
     */
    public void initAccessTokenWithAkSk() {
        if(HomePrivacySharedPreferences.getIsTip()){
            return;
        }
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                mHasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                ToastUtil.toastShow("AK，SK方式获取token失败 / 错误消息="+error.getMessage(), ToastUtil.ICON.WARNING);
            }
        }, getApplicationContext(), ORC_API_KEY, ORC_SECRET_KEY);
    }

    public void initX5WebView() {
        if(HomePrivacySharedPreferences.getIsTip()){
            return;
        }
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d(TAG, " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

}
