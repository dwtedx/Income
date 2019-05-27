/**   
 * @Title: CommonBroadcast.java 
 * @Package com.meineke.auto11.base.widget
 * @author heli
 * @date 2014年9月12日 下午3:15:45 
 * @version V1.0   
 */

package com.dwtedx.income.broadcast;

import android.content.Context;
import android.content.Intent;

/**
 * @ClassName: CommonBroadcast
 * @Description: 自定义程序通用title，并实现左右按钮点击事件
 * @author heli
 * @date 2014年9月12日 下午3:15:45
 */
public class CommonBroadcast {
    
    public final static String BROADCAST_ACTION = "Broadcast_Income";
    public final static String BROADCAST_ACTION_TYPE = "Broadcast_Income_Type";
    
    public final static int BROADCAST_ACTION_TYPE_ORDER = 0; // 提交订单相关

    public static void sendBroadcast(Context context, Intent intent) {
        context.sendBroadcast(intent);
    }
}
