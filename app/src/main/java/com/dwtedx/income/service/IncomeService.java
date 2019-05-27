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
import com.dwtedx.income.entity.DiParacontent;
import com.dwtedx.income.entity.DiScan;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.entity.IdInfo;
import com.dwtedx.income.utility.ParseJsonToObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@SuppressWarnings({"JavaDoc", "UnusedReturnValue", "UnnecessaryLocalVariable"})
@SuppressLint("StaticFieldLeak")
public class IncomeService {
    private static IncomeService mService = null;

    private IncomeService() {
    }

    public static IncomeService getInstance() {
        if (mService == null)
            mService = new IncomeService();
        return mService;
    }

    /**
     * incomeCynchronize
     *
     * @param mDiIncomeList
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<DiIncome>> incomeCynchronize(final List<DiIncome> mDiIncomeList, SaDataProccessHandler<Void, Void, List<DiIncome>> handler) {
        handler.setUrl(ServiceAPI.WEB_API_INCOME_SYNCHRONIZEID);
        SaAsyncTask<Void, Void, List<DiIncome>> task = new SaAsyncTask<Void, Void, List<DiIncome>>(handler) {
            @Override
            protected List<DiIncome> doInBackground(Void... params) {
                List<DiIncome> result = null;
                try {
                    BaseHttpResponseHandler<List<DiIncome>> dataHandler = new BaseHttpResponseHandler<List<DiIncome>>() {

                                @Override
                                public List<DiIncome> getResult(Object jsonObject) throws SaException {
                                    List<DiIncome> resultData = ParseJsonToObject.getObjectList(DiIncome.class, jsonObject);
                                    return resultData;
                                }
                            };

                    JSONArray requestParameter = ParseJsonToObject.getJsonFromObjList(mDiIncomeList);
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
     * incomeCynchronizeSingle
     *
     * @param mDiIncome
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, Void> incomeCynchronizeSingle(final DiIncome mDiIncome, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_INCOME_SYNCHRONIZE_SINGLE);
        SaAsyncTask<Void, Void, Void> task = new SaAsyncTask<Void, Void, Void>(handler) {
            @Override
            protected Void doInBackground(Void... params) {
                Void result = null;
                try {
                    BaseHttpResponseHandler<Void> dataHandler = new
                            BaseHttpResponseHandler<Void>() {

                                @Override
                                public Void getResult(Object jsonObject) throws SaException {
                                    Void resultData = ParseJsonToObject.getObject(Void.class,
                                            (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(mDiIncome);
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
     * incomeCynchronizeSingle
     *
     * @param mDiIncome
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, Void> incomeCynchronizeSingleByServerId(final DiIncome mDiIncome, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_INCOME_SYNCHRONIZE_SINGLEID);
        SaAsyncTask<Void, Void, Void> task = new SaAsyncTask<Void, Void, Void>(handler) {
            @Override
            protected Void doInBackground(Void... params) {
                Void result = null;
                try {
                    BaseHttpResponseHandler<Void> dataHandler = new BaseHttpResponseHandler<Void>() {

                                @Override
                                public Void getResult(Object jsonObject) throws SaException {
                                    Void resultData = ParseJsonToObject.getObject(Void.class,
                                            (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(mDiIncome);
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
     * 数据恢复
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<DiIncome>> incomeCynchronizeSingleList(final int start, final int length, SaDataProccessHandler<Void, Void, List<DiIncome>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_INCOME_SYNCHRONIZE_EGAIN);
        SaAsyncTask<Void, Void, List<DiIncome>> task = new SaAsyncTask<Void, Void, List<DiIncome>>(handler) {
            @Override
            protected List<DiIncome> doInBackground(Void... params) {
                List<DiIncome> result = null;
                try {
                    BaseHttpResponseHandler<List<DiIncome>> dataHandler = new
                            BaseHttpResponseHandler<List<DiIncome>>() {

                                @Override
                                public List<DiIncome> getResult(Object jsonObject) throws SaException {
                                    List<DiIncome> resultData = ParseJsonToObject.getObjectList(DiIncome.class, jsonObject);
                                    return resultData;
                                }
                            };
                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("id", ApplicationData.mDiUserInfo.getId());
                        requestParameter.put("start", start);
                        requestParameter.put("length", length);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }
                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.
                            genParamterJSONObject(requestParameter), dataHandler);
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
     * 类型列表
     *
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<DiType>> typeCynchronizeSingleList(SaDataProccessHandler<Void, Void, List<DiType>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TYPE_GETTYPE);
        SaAsyncTask<Void, Void, List<DiType>> task = new SaAsyncTask<Void, Void, List<DiType>>(handler) {
            @Override
            protected List<DiType> doInBackground(Void... params) {
                List<DiType> result = null;
                try {
                    BaseHttpResponseHandler<List<DiType>> dataHandler = new
                            BaseHttpResponseHandler<List<DiType>>() {

                                @Override
                                public List<DiType> getResult(Object jsonObject) throws SaException {
                                    List<DiType> resultData = ParseJsonToObject.getObjectList(DiType.class, jsonObject);
                                    return resultData;
                                }
                            };
                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("id", ApplicationData.mDiUserInfo.getId());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }
                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.
                            genParamterJSONObject(requestParameter), dataHandler);
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
     * 账户列表
     *
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<DiAccount>> accountCynchronizeSingleList(SaDataProccessHandler<Void, Void, List<DiAccount>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_ACCOUNT_GETACCOUNT);
        SaAsyncTask<Void, Void, List<DiAccount>> task = new SaAsyncTask<Void, Void, List<DiAccount>>(handler) {
            @Override
            protected List<DiAccount> doInBackground(Void... params) {
                List<DiAccount> result = null;
                try {
                    BaseHttpResponseHandler<List<DiAccount>> dataHandler = new
                            BaseHttpResponseHandler<List<DiAccount>>() {

                                @Override
                                public List<DiAccount> getResult(Object jsonObject) throws SaException {
                                    List<DiAccount> resultData = ParseJsonToObject.getObjectList(DiAccount.class, jsonObject);
                                    return resultData;
                                }
                            };
                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("id", ApplicationData.mDiUserInfo.getId());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new SaException(SaError.ERROR_TYPE_JSON, e);
                    }
                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.
                            genParamterJSONObject(requestParameter), dataHandler);
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
     * incomeCynchronizeSingle
     *
     * @param mDiType
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, IdInfo> addType(final DiType mDiType, SaDataProccessHandler<Void, Void, IdInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_TYPE_ADDTYPE);
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

                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(mDiType);
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
     * updateType
     *
     * @param name
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, Void> updateType(final String name, final String icon, final String color, final String remark, final int serverid, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_TYPE_UPDATETYPE);
        SaAsyncTask<Void, Void, Void> task = new SaAsyncTask<Void, Void, Void>(handler) {
            @Override
            protected Void doInBackground(Void... params) {
                Void result = null;
                try {
                    BaseHttpResponseHandler<Void> dataHandler = new
                            BaseHttpResponseHandler<Void>() {

                                @Override
                                public Void getResult(Object jsonObject) throws SaException {
                                    Void resultData = ParseJsonToObject.getObject(Void.class,
                                            (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("serverid", serverid);
                        requestParameter.put("name", name);
                        requestParameter.put("icon", icon);
                        requestParameter.put("color", color);
                        requestParameter.put("remark", remark);

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
     * deleteType
     *
     * @param id
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, Void> deleteType(final int id, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_TYPE_DELETETYPE);
        SaAsyncTask<Void, Void, Void> task = new SaAsyncTask<Void, Void, Void>(handler) {
            @Override
            protected Void doInBackground(Void... params) {
                Void result = null;
                try {
                    BaseHttpResponseHandler<Void> dataHandler = new
                            BaseHttpResponseHandler<Void>() {

                                @Override
                                public Void getResult(Object jsonObject) throws SaException {
                                    Void resultData = ParseJsonToObject.getObject(Void.class,
                                            (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("id", id);

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
     * addAccount
     *
     * @param mDiAccount
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, IdInfo> addAccount(final DiAccount mDiAccount, SaDataProccessHandler<Void, Void, IdInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_ACCOUNT_ADDACCOUNT);
        SaAsyncTask<Void, Void, IdInfo> task = new SaAsyncTask<Void, Void, IdInfo>(handler) {
            @Override
            protected IdInfo doInBackground(Void... params) {
                IdInfo result = null;
                try {
                    BaseHttpResponseHandler<IdInfo> dataHandler = new
                            BaseHttpResponseHandler<IdInfo>() {

                                @Override
                                public IdInfo getResult(Object jsonObject) throws SaException {
                                    IdInfo resultData = ParseJsonToObject.getObject(IdInfo.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(mDiAccount);
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
     * deleteAccount
     *
     * @param id
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, Void> deleteAccount(final int id, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_ACCOUNT_DELETEACCOUNT);
        SaAsyncTask<Void, Void, Void> task = new SaAsyncTask<Void, Void, Void>(handler) {
            @Override
            protected Void doInBackground(Void... params) {
                Void result = null;
                try {
                    BaseHttpResponseHandler<Void> dataHandler = new
                            BaseHttpResponseHandler<Void>() {

                                @Override
                                public Void getResult(Object jsonObject) throws SaException {
                                    Void resultData = ParseJsonToObject.getObject(Void.class,
                                            (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("id", id);

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
     * deleteIncome
     *
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, Void> deleteIncome(final int clientid, final int userid, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_INCOME_DELETEINCOME);
        SaAsyncTask<Void, Void, Void> task = new SaAsyncTask<Void, Void, Void>(handler) {
            @Override
            protected Void doInBackground(Void... params) {
                Void result = null;
                try {
                    BaseHttpResponseHandler<Void> dataHandler = new
                            BaseHttpResponseHandler<Void>() {

                                @Override
                                public Void getResult(Object jsonObject) throws SaException {
                                    Void resultData = ParseJsonToObject.getObject(Void.class,
                                            (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("clientid", clientid);
                        requestParameter.put("userid", userid);

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
     * deleteIncome
     *
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, Void> deleteIncomeByServerId(final DiIncome income, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_INCOME_DELETEINCOMEID);
        SaAsyncTask<Void, Void, Void> task = new SaAsyncTask<Void, Void, Void>(handler) {
            @Override
            protected Void doInBackground(Void... params) {
                Void result = null;
                try {
                    BaseHttpResponseHandler<Void> dataHandler = new
                            BaseHttpResponseHandler<Void>() {

                                @Override
                                public Void getResult(Object jsonObject) throws SaException {
                                    Void resultData = ParseJsonToObject.getObject(Void.class,
                                            (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(income);
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
     * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/9/16 11:20.
     * Company 路之遥网络科技有限公司
     * Description 预算同步
     */
    public SaAsyncTask<Void, Void, Void> budgetCynchronize(final List<DiBudget> mDiBudgetList, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_BUDGET_SYNCHRONIZE);
        SaAsyncTask<Void, Void, Void> task = new SaAsyncTask<Void, Void, Void>(handler) {
            @Override
            protected Void doInBackground(Void... params) {
                Void result = null;
                try {
                    BaseHttpResponseHandler<Void> dataHandler = new
                            BaseHttpResponseHandler<Void>() {

                                @Override
                                public Void getResult(Object jsonObject) throws SaException {
                                    Void resultData = ParseJsonToObject.getObject(Void.class,
                                            (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONArray requestParameter = ParseJsonToObject.getJsonFromObjList(mDiBudgetList);
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

    public SaAsyncTask<Void, Void, Void> budgetCynchronizeSingle(final DiBudget iBudgetm, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_BUDGET_SYNCHRONIZESINGLE);
        SaAsyncTask<Void, Void, Void> task = new SaAsyncTask<Void, Void, Void>(handler) {
            @Override
            protected Void doInBackground(Void... params) {
                Void result = null;
                try {
                    BaseHttpResponseHandler<Void> dataHandler = new
                            BaseHttpResponseHandler<Void>() {

                                @Override
                                public Void getResult(Object jsonObject) throws SaException {
                                    Void resultData = ParseJsonToObject.getObject(Void.class,
                                            (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(iBudgetm);
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
     * 删除单个扫单
     *
     * @param diScan
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, Void> scanDeleteByServerId(final DiScan diScan, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_SCAN_DELETESCAN);
        SaAsyncTask<Void, Void, Void> task = new SaAsyncTask<Void, Void, Void>(handler) {
            @Override
            protected Void doInBackground(Void... params) {
                Void result = null;
                try {
                    BaseHttpResponseHandler<Void> dataHandler = new BaseHttpResponseHandler<Void>() {

                        @Override
                        public Void getResult(Object jsonObject) throws SaException {
                            Void resultData = ParseJsonToObject.getObject(Void.class, (JSONObject) jsonObject);
                            return resultData;
                        }
                    };

                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(diScan);
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
     * saveIncomeScanSingleByServerId
     *
     * @param diIncome
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, DiIncome> saveIncomeScanSingleByServerId(final DiIncome diIncome, SaDataProccessHandler<Void, Void, DiIncome> handler) {
        handler.setUrl(ServiceAPI.WEB_API_INCOME_SAVEINCOMESCANSINGLEID);
        SaAsyncTask<Void, Void, DiIncome> task = new SaAsyncTask<Void, Void, DiIncome>(handler) {
            @Override
            protected DiIncome doInBackground(Void... params) {
                DiIncome result = null;
                try {
                    BaseHttpResponseHandler<DiIncome> dataHandler = new BaseHttpResponseHandler<DiIncome>() {

                        @Override
                        public DiIncome getResult(Object jsonObject) throws SaException {
                            DiIncome resultData = ParseJsonToObject.getObject(DiIncome.class, (JSONObject) jsonObject);
                            return resultData;
                        }
                    };

                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(diIncome);
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
     * 扫描小票商品名排除库
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<DiParacontent>> getScanPara(SaDataProccessHandler<Void, Void, List<DiParacontent>> handler) {
        handler.setUrl(ServiceAPI.WEB_API_PARA_GETSCAN);
        SaAsyncTask<Void, Void, List<DiParacontent>> task = new SaAsyncTask<Void, Void, List<DiParacontent>>(handler) {
            @Override
            protected List<DiParacontent> doInBackground(Void... params) {
                List<DiParacontent> result = null;
                try {
                    BaseHttpResponseHandler<List<DiParacontent>> dataHandler = new
                            BaseHttpResponseHandler<List<DiParacontent>>() {
                                @Override
                                public List<DiParacontent> getResult(Object jsonObject) throws SaException {
                                    List<DiParacontent> resultData = ParseJsonToObject.getObjectList(DiParacontent.class, jsonObject);
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

}
