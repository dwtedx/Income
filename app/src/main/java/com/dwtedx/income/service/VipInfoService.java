package com.dwtedx.income.service;

import android.annotation.SuppressLint;

import com.dwtedx.income.alipay.AlipayModel;
import com.dwtedx.income.connect.BaseHttpResponseHandler;
import com.dwtedx.income.connect.SaAsyncTask;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaError;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiExpexcel;
import com.dwtedx.income.entity.DiUserinviteinfo;
import com.dwtedx.income.entity.IdInfo;
import com.dwtedx.income.entity.UserVipInfo;
import com.dwtedx.income.utility.ParseJsonToObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@SuppressWarnings({"JavaDoc", "UnusedReturnValue", "UnnecessaryLocalVariable"})
@SuppressLint("StaticFieldLeak")
public class VipInfoService {
    private static VipInfoService mService = null;

    private VipInfoService() {
    }

    public static VipInfoService getInstance() {
        if (mService == null)
            mService = new VipInfoService();
        return mService;
    }

    /**
     * 数据获取
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, AlipayModel> getVipOrderInfo(UserVipInfo userVipInfo,SaDataProccessHandler<Void, Void, AlipayModel> handler) {
        handler.setUrl(ServiceAPI.WEB_API_VIPINFO_ORDER);
        SaAsyncTask<Void, Void, AlipayModel> task = new SaAsyncTask<Void, Void, AlipayModel>(handler) {
            @Override
            protected AlipayModel doInBackground(Void... params) {
                AlipayModel result = null;
                try {
                    BaseHttpResponseHandler<AlipayModel> dataHandler = new BaseHttpResponseHandler<AlipayModel>() {
                                @Override
                                public AlipayModel getResult(Object jsonObject) throws SaException {
                                    AlipayModel resultData = ParseJsonToObject.getObject(AlipayModel.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };
                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(userVipInfo);
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
     * 获取VIP数量
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, IdInfo> getVipOpenCount(SaDataProccessHandler<Void, Void, IdInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_VIPINFO_VIPCOUNT);
        SaAsyncTask<Void, Void, IdInfo> task = new SaAsyncTask<Void, Void, IdInfo>(handler) {
            @Override
            protected IdInfo doInBackground(Void... params) {
                IdInfo result = null;
                try {
                    BaseHttpResponseHandler<IdInfo> dataHandler = new BaseHttpResponseHandler<IdInfo>() {
                                @Override
                                public IdInfo getResult(Object jsonObject) throws SaException {
                                    IdInfo resultData = ParseJsonToObject.getObject(IdInfo.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };
                    JSONObject requestParameter = new JSONObject();
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

    public SaAsyncTask<Void, Void, Void> saveInvite(final String phone, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_VIPINVITE_SAVE);
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
                        requestParameter.put("invitephone", phone);
                        requestParameter.put("userid", ApplicationData.mDiUserInfo.getId());
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

    public SaAsyncTask<Void, Void, List<DiUserinviteinfo>> getUserInvite(int status, SaDataProccessHandler<Void, Void, List<DiUserinviteinfo>> handler) {
        handler.setUrl(ServiceAPI.WEB_API_VIPINVITE_FIND);
        SaAsyncTask<Void, Void, List<DiUserinviteinfo>> task = new SaAsyncTask<Void, Void, List<DiUserinviteinfo>>(handler) {
            @Override
            protected List<DiUserinviteinfo> doInBackground(Void... params) {
                List<DiUserinviteinfo> result = null;
                try {
                    BaseHttpResponseHandler<List<DiUserinviteinfo>> dataHandler = new BaseHttpResponseHandler<List<DiUserinviteinfo>>() {
                        @Override
                        public List<DiUserinviteinfo> getResult(Object jsonObject) throws SaException {
                            List<DiUserinviteinfo> resultData = ParseJsonToObject.getObjectList(DiUserinviteinfo.class, jsonObject);
                            return resultData;
                        }
                    };
                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("id", status);
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
