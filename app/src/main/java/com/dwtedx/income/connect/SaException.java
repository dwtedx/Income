package com.dwtedx.income.connect;

/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 上午9:20.
 * @Company 路之遥网络科技有限公司
 * @Description SaException 异常
 */
public class SaException extends Exception {

    private SaError mSaError;

    /**
     * 提供http状态码不为200的异常
     * @param errorCode
     */
    public SaException(int errorCode) {
        if (mSaError == null) {
            mSaError = new SaError();
        }
        mSaError.setmCode(errorCode);
        mSaError.setmMessage("服务器迷路了(" + errorCode+")、正在玩命寻路中");
        mSaError.setmType(SaError.SAERROR_TYPE_COMUNICATION);
    }

    /**
     * 提供本地错误的异常
     * @param errorCode
     * @param cause
     */
    public SaException(int errorCode, Throwable cause) {
        super(cause);
        if (mSaError == null) {
            mSaError = new SaError();
        }
        mSaError.setmCode(errorCode);
        if(null != cause && null != cause.getMessage()) {
            mSaError.setmMessage(cause.getMessage().toString());
        }
        mSaError.setmType(SaError.SAERROR_TYPE_LOCAL);
    }

    /**
     * 提供远程服务器的异常
     * @param error
     */
    public SaException(SaError error) {
        super();
        this.mSaError = error;
    }

    public int getErrorCode() {
        if (mSaError != null) {
            return mSaError.getmCode();
        }
        return 0;
    }

    public int getErrorType() {
        if (mSaError != null) {
            return mSaError.getmType();
        }
        return 0;
    }

    public String getErrorMessage() {
        if (mSaError != null) {
            return mSaError.getmMessage();
        }
        return "";
    }

    public String getErrorServerCode() {
        if (mSaError != null) {
            return mSaError.getmServerCode();
        }
        return "";
    }

    public String getErrorLogPath() {
        if (mSaError != null) {
            return mSaError.getmLogPath();
        }
        return "";
    }

}
