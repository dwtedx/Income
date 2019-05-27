package com.dwtedx.income.discovery;

import android.app.Activity;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.dwtedx.income.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 2017/10/21.
 * Company 路之遥网络科技有限公司
 * Description TODO(这里用一句话描述这个类的作用)
 */

public class  TaobaoTradeUtility {

    public static void showTaobaonNumId(Activity activity, String numid){

        AlibcBasePage alibcBasePage = new AlibcDetailPage(numid);

        AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams(); // 若非淘客taokeParams设置为null即可
        alibcTaokeParams.adzoneid = "66306847";
        alibcTaokeParams.pid = "mm_115403208_16988206_66306847";
        alibcTaokeParams.subPid = "mm_115403208_16988206_66306847";
        alibcTaokeParams.extraParams = new HashMap<>();
        alibcTaokeParams.extraParams.put("taokeAppkey","24634592");


        AlibcShowParams alibcShowParams = new AlibcShowParams(OpenType.Auto, false);//页面打开方式，默认，H5，Native
        Map<String, String> exParams = new HashMap<>();
        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改;//yhhpass参数

        Toast.makeText(activity, activity.getString(R.string.discovery_taobao_hint), Toast.LENGTH_SHORT).show();
        AlibcTrade.show(activity, alibcBasePage, alibcShowParams, alibcTaokeParams, exParams , new TaobaoTradeCallback());
    }

    public static void showTaobaonActivityUrl(Activity activity, String path){

        //Toast.makeText(activity, activity.getString(R.string.discovery_taobao_hint), Toast.LENGTH_SHORT).show();

        AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams(); // 若非淘客taokeParams设置为null即可
        alibcTaokeParams.adzoneid = "66306847";
        alibcTaokeParams.pid = "mm_115403208_16988206_66306847";
        alibcTaokeParams.subPid = "mm_115403208_16988206_66306847";
        alibcTaokeParams.extraParams = new HashMap<>();
        alibcTaokeParams.extraParams.put("taokeAppkey","24634592");


        AlibcShowParams alibcShowParams = new AlibcShowParams(OpenType.Auto, false);//页面打开方式，默认，H5，Native
        Map<String, String> exParams = new HashMap<>();
        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改;//yhhpass参数

        Toast.makeText(activity, activity.getString(R.string.discovery_taobao_hint), Toast.LENGTH_SHORT).show();
        //实例化URL打开page
        AlibcBasePage alibcBasePage = new AlibcPage(path);
        AlibcTrade.show(activity, alibcBasePage, alibcShowParams, alibcTaokeParams, exParams , new TaobaoTradeCallback());
    }

}
