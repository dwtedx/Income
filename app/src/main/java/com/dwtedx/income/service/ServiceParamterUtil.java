package com.dwtedx.income.service;




import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.MD5Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServiceParamterUtil {


    public static JSONObject genParamterJSONObject (JSONObject body) {
        JSONObject result = new JSONObject();
        try {
            result.put("head", getParamterHead(body));
            result.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject genParamterJSONObject (JSONArray body) {
        JSONObject result = new JSONObject();
        try {
            result.put("head", getParamterHead(body));
            result.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static JSONObject getParamterHead(JSONObject body) {
        JSONObject head = new JSONObject();
        try {
            head.put("clientType", CommonConstants.ClientType);
            head.put("clientSID", ApplicationData.mClientSID);
            head.put("clientVersion", ApplicationData.mAppVersionCode);

            String md5Str = body.toString() + ";jFX024sn0gk08m8J630PJq7D787sWNnIQYLdwtedx199117??";
            md5Str = md5Str.replaceAll("/", "");
            md5Str = md5Str.replaceAll("\\\\", "");

            String sign = MD5Util.stringToMD5(md5Str);
            head.put("sign", sign);

            if(null != ApplicationData.mDiUserInfo) {
                head.put("userId", ApplicationData.mDiUserInfo.getId());
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return head;


    }

    private static JSONObject getParamterHead(JSONArray body) {
        JSONObject head = new JSONObject();
        try {
            head.put("clientType", CommonConstants.ClientType);
            head.put("clientSID", ApplicationData.mClientSID);
            head.put("clientVersion", ApplicationData.mAppVersionCode);

            String md5Str = body.toString() + ";jFX024sn0gk08m8J630PJq7D787sWNnIQYLdwtedx199117??";
            md5Str = md5Str.replaceAll("/", "");
            md5Str = md5Str.replaceAll("\\\\", "");

            String sign = MD5Util.stringToMD5(md5Str);
            head.put("sign", sign);

            if(null != ApplicationData.mDiUserInfo) {
                head.put("userId", ApplicationData.mDiUserInfo.getId());
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return head;


    }


}
