package com.dwtedx.income.service;

import android.annotation.SuppressLint;

import com.dwtedx.income.connect.BaseHttpResponseHandler;
import com.dwtedx.income.connect.SaAsyncTask;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaError;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.entity.DiBudget;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.entity.DiUserInfo;
import com.dwtedx.income.entity.DiVersion;
import com.dwtedx.income.entity.IdInfo;
import com.dwtedx.income.entity.UMengInfo;
import com.dwtedx.income.utility.ParseJsonToObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@SuppressWarnings({"JavaDoc", "UnusedReturnValue", "UnnecessaryLocalVariable"})
@SuppressLint("StaticFieldLeak")
public class UserService {
    private static UserService mService = null;

    private UserService() {
    }

    public static UserService getInstance() {
        if (mService == null)
            mService = new UserService();
        return mService;
    }

    /**
     * OtherLogin
     *
     * @param uMengInfo
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, DiUserInfo> otherLogin(final UMengInfo uMengInfo, SaDataProccessHandler<Void, Void, DiUserInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_USER_OTHER_LOGIN);
        SaAsyncTask<Void, Void, DiUserInfo> task = new SaAsyncTask<Void, Void, DiUserInfo>(handler) {
            @Override
            protected DiUserInfo doInBackground(Void... params) {
                DiUserInfo result = null;
                try {
                    BaseHttpResponseHandler<DiUserInfo> dataHandler = new
                            BaseHttpResponseHandler<DiUserInfo>() {

                                @Override
                                public DiUserInfo getResult(Object jsonObject) throws SaException {
                                    DiUserInfo resultData = ParseJsonToObject.getObject(DiUserInfo.class,
                                            (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("name", uMengInfo.getName());
                        requestParameter.put("head", uMengInfo.getHead());
                        requestParameter.put("sex", uMengInfo.getSex());
                        requestParameter.put("qqopenid", uMengInfo.getQqopenid());
                        requestParameter.put("weixinopenid", uMengInfo.getWeixinopenid());
                        requestParameter.put("sinaopenid", uMengInfo.getSinaopenid());
                        requestParameter.put("othertype", uMengInfo.getOthertype());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }

                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return result;
            }
        };
        task.execute();
        return task;
    }

    /**
     * userbyid
     *
     * @param userId
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, DiUserInfo> getUserById(final int userId, SaDataProccessHandler<Void, Void, DiUserInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_USER_USERBYID);
        SaAsyncTask<Void, Void, DiUserInfo> task = new SaAsyncTask<Void, Void, DiUserInfo>(handler) {
            @Override
            protected DiUserInfo doInBackground(Void... params) {
                DiUserInfo result = null;
                try {
                    BaseHttpResponseHandler<DiUserInfo> dataHandler = new
                            BaseHttpResponseHandler<DiUserInfo>() {

                                @Override
                                public DiUserInfo getResult(Object jsonObject) throws SaException {
                                    DiUserInfo resultData = ParseJsonToObject.getObject(DiUserInfo.class,
                                            (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("id", userId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }

                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return result;
            }
        };
        task.execute();
        return task;
    }


    public SaAsyncTask<Void, Void, DiVersion> versionUpdate(SaDataProccessHandler<Void, Void, DiVersion> handler) {
        handler.setUrl(ServiceAPI.WEB_API_VERSION_UPDATE);
        SaAsyncTask<Void, Void, DiVersion> task = new SaAsyncTask<Void, Void, DiVersion>(handler) {
            @Override
            protected DiVersion doInBackground(Void... params) {
                DiVersion result = null;
                try {
                    BaseHttpResponseHandler<DiVersion> dataHandler = new
                            BaseHttpResponseHandler<DiVersion>() {

                                @Override
                                public DiVersion getResult(Object jsonObject) throws SaException {
                                    DiVersion resultData = ParseJsonToObject.getObject(DiVersion.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("versionCode", ApplicationData.mAppVersionCode);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }

                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return result;
            }
        };
        task.execute();
        return task;
    }

    public SaAsyncTask<Void, Void, DiUserInfo> uploadPic(final int id, final String imgData, SaDataProccessHandler<Void, Void, DiUserInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_USER_UPLOADPIC);
        SaAsyncTask<Void, Void, DiUserInfo> task = new SaAsyncTask<Void, Void, DiUserInfo>(handler) {
            @Override
            protected DiUserInfo doInBackground(Void... params) {
                DiUserInfo result = null;
                try {
                    BaseHttpResponseHandler<DiUserInfo> dataHandler = new
                            BaseHttpResponseHandler<DiUserInfo>() {

                                @Override
                                public DiUserInfo getResult(Object jsonObject) throws SaException {
                                    DiUserInfo resultData = ParseJsonToObject.getObject(DiUserInfo.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("id", id);
                        requestParameter.put("head", imgData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }

                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return result;
            }
        };
        task.execute();
        return task;
    }

    public SaAsyncTask<Void, Void, DiUserInfo> updateUserName(final String userName, SaDataProccessHandler<Void, Void, DiUserInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_USER_UPDATEUSERNAME);
        SaAsyncTask<Void, Void, DiUserInfo> task = new SaAsyncTask<Void, Void, DiUserInfo>(handler) {
            @Override
            protected DiUserInfo doInBackground(Void... params) {
                DiUserInfo result = null;
                try {
                    BaseHttpResponseHandler<DiUserInfo> dataHandler = new
                            BaseHttpResponseHandler<DiUserInfo>() {

                                @Override
                                public DiUserInfo getResult(Object jsonObject) throws SaException {
                                    DiUserInfo resultData = ParseJsonToObject.getObject(DiUserInfo.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("username", userName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }

                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return result;
            }
        };
        task.execute();
        return task;
    }

    public SaAsyncTask<Void, Void, DiUserInfo> updateUserInfo(final DiUserInfo userInfo, SaDataProccessHandler<Void, Void, DiUserInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_USER_UPDATEUSERINFO);
        SaAsyncTask<Void, Void, DiUserInfo> task = new SaAsyncTask<Void, Void, DiUserInfo>(handler) {
            @Override
            protected DiUserInfo doInBackground(Void... params) {
                DiUserInfo result = null;
                try {
                    BaseHttpResponseHandler<DiUserInfo> dataHandler = new
                            BaseHttpResponseHandler<DiUserInfo>() {

                                @Override
                                public DiUserInfo getResult(Object jsonObject) throws SaException {
                                    DiUserInfo resultData = ParseJsonToObject.getObject(DiUserInfo.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(userInfo);

                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return result;
            }
        };
        task.execute();
        return task;
    }

    public SaAsyncTask<Void, Void, DiUserInfo> updatePassWord(final int type, final String password, final String passwordNew, SaDataProccessHandler<Void, Void, DiUserInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_USER_UPDATEPASSWORD);
        SaAsyncTask<Void, Void, DiUserInfo> task = new SaAsyncTask<Void, Void, DiUserInfo>(handler) {
            @Override
            protected DiUserInfo doInBackground(Void... params) {
                DiUserInfo result = null;
                try {
                    BaseHttpResponseHandler<DiUserInfo> dataHandler = new
                            BaseHttpResponseHandler<DiUserInfo>() {

                                @Override
                                public DiUserInfo getResult(Object jsonObject) throws SaException {
                                    DiUserInfo resultData = ParseJsonToObject.getObject(DiUserInfo.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("type", type);
                        requestParameter.put("password", password);
                        requestParameter.put("passwordnew", passwordNew);
                        requestParameter.put("passwordconfig", passwordNew);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }

                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return result;
            }
        };
        task.execute();
        return task;
    }

    public SaAsyncTask<Void, Void, DiUserInfo> login(final String name, final String passWord, SaDataProccessHandler<Void, Void, DiUserInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_USER_LOGIN);
        SaAsyncTask<Void, Void, DiUserInfo> task = new SaAsyncTask<Void, Void, DiUserInfo>(handler) {
            @Override
            protected DiUserInfo doInBackground(Void... params) {
                DiUserInfo result = null;
                try {
                    BaseHttpResponseHandler<DiUserInfo> dataHandler = new
                            BaseHttpResponseHandler<DiUserInfo>() {

                                @Override
                                public DiUserInfo getResult(Object jsonObject) throws SaException {
                                    DiUserInfo resultData = ParseJsonToObject.getObject(DiUserInfo.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("username", name);
                        requestParameter.put("password", passWord);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }

                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return result;
            }
        };
        task.execute();
        return task;
    }

    public SaAsyncTask<Void, Void, DiUserInfo> register(final UMengInfo mUMengInfo, SaDataProccessHandler<Void, Void, DiUserInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_USER_REGISTER);
        SaAsyncTask<Void, Void, DiUserInfo> task = new SaAsyncTask<Void, Void, DiUserInfo>(handler) {
            @Override
            protected DiUserInfo doInBackground(Void... params) {
                DiUserInfo result = null;
                try {
                    BaseHttpResponseHandler<DiUserInfo> dataHandler = new
                            BaseHttpResponseHandler<DiUserInfo>() {

                                @Override
                                public DiUserInfo getResult(Object jsonObject) throws SaException {
                                    DiUserInfo resultData = ParseJsonToObject.getObject(DiUserInfo.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(mUMengInfo);
                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return result;
            }
        };
        task.execute();
        return task;
    }

    public SaAsyncTask<Void, Void, DiUserInfo> registerByPhone(final UMengInfo mUMengInfo, SaDataProccessHandler<Void, Void, DiUserInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_USER_REGISTERBYPHONE);
        SaAsyncTask<Void, Void, DiUserInfo> task = new SaAsyncTask<Void, Void, DiUserInfo>(handler) {
            @Override
            protected DiUserInfo doInBackground(Void... params) {
                DiUserInfo result = null;
                try {
                    BaseHttpResponseHandler<DiUserInfo> dataHandler = new
                            BaseHttpResponseHandler<DiUserInfo>() {

                                @Override
                                public DiUserInfo getResult(Object jsonObject) throws SaException {
                                    DiUserInfo resultData = ParseJsonToObject.getObject(DiUserInfo.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(mUMengInfo);
                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return result;
            }
        };
        task.execute();
        return task;
    }

    /**
     * OtherLogin
     *
     * @param uMengInfo
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, DiUserInfo> otherLoginV2(final UMengInfo uMengInfo, SaDataProccessHandler<Void, Void, DiUserInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_USER_OTHER_LOGINV2);
        SaAsyncTask<Void, Void, DiUserInfo> task = new SaAsyncTask<Void, Void, DiUserInfo>(handler) {
            @Override
            protected DiUserInfo doInBackground(Void... params) {
                DiUserInfo result = null;
                try {
                    BaseHttpResponseHandler<DiUserInfo> dataHandler = new
                            BaseHttpResponseHandler<DiUserInfo>() {

                                @Override
                                public DiUserInfo getResult(Object jsonObject) throws SaException {
                                    DiUserInfo resultData = ParseJsonToObject.getObject(DiUserInfo.class,
                                            (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("name", uMengInfo.getName());
                        requestParameter.put("head", uMengInfo.getHead());
                        requestParameter.put("sex", uMengInfo.getSex());
                        requestParameter.put("qqopenid", uMengInfo.getQqopenid());
                        requestParameter.put("weixinopenid", uMengInfo.getWeixinopenid());
                        requestParameter.put("sinaopenid", uMengInfo.getSinaopenid());
                        requestParameter.put("othertype", uMengInfo.getOthertype());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }

                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return result;
            }
        };
        task.execute();
        return task;
    }

    public SaAsyncTask<Void, Void, Void> sendSms(final String phone, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_SMS_SENDSMS);
        SaAsyncTask<Void, Void, Void> task = new SaAsyncTask<Void, Void, Void>(handler) {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    BaseHttpResponseHandler<Void> dataHandler = new BaseHttpResponseHandler<Void>() {

                                @Override
                                public Void getResult(Object jsonObject) throws SaException {

                                    return null;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("phone", phone);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }

                    handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return null;
            }
        };
        task.execute();
        return task;
    }

    public SaAsyncTask<Void, Void, Void> sendSmsHave(final String phone, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_SMS_SENDSMSHAVE);
        SaAsyncTask<Void, Void, Void> task = new SaAsyncTask<Void, Void, Void>(handler) {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    BaseHttpResponseHandler<Void> dataHandler = new BaseHttpResponseHandler<Void>() {

                                @Override
                                public Void getResult(Object jsonObject) throws SaException {

                                    return null;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("phone", phone);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }

                    handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return null;
            }
        };
        task.execute();
        return task;
    }

    public SaAsyncTask<Void, Void, DiUserInfo> updateUserPhone(final String phone, final String code, SaDataProccessHandler<Void, Void, DiUserInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_USER_UPDATEUSERPHONE);
        SaAsyncTask<Void, Void, DiUserInfo> task = new SaAsyncTask<Void, Void, DiUserInfo>(handler) {
            @Override
            protected DiUserInfo doInBackground(Void... params) {
                DiUserInfo result = null;
                try {
                    BaseHttpResponseHandler<DiUserInfo> dataHandler = new
                            BaseHttpResponseHandler<DiUserInfo>() {

                                @Override
                                public DiUserInfo getResult(Object jsonObject) throws SaException {
                                    DiUserInfo resultData = ParseJsonToObject.getObject(DiUserInfo.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("phone", phone);
                        requestParameter.put("smscode", code);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }

                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return result;
            }
        };
        task.execute();
        return task;
    }

    public SaAsyncTask<Void, Void, DiUserInfo> reSetPassWord(final String phone, final String code, final String password, SaDataProccessHandler<Void, Void, DiUserInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_USER_RESETPASSWORD);
        SaAsyncTask<Void, Void, DiUserInfo> task = new SaAsyncTask<Void, Void, DiUserInfo>(handler) {
            @Override
            protected DiUserInfo doInBackground(Void... params) {
                DiUserInfo result = null;
                try {
                    BaseHttpResponseHandler<DiUserInfo> dataHandler = new BaseHttpResponseHandler<DiUserInfo>() {

                                @Override
                                public DiUserInfo getResult(Object jsonObject) throws SaException {
                                    DiUserInfo resultData = ParseJsonToObject.getObject(DiUserInfo.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("phone", phone);
                        requestParameter.put("smscode", code);
                        requestParameter.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }

                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
                } catch (SaException e) {
                    setErrorObj(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorObj(new SaException(SaError.ERROR_TYPE_UNKNOWN, e));
                }
                return result;
            }
        };
        task.execute();
        return task;
    }

}
