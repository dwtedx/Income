package com.dwtedx.income.connect;

/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 上午9:18.
 * @Company 路之遥网络科技有限公司
 * @Description 错误码
 */
@SuppressWarnings("deprecation")
public class SaError {

    // 本地错误错里
    public static final int SAERROR_TYPE_LOCAL = 0;
    // 网络连接错误
    public static final int SAERROR_TYPE_COMUNICATION = 1;
    // 远程服务器返回错误的处理结果
    public static final int SAERROR_TYPE_REMOTE = 2;

    /**
     * 本地错误码
     */
    public static final int ERROR_TYPE_MALFORMED_URL = 7001;
    public static final int ERROR_TYPE_PROTOCOL = 7002;
    public static final int ERROR_TYPE_SOCKET = 7003;
    public static final int ERROR_TYPE_IO = 7004;
    public static final int ERROR_TYPE_UNKNOWN = 7005;
    public static final int ERROR_TYPE_JSON = 7006;
    public static final int ERROR_TYPE_TIMEOUT = 7007;
    public static final int ERROR_TYPE_EOF = 7008;

    /**
     * 错误类型
     * 0 本地错误错里
     * 1 http 网络连接错误
     * 2 远程服务器返回错误的处理结果
     */
    private int mType;
    private int mCode;
    private String mMessage;
    private String mLogPath;

    /**
     * 远程服务器返回错误码
     */
    private String mServerCode;


    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public int getmCode() {
        return mCode;
    }

    public void setmCode(int mCode) {
        this.mCode = mCode;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmLogPath() {
        return mLogPath;
    }

    public void setmLogPath(String mLogPath) {
        this.mLogPath = mLogPath;
    }

    public String getmServerCode() {
        return mServerCode;
    }

    public void setmServerCode(String mServerCode) {
        this.mServerCode = mServerCode;
    }

    public static SaError getServerInfoError(ResponseData responseInfo) {
        SaError error = null;
        if (null != responseInfo && ResponseData.REQUEST_SUCCESS != responseInfo.getmHead().getErrorCode()) {
            error = new SaError();
            error.setmServerCode(responseInfo.getmHead().getMsg());
            error.setmType(SAERROR_TYPE_REMOTE);
            error.setmMessage(responseInfo.getmHead().getMsg());
        }
        return error;
    }
}

