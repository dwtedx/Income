package com.dwtedx.income.updateapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;
import com.dwtedx.income.R;
import com.dwtedx.income.utility.CommonConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/***
 * 升级服务
 * 文件名称：UpdateService.java
 * 作         者： qinyl
 * 完成日期：2012-12-24
 *
 * @author dwtedx
 *
 */
public class UpdateService extends Service {
    private final static String TAG = "UpdateService";

    private static final int TIMEOUT = 10 * 1000;// 超时
    private static String down_url;
    private static final int DOWN_OK = 1;
    private static final int DOWN_ERROR = 0;

    private String app_name;

    private NotificationManager notificationManager;
    private Notification.Builder notificationBuilder;
    private PendingIntent pendingIntent;

    //下载的文件相关
    public static File updateFile;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * 方法描述：onStartCommand方法
     *
     * @param intent intent, int flags, int startId
     * @return int
     * @see UpdateService
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        app_name = intent.getStringExtra("Key_App_Name");
        down_url = intent.getStringExtra("Key_Down_Url");
        // create file,应该在这个地方加一个返回值的判断SD卡是否准备好，文件是否创建成功，等等！
        if (createFile(app_name)) {
            createNotification();
            createThread();
        } else {
            Toast.makeText(this, R.string.insert_card, Toast.LENGTH_SHORT).show();
            //stop service
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    /********* update UI******/
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_OK:
                    //下载完成，点击安装
                    Uri uri = Uri.fromFile(updateFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    //判断是否是AndroidN以及更高的版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(UpdateService.this, getPackageName() + ".fileProvider", updateFile);
                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    } else {
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    }
                    pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, 0);

                    //下载成功通知
                    notificationBuilder.setContentIntent(pendingIntent)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .setProgress(0, 0, false)
                            .setAutoCancel(false)
                            .setContentTitle(app_name)
                            .setContentText(getString(R.string.down_sucess));
                    Notification notification;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        notification = notificationBuilder.build();
                    } else {
                        //noinspection deprecation
                        notification = notificationBuilder.getNotification();
                    }
                    //startForeground(2, notificationSuss);
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(2, notification);

                    //安装APK
                    installApk();

                    ///stop service
                    stopSelf();
                    break;

                case DOWN_ERROR:
                    notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .setAutoCancel(false)
                            .setContentTitle(app_name)
                            .setContentText(getString(R.string.down_fail));
                    Notification notificationErr;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        notificationErr = notificationBuilder.build();
                    } else {
                        //noinspection deprecation
                        notificationErr = notificationBuilder.getNotification();
                    }
                    //startForeground(3, notificationErr);
                    notificationErr.flags = Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(3, notificationErr);

                    //stop service
                    stopSelf();
                    break;

                default:
                    stopSelf();
                    break;
            }
        }
    };

    private void installApk() {
        //下载完成，点击安装
        Uri uri = Uri.fromFile(updateFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(UpdateService.this, getPackageName() + ".fileProvider", updateFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            //加这个属性是因为使用Context的startActivity方法的话，就需要开启一个新的task
        }
        UpdateService.this.startActivity(intent);
    }

    /**
     * 方法描述：createThread方法, 开线程下载
     * * @see UpdateService
     */
    public void createThread() {
        new DownLoadThread().start();
    }


    private class DownLoadThread extends Thread {
        @Override
        public void run() {
            Message message = new Message();
            try {
                long downloadSize = downloadUpdateFile(down_url, updateFile.toString());
                if (downloadSize > 0) {
                    // down success
                    notificationManager.cancel(1);
                    message.what = DOWN_OK;
                    handler.sendMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                message.what = DOWN_ERROR;
                handler.sendMessage(message);
            }
        }
    }

    /**
     * 方法描述：createNotification方法
     *
     * @see UpdateService
     */
    public void createNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(UpdateService.this, "77");
        }else{
            notificationBuilder = new Notification.Builder(UpdateService.this);
        }
        notificationBuilder.setWhen(System.currentTimeMillis())
                .setAutoCancel(false)
                .setContentTitle(app_name + getString(R.string.is_downing))
                .setProgress(100, 0, false)
                .setSmallIcon(android.R.drawable.stat_sys_download);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = notificationBuilder.build();
        } else {
            //noinspection deprecation
            notification = notificationBuilder.getNotification();
        }
        notification.flags = Notification.FLAG_NO_CLEAR;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //兼容android8.0
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("77", app_name + getString(R.string.is_downing), NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(false); //是否在桌面icon右上角展示小红点
            channel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
            channel.enableVibration(false);
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, notification);
    }

    /***
     * down file
     * return
     * throws MalformedURLException
     */
    public long downloadUpdateFile(String down_url, String file) throws Exception {

        int down_step = 1;// 提示step
        int totalSize;// 文件总大小
        int downloadCount = 0;// 已经下载好的大小
        int updateCount = 0;// 已经上传的文件大小

        InputStream inputStream;
        OutputStream outputStream;

        URL url = new URL(down_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        // 获取下载文件的size
        totalSize = httpURLConnection.getContentLength();

        if (httpURLConnection.getResponseCode() == 404) {
            throw new Exception("fail!");
            //这个地方应该加一个下载失败的处理，但是，因为我们在外面加了一个try---catch，已经处理了Exception,
            //所以不用处理
        }

        inputStream = httpURLConnection.getInputStream();
        outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉

        byte buffer[] = new byte[1024];
        int readsize;

        while ((readsize = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, readsize);
            downloadCount += readsize;// 时时获取下载到的大小
            //每次增张1
            if (updateCount == 0 || (downloadCount * 100 / totalSize - down_step) >= updateCount) {
                updateCount += down_step;
                // 改变通知栏
                notificationBuilder.setProgress(100, updateCount, false);
                notificationBuilder.setContentText(getString(R.string.is_downing) + updateCount + "%");
                Notification notification;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    notification = notificationBuilder.build();
                } else {
                    //noinspection deprecation
                    notification = notificationBuilder.getNotification();
                }
                notification.flags = Notification.FLAG_NO_CLEAR;
                notificationManager.notify(1, notification);
            }
        }

        httpURLConnection.disconnect();
        inputStream.close();
        outputStream.close();

        return downloadCount;
    }

    /**
     * 方法描述：createFile方法
     * @return
     */
    public boolean createFile(String app_name) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            File updateDir = new File(CommonConstants.INCOME +"/");
            updateFile = new File(updateDir + "/" + app_name + ".apk");

            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            if (updateFile.exists()) {
                updateFile.delete();
            }
            try {
                updateFile.createNewFile();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static int getAPKVersionCode(Context context) {
        int versionCode = 1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionCode = pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.v(TAG, e.getMessage());
        }

        return versionCode;
    }

    public static String getAPKVersion(Context context) {
        String version = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            version = context.getString(R.string.version_name) + pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.v(TAG, e.getMessage());
        }
        return version;
    }

}