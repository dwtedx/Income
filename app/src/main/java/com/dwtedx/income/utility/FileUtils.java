package com.dwtedx.income.utility;

import android.os.Build;
import android.os.Environment;

import com.dwtedx.income.entity.ApplicationData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ClassName FileUtils
 * Description 文件工具类
 * Create by shinyuu by 2022/6/9 16:21
 */
public class FileUtils {

    /**
     * 系统路径
     * @return
     */
    public static String getFileRootPath() {
        String environmentFileRoot = null; //文件根路径
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//sd卡是否可用
            int currentapiVersion = Build.VERSION.SDK_INT;//手机系统版本号
            if (currentapiVersion < Build.VERSION_CODES.Q) {
                environmentFileRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
            } else {
                File external = ApplicationData.mIncomeApplication.getExternalFilesDir(null);
                if (external != null) {
                    environmentFileRoot = external.getAbsolutePath();
                }
            }
        } else {
            environmentFileRoot = ApplicationData.mIncomeApplication.getFilesDir().getAbsolutePath();
        }
        return environmentFileRoot;
    }

    /**
     * 追加系统目录后面
     * @param dis
     * @return
     */
    public static String getFileRootPath(String dis) {
        return getFileRootPath() + dis;
    }

    /**
     * 获取文件名 （当前日期）
     * @param suffix
     * @return
     */
    public static String getFileName(String suffix) {
        SimpleDateFormat fileFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return fileFormat.format(new Date()) + "." + suffix;
    }


    /**
     * 获取文件名 （当前日期时分秒）
     * @param suffix
     * @return
     */
    public static String getFileName(String prefix, String suffix) {
        SimpleDateFormat fileFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        if(null != prefix) {
            return prefix + "_" + fileFormat.format(new Date()) + "." + suffix;
        }
        return fileFormat.format(new Date()) + "." + suffix;
    }

}
