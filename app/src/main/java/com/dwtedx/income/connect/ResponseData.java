package com.dwtedx.income.connect;

import com.dwtedx.income.utility.JsonDataUtility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 上午9:15.
 * @Company 路之遥网络科技有限公司
 * @Description ResponseData
 */
public class ResponseData {
    public final static int REQUEST_SUCCESS = 10000;

    public final static String PROPERTY_KEY_HEAD = "head";
    public final static String PROPERTY_KEY_BODY = "body";
    public final static String PROPERTY_KEY_ERRMSG = "message";
    public final static String PROPERTY_KEY_ERRCODE = "errorCode";

    private Head mHead;
    private Object mBody;

    public ResponseData(String data) throws SaException {
        JSONObject object;
        try {
            object = new JSONObject(data);
            mHead = new Head(JsonDataUtility.getJSONObject(object, PROPERTY_KEY_HEAD));

            //数据
            mBody = JsonDataUtility.getJSONObject(object, PROPERTY_KEY_BODY);
            if (null == mBody) {
                mBody = JsonDataUtility.getJSONArray(object, PROPERTY_KEY_BODY);
            }
            if (null == mBody) {
                mBody = JsonDataUtility.getString(object, PROPERTY_KEY_BODY, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new SaException(SaError.ERROR_TYPE_JSON, e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SaException(SaError.ERROR_TYPE_IO, e);
        }
    }

    public Head getmHead() {
        return mHead;
    }

    public void setmHead(Head mHead) {
        this.mHead = mHead;
    }

    public Object getmBody() {
        return mBody;
    }

    public void setmBody(Object mBody) {
        this.mBody = mBody;
    }

    class Head {

        private int ErrorCode;
        private String Msg;

        public Head(JSONObject object) {
            ErrorCode = JsonDataUtility.getInt(object, PROPERTY_KEY_ERRCODE, 10001);
            Msg = JsonDataUtility.getString(object, PROPERTY_KEY_ERRMSG, null);
        }

        public int getErrorCode() {
            return ErrorCode;
        }

        public void setErrorCode(int errorCode) {
            ErrorCode = errorCode;
        }

        public String getMsg() {
            return Msg;
        }

        public void setMsg(String msg) {
            Msg = msg;
        }
    }

}