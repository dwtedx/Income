package com.dwtedx.income.utility;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.coder.vincent.smart_toast.SmartToast;
import com.dwtedx.income.R;


/**
 * ClassName:ToastUtil
 * Function:提示框工具类. <br/>
 * Description: 提示框工具类，用于弹出一个简单的文本提示；该工具类主要用于防止重复提示问题. <br/>
 * Date:2014年10月9日
 *
 * @author BaoHang
 * @version V1.0
 * @since[ThreeTi/Android Project]
 */
public class ToastUtil {

    /**
     * 图标类型
     */
    public enum ICON {WARNING, SUCCESS}

    /**
     * toastShortShow:显示一个短暂的提示框. <br/>
     *
     * @param text    要显示的文字，不能为空
     * @author BaoHang
     */
    public static void toastShow(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            toastShow(text,false, 0);
        }
    }

    /**
     * toastShortShow:显示一个短暂的提示框. <br/>
     *
     * @param text    要显示的文字，不能为空
     * @param icon    左边的小图标
     * @author BaoHang
     */
    public static void toastShow(CharSequence text, ICON icon) {
        if (!TextUtils.isEmpty(text)) {
            int resId = 0;
            if (icon.equals(ICON.WARNING)) {
                resId = R.drawable.ic_smart_toast_emotion_warning;
            } else if (icon.equals(ICON.SUCCESS)) {
                resId = R.drawable.ic_smart_toast_emotion_success;
            }
            toastShow(text, true, resId);
        }
    }

    /**
     * toastShortShow:显示一个短暂的提示框. <br/>
     *
     * @param stringId 要显示的文字，不能为空
     * @param icon     左边的小图标
     * @author BaoHang
     */
    public static void toastShow(@StringRes int stringId, ICON icon) {
        int resId = 0;
        if (icon.equals(ICON.WARNING)) {
            resId = R.drawable.ic_smart_toast_emotion_warning;
        } else if (icon.equals(ICON.SUCCESS)) {
            resId = R.drawable.ic_smart_toast_emotion_success;
        }
        toastShow(stringId,  true, resId);

    }

    /**
     * toastShow:显示一个提示框. <br/>
     *
     * @param textResId 要显示的文字的资源id
     * @author BaoHang
     */
    public static void toastShow(@StringRes int textResId) {
        toastShow(textResId, false, 0);
    }

    /**
     * toastShow:显示一个提示框. <br/>
     *
     * @param msg      提示框显示消息文字内容
     *                 LENGTH_LONG
     * @author BaoHang
     */
    private static void toastShow(CharSequence msg, boolean showIcon, int resId) {
        //是否主线程
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            return;
        }
        SmartToast.classic()
                .config()
                .iconResource(showIcon ? resId : 0)
                .iconSizeDp(12f)
                .iconPosition(0) //0左边，1右边
                //.backgroundResource(R.drawable.shape_toast_common)
                .apply()
                .showAtLocation(msg, Gravity.BOTTOM, 0, 96);
    }

    /**
     * toastShow:显示一个提示框. <br/>
     *
     * @param msg      提示框显示消息文字内容
     *                 LENGTH_LONG
     * @author BaoHang
     */
    private static void toastShow(int msg, boolean showIcon, int resId) {
        //是否主线程
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            return;
        }
        SmartToast.classic()
                .config()
                .iconResource(showIcon ? resId : 0)
                .iconSizeDp(12f)
                .iconPosition(0) //0左边，1右边
                //.backgroundResource(R.drawable.shape_toast_common)
                .apply()
                .showAtLocation(msg, Gravity.BOTTOM, 0, 96);
    }

}
