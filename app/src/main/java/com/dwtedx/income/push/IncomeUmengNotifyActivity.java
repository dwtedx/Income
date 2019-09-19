package com.dwtedx.income.push;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dwtedx.income.MainActivity;
import com.dwtedx.income.R;
import com.dwtedx.income.entity.UmengPushInfo;
import com.dwtedx.income.utility.ParseJsonToObject;
import com.umeng.message.UmengNotifyClickActivity;

import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class IncomeUmengNotifyActivity extends UmengNotifyClickActivity {

    private static String TAG = IncomeUmengNotifyActivity.class.getName();
    private static String AFTER_OPEN_GO_APP = "go_app";
    private static String AFTER_OPEN_GO_URL = "go_url";
    private static String AFTER_OPEN_GO_ACTIVITY = "go_activity";
    private static String AFTER_OPEN_GO_CUSTOM = "go_custom";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mipush);
    }
    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Log.i(TAG, body);
        UmengPushInfo umengPushInfo;
        try {
            umengPushInfo = ParseJsonToObject.getObject(UmengPushInfo.class, new JSONObject(body));
        } catch (JSONException e) {
            e.printStackTrace();
            umengPushInfo = null;
        }
        if(null != umengPushInfo){
            if(null != umengPushInfo.getBody()
                    && null != umengPushInfo.getBody().getAfter_open()){
                if(AFTER_OPEN_GO_APP.equals(umengPushInfo.getBody().getAfter_open())){
                    Intent intentPush = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentPush);
                    finish();
                }
                if(AFTER_OPEN_GO_URL.equals(umengPushInfo.getBody().getAfter_open())){

                }
                if(AFTER_OPEN_GO_ACTIVITY.equals(umengPushInfo.getBody().getAfter_open())){

                }
                if(AFTER_OPEN_GO_CUSTOM.equals(umengPushInfo.getBody().getAfter_open())){

                }
            }
        }
    }

}
