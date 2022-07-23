package com.dwtedx.income.utility;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.Disposable;

/**
 * ================================================
 * <p> 权限申请工具
 * Created by ligang on 2021/4/12 19:38
 * ================================================
 */
public class PermissionsUtils {
    private static final Map<String, String[]> permissionMap = new HashMap<>();
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = "读写手机存储";
    public static final String PERMISSION_RECORD_AUDIO = "录音";
    public static final String PERMISSION_CAMERA = "相机";
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE_CAMERA = "读写手机存储、相机";
    public static final String PERMISSION_LOCATION = "定位";
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE_RECORD_AUDIO = "读写手机存储、录音";
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE_CAMERA_RECORD_AUDIO = "读写手机存储、相机、录音";
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE_CAMERA_LOCATION = "读写手机存储、相机、定位";
    public static final String PERMISSION_READ_CONTACTS = "读取通讯录";
    public static final String PERMISSION_READ_BLUETOOTH = "蓝牙";

    static {
        permissionMap.put(PERMISSION_WRITE_EXTERNAL_STORAGE, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
        permissionMap.put(PERMISSION_RECORD_AUDIO, new String[]{Manifest.permission.RECORD_AUDIO});
        permissionMap.put(PERMISSION_CAMERA, new String[]{Manifest.permission.CAMERA});
        permissionMap.put(PERMISSION_WRITE_EXTERNAL_STORAGE_CAMERA, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
        permissionMap.put(PERMISSION_LOCATION, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
        permissionMap.put(PERMISSION_WRITE_EXTERNAL_STORAGE_RECORD_AUDIO, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO});
        permissionMap.put(PERMISSION_WRITE_EXTERNAL_STORAGE_CAMERA_RECORD_AUDIO, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO});
        permissionMap.put(PERMISSION_WRITE_EXTERNAL_STORAGE_CAMERA_LOCATION, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
        permissionMap.put(PERMISSION_READ_CONTACTS, new String[]{Manifest.permission.READ_CONTACTS});
        permissionMap.put(PERMISSION_READ_BLUETOOTH, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN});
    }

    public static void requestPermission(Fragment fragment, Callback runnable, String permissionKey) {
        requestPermission(true, fragment, runnable, permissionKey);
    }

    public static void requestPermission(FragmentActivity activity, Callback runnable, String permissionKey) {
        requestPermission(true, activity, runnable, permissionKey);
    }

    public static void requestPermission(boolean mustRequest, FragmentActivity activity, Callback runnable, String permissionKey) {
        if (permissionMap.containsKey(permissionKey)) {
            requestPermission(null, activity, mustRequest, runnable, permissionKey, permissionMap.get(permissionKey));
        }
    }

    public static void requestPermission(boolean mustRequest, Fragment fragment, Callback runnable, String permissionKey) {
        if (permissionMap.containsKey(permissionKey)) {
            requestPermission(fragment, fragment.getActivity(), mustRequest, runnable, permissionKey, permissionMap.get(permissionKey));
        }
    }

    private static void requestPermission(Fragment fragment, FragmentActivity activity, boolean mustRequest, Callback runnable, String permissionTips, String... permissions) {
        RxPermissions rxPermissions;
        if (fragment != null) {
            rxPermissions = new RxPermissions(fragment);
        } else {
            rxPermissions = new RxPermissions(activity);
        }
        Set<String> noPermissionsList = getNoPermissionsList(rxPermissions, permissions);
        //判断权限是否全部被允许
        if (noPermissionsList == null || noPermissionsList.size() == 0) {
            if (runnable != null) {
                runnable.run(true);
            }
            return;
        }

        if (!(activity instanceof BaseActivity)) {
            return;
        }
        //app内部弹窗提示： 告知用户为什么申请权限
        String msgTips = getPermissionTips(noPermissionsList);
        new MaterialDialog.Builder(activity)
                .title("权限请求")
                .content(msgTips)
                .positiveText("同意")
                .negativeText("不同意")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //NEGATIVE   POSITIVE
                        if (which.name().equals("POSITIVE")) {
                            Disposable disposable = rxPermissions.requestEachCombined(permissions)
                                    .subscribe(permission -> {
                                        if (permission.granted) {
                                            if (runnable != null) {
                                                runnable.run(true);
                                            }
                                        } else if (permission.shouldShowRequestPermissionRationale) {
                                            Set<String> noPermissionsList = getNoPermissionsList(rxPermissions, permissions);
                                            ToastUtil.toastShow(getNoPermissionsToastTips(noPermissionsList));
                                            if (!mustRequest) {
                                                if (runnable != null) {
                                                    runnable.run(false);
                                                }
                                            }
                                        } else {
                                            Set<String> noPermissionsList = getNoPermissionsList(rxPermissions, permissions);
                                            new MaterialDialog.Builder(activity)
                                                    .title("权限请求")
                                                    .content(String.format(activity.getResources().getString(R.string.permissions_set_hint), getSettingTips(noPermissionsList)))
                                                    .positiveText("去设置")
                                                    .negativeText("不同意")
                                                    .onAny(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            //NEGATIVE   POSITIVE
                                                            if (which.name().equals("POSITIVE")) {
                                                                MiuiUtils.jumpToPermissionsEditorActivity(activity);
                                                            }
                                                            else if(which.name().equals("NEGATIVE")) {
                                                                if (!mustRequest) {
                                                                    if (runnable != null) {
                                                                        runnable.run(false);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });
                        }
                        else if(which.name().equals("NEGATIVE")) {
                            if (!mustRequest) {
                                if (runnable != null) {
                                    runnable.run(false);
                                }
                            }
                        }
                    }
                })
                .show();

    }

    /**
     * 获取权限说明提示
     * @param permissionList
     * @return
     */
    private static String getPermissionTips(Set<String> permissionList) {
        StringBuilder sb = new StringBuilder("“DD记账”想获取以下权限\n");
        int index = 0;
        for (String permission : permissionList) {
            switch (permission) {
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    sb.append("\n" + ++index + ".读写手机存储权限: 用于图片存储、读取文件等功能。");
                    break;
                case Manifest.permission.CAMERA:
                    sb.append("\n" + ++index + ".相机权限: 为了帮助你完成拍摄图片、拍摄视频，上传头像或扫一扫的功能。");
                    break;
                case Manifest.permission.RECORD_AUDIO:
                    sb.append("\n" + ++index + ".录音权限: 为了帮助你录制音频。");
                    break;
                case Manifest.permission.READ_CONTACTS:
                    sb.append("\n" + ++index + ".读取通讯录权限: 为了帮助你快速选择联系人。");
                    break;
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    sb.append("\n" + ++index + ".定位权限: 为了帮助你定位当前城市。");
                    break;
                case Manifest.permission.BLUETOOTH:
                    sb.append("\n" + ++index + ".蓝牙权限: 为了帮助你连接蓝牙设备。");
                    break;
            }
        }
        if (permissionList.size() == 1) {
            return sb.toString().replace("1.", "");
        }
        return sb.toString();
    }

    /**
     * 获取没有被允许的权限
     * @param rxPermissions
     * @param permissions
     * @return
     */
    private static Set<String> getNoPermissionsList(RxPermissions rxPermissions, String... permissions) {
        Set<String> noPermissionsList = null;
        //判断权限是否全部被允许
        for (String p : permissions) {
            if (!rxPermissions.isGranted(p)) {
                if (noPermissionsList == null) {
                    noPermissionsList = new LinkedHashSet<>();
                }
                noPermissionsList.add(p);
            }
        }
        return noPermissionsList;
    }

    /**
     * 获取没有权限时的toast提示信息
     * @param permissionList
     * @return
     */
    private static String getNoPermissionsToastTips(Set<String> permissionList){
        StringBuilder sb = new StringBuilder("没有【");
        for (String permission : permissionList) {
            switch (permission) {
                case Manifest.permission.CAMERA:
                    sb.append("相机、");
                    break;
                case Manifest.permission.RECORD_AUDIO:
                    sb.append("录音、");
                    break;
                case Manifest.permission.READ_CONTACTS:
                    sb.append("读取通讯录、");
                    break;
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    sb.append("定位、");
                    break;
                case Manifest.permission.BLUETOOTH:
                    sb.append("蓝牙、");
                    break;
                default:
                    sb.append("读写手机存储、");
                    break;
            }
        }
        if (permissionList.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("】权限");
        return sb.toString();
    }

    /**
     * 获取跳转到设置的提示信息
     * @param permissionList
     * @return
     */
    private static String getSettingTips(Set<String> permissionList) {
        StringBuilder sb = new StringBuilder("");
        for (String permission : permissionList) {
            switch (permission) {
                case Manifest.permission.CAMERA:
                    sb.append("、相机");
                    break;
                case Manifest.permission.RECORD_AUDIO:
                    sb.append("、录音");
                    break;
                case Manifest.permission.READ_CONTACTS:
                    sb.append("、读取通讯录");
                    break;
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    sb.append("、定位");
                    break;
                case Manifest.permission.BLUETOOTH:
                    sb.append("、蓝牙");
                    break;
                default:
                    sb.append("、读写手机存储");
                    break;
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    public interface Callback {
        void run(boolean hasPermissions);
    }
}
