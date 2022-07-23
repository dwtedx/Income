package com.dwtedx.income.utility;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

public class MiuiUtils {
    public static final int request_code_permission = 100;

    /**
     * 跳转到MIUI应用权限设置页面
     *
     * @param context context
     */
    public static void jumpToPermissionsEditorActivity(Activity context) {
        if (isMIUI()) {
            Log.d("MiuiUtils", "IS_MIUI");
            try {
                // MIUI 8
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", context.getPackageName());
                context.startActivityForResult(localIntent, request_code_permission);
                Log.d("MiuiUtils", "MIUI 8");
            } catch (Exception e) {
                Log.d("MiuiUtils", "not MIUI 8");
                try {
                    // MIUI 5/6/7
                    Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                    localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                    localIntent.putExtra("extra_pkgname", context.getPackageName());
                    context.startActivityForResult(localIntent, request_code_permission);
                    Log.d("MiuiUtils", "MIUI 5/6/7");
                } catch (Exception e1) {
                    Log.d("MiuiUtils", "not MIUI 5/6/7");
                    // 否则跳转到应用详情
                    jumpToCommonAppSettingActivity(context);
                }
            }
        } else {
            jumpToCommonAppSettingActivity(context);
        }
    }

    /**
     * 跳转到应用详情
     *
     * @param context
     */
    private static void jumpToCommonAppSettingActivity(Activity context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivityForResult(intent, request_code_permission);
    }

    /**
     * 判断是否是MIUI
     */
    private static boolean isMIUI() {
        String device = Build.MANUFACTURER;
        if (device.equalsIgnoreCase("Xiaomi")) {
            return true;
        }
        return false;
    }
}
