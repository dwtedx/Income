package com.dwtedx.income.service;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServiceAPI {

    private final static String CONFIG_FILE_NAME = "income_config_data.tmp";
    private final static String CONFIG_SERVER_NAME = "server_uri";

//    public static String WEB_API_DOMAIN = "https://diapp.dwtedx.com";//正式
    public static String WEB_API_DOMAIN = "http://172.16.118.114:8080/incomeser";//开发
//    public static String WEB_API_DOMAIN = "http://192.168.31.114:8080/incomeser";//开发mac mini

    public static String WEB_API_USER_OTHER_LOGIN = WEB_API_DOMAIN + "/user/otherlogin";
    public static String WEB_API_USER_OTHER_LOGINV2 = WEB_API_DOMAIN + "/user/otherloginv2";
    public static String WEB_API_USER_USERBYID = WEB_API_DOMAIN + "/user/userbyid";
    public static String WEB_API_USER_UPLOADPIC = WEB_API_DOMAIN + "/user/uploadpic";
    public static String WEB_API_USER_UPDATEUSERNAME = WEB_API_DOMAIN + "/user/updateusername";
    public static String WEB_API_USER_UPDATEUSERPHONE = WEB_API_DOMAIN + "/user/updateuserphone";
    public static String WEB_API_USER_UPDATEUSERINFO = WEB_API_DOMAIN + "/user/updateuserinfo";
    public static String WEB_API_USER_UPDATEPASSWORD = WEB_API_DOMAIN + "/user/updatepassword";
    public static String WEB_API_USER_RESETPASSWORD = WEB_API_DOMAIN + "/user/resetpassword";
    public static String WEB_API_USER_LOGIN = WEB_API_DOMAIN + "/user/login";
    public static String WEB_API_USER_REGISTER = WEB_API_DOMAIN + "/user/register";
    public static String WEB_API_USER_REGISTERBYPHONE = WEB_API_DOMAIN + "/user/registerbyphone";

    public static String WEB_API_VERSION_UPDATE = WEB_API_DOMAIN + "/version/update";

    public static String WEB_API_INCOME_SYNCHRONIZEID = WEB_API_DOMAIN + "/income/synchronizeid";
    public static String WEB_API_INCOME_SYNCHRONIZE_SINGLE = WEB_API_DOMAIN + "/income/synchronizesingle";
    public static String WEB_API_INCOME_SYNCHRONIZE_SINGLEID = WEB_API_DOMAIN + "/income/synchronizesingleid";
    public static String WEB_API_INCOME_SAVEINCOMESCANSINGLEID = WEB_API_DOMAIN + "/income/saveincomescansingleid";
    public static String WEB_API_INCOME_SYNCHRONIZE_EGAIN = WEB_API_DOMAIN + "/income/synchronizeegain";
    public static String WEB_API_INCOME_DELETEINCOME = WEB_API_DOMAIN + "/income/deleteincome";
    public static String WEB_API_INCOME_DELETEINCOMEID = WEB_API_DOMAIN + "/income/deleteincomeid";
    public static String WEB_API_INCOME_UPDATEINCOMEBEFORTIME = WEB_API_DOMAIN + "/income/updateincomebefortime";

    public static String WEB_API_SCAN_DELETESCAN = WEB_API_DOMAIN + "/scan/deletescan";

    public static String WEB_API_TYPE_ADDTYPE = WEB_API_DOMAIN + "/type/addtype";
    public static String WEB_API_TYPE_UPDATETYPE = WEB_API_DOMAIN + "/type/updatetype";
    public static String WEB_API_TYPE_DELETETYPE = WEB_API_DOMAIN + "/type/deletetype";
    public static String WEB_API_TYPE_GETTYPE = WEB_API_DOMAIN + "/type/gettype";

    public static String WEB_API_ACCOUNT_ADDACCOUNT = WEB_API_DOMAIN + "/account/addaccount";
    public static String WEB_API_ACCOUNT_DELETEACCOUNT = WEB_API_DOMAIN + "/account/deleteaccount";
    public static String WEB_API_ACCOUNT_GETACCOUNT = WEB_API_DOMAIN + "/account/getaccount";

    public static String WEB_API_BUDGET_SYNCHRONIZE = WEB_API_DOMAIN + "/budget/synchronize";
    public static String WEB_API_BUDGET_SYNCHRONIZESINGLE = WEB_API_DOMAIN + "/budget/synchronizesingle";

    public static String WEB_API_SMS_SENDSMS = WEB_API_DOMAIN + "/sms/sendsms";
    public static String WEB_API_SMS_SENDSMSHAVE = WEB_API_DOMAIN + "/sms/sendsmshave";

    public static String WEB_API_PARA_GETSCAN = WEB_API_DOMAIN + "/para/getscan";

    public static String WEB_API_TAOBAO_SEARCH = WEB_API_DOMAIN + "/taobao/search";
    public static String WEB_API_TAOBAO_RECOMMEND = WEB_API_DOMAIN + "/taobao/recommend";
    public static String WEB_API_TAOBAO_TOPTAOBAOITEMINFO = WEB_API_DOMAIN + "/taobao/toptaobaoiteminfo";
    public static String WEB_API_TAOBAO_REFRESHTAOBAOITEMINFO = WEB_API_DOMAIN + "/taobao/refreshtaobaoiteminfo";
    public static String WEB_API_TAOBAO_WOMENSCLOTHINGITEM = WEB_API_DOMAIN + "/taobao/womensclothingitem";
    public static String WEB_API_TAOBAO_COUPONITEM = WEB_API_DOMAIN + "/taobao/couponitem";
    public static String WEB_API_TAOBAO_MANCLOTHINGITEM = WEB_API_DOMAIN + "/taobao/manclothingitem";
    public static String WEB_API_TAOBAO_SNACKSITEM = WEB_API_DOMAIN + "/taobao/snacksitem";
    public static String WEB_API_TAOBAO_MAKEUPSITEM = WEB_API_DOMAIN + "/taobao/makeupsitem";
    public static String WEB_API_TAOBAO_DIGITALITEM = WEB_API_DOMAIN + "/taobao/digitalitem";
    public static String WEB_API_TAOBAO_UNDERWEARITEM = WEB_API_DOMAIN + "/taobao/underwearitem";
    public static String WEB_API_TAOBAO_CHILDREN = WEB_API_DOMAIN + "/taobao/childrenitem";
    public static String WEB_API_TAOBAO_MOTHERBABYITEM = WEB_API_DOMAIN + "/taobao/motherbabyitem";
    public static String WEB_API_TAOBAO_LIVEHOME = WEB_API_DOMAIN + "/taobao/livehomeitem";
    public static String WEB_API_TAOBAO_LUGGAGEITEM = WEB_API_DOMAIN + "/taobao/luggageitem";
    public static String WEB_API_TAOBAO_OTHERITEM = WEB_API_DOMAIN + "/taobao/otheritem";
    public static String WEB_API_TAOBAO_NINENINETAOBAOITEM = WEB_API_DOMAIN + "/taobao/nineninetaobaoitem";
    public static String WEB_API_TAOBAO_RANDITEM = WEB_API_DOMAIN + "/taobao/randitem";
    public static String WEB_API_TAOBAO_TAOBAOACTIVITYINFO = WEB_API_DOMAIN + "/taobao/taobaoactivityinfo";
    public static String WEB_API_TAOBAO_CATEGORYTOP = WEB_API_DOMAIN + "/taobao/categorytop";
    public static String WEB_API_TAOBAO_CATEGORYIDITEM = WEB_API_DOMAIN + "/taobao/categoryiditem";

    public static String WEB_API_TOPIC_INDEX = WEB_API_DOMAIN + "/topic/index";
    public static String WEB_API_TOPIC_MYTOPIC = WEB_API_DOMAIN + "/topic/mytopic";
    public static String WEB_API_TOPIC_FINDTOPIC = WEB_API_DOMAIN + "/topic/findtopic";
    public static String WEB_API_TOPIC_SEVEVOTERESULT = WEB_API_DOMAIN + "/topic/sevevoteresult";
    public static String WEB_API_TOPIC_SEVETOPICLIKED = WEB_API_DOMAIN + "/topic/sevetopicliked";
    public static String WEB_API_TOPIC_UPLOADIMG = WEB_API_DOMAIN + "/topic/uploadimg";
    public static String WEB_API_TOPIC_SEVETOPIC = WEB_API_DOMAIN + "/topic/sevetopic";
    public static String WEB_API_TOPIC_SEVETOPICTALK = WEB_API_DOMAIN + "/topic/sevetopictalk";
    public static String WEB_API_TOPIC_SEVETOPICSHARE = WEB_API_DOMAIN + "/topic/sevetopicshare";
    public static String WEB_API_TOPIC_DELETETOPIC = WEB_API_DOMAIN + "/topic/deletetopic";

    public static String WEB_API_EXPEXCEL_POOL = WEB_API_DOMAIN + "/expexcel/pool";
    public static String WEB_API_EXPEXCEL_SAVE = WEB_API_DOMAIN + "/expexcel/save";

    /**
     * @return void    返回类型
     * @throws
     * @Title: loadGlobalData
     * @Description: 本地配置url
     * @author qinyl http://dwtedx.com
     */
    public static void loadGlobalData() {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator +
                CONFIG_FILE_NAME);
        if (file.exists()) {
            try {
                FileInputStream inStream = new FileInputStream(file);
                InputStreamReader inputreader = new InputStreamReader(inStream);
                BufferedReader dataIO = new BufferedReader(inputreader);
                String strLine = null;
                while ((strLine = dataIO.readLine()) != null) {
                    if (strLine.startsWith("//")) {
                        continue;
                    }
                    String[] dataArray = strLine.split("=");
                    if (null != dataArray && dataArray.length >= 2) {
                        if (dataArray[1].contains(";")) {
                            if (dataArray[0].equals(CONFIG_SERVER_NAME)) {
                                WEB_API_DOMAIN = dataArray[1].substring(0, dataArray[1].indexOf
                                        (';'));
                            }
                        }
                    }
                }
                dataIO.close();
                inputreader.close();
                inStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setGlobalDataUrl();
        }
    }


    private static void setGlobalDataUrl() {

        WEB_API_USER_OTHER_LOGIN = WEB_API_DOMAIN + "/user/qqlogin";

    }

}
