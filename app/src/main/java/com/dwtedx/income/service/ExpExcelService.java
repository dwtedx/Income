package com.dwtedx.income.service;

import android.annotation.SuppressLint;

import com.dwtedx.income.connect.BaseHttpResponseHandler;
import com.dwtedx.income.connect.SaAsyncTask;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaError;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.DiExpexcel;
import com.dwtedx.income.utility.ParseJsonToObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@SuppressWarnings({"JavaDoc", "UnusedReturnValue", "UnnecessaryLocalVariable"})
@SuppressLint("StaticFieldLeak")
public class ExpExcelService {
    private static ExpExcelService mService = null;

    private ExpExcelService() {
    }

    public static ExpExcelService getInstance() {
        if (mService == null)
            mService = new ExpExcelService();
        return mService;
    }

    /**
     * 保存
     * @param expexcel
     * @param handler
     */
    public SaAsyncTask<Void, Void, Integer> poolExpExcel(final DiExpexcel expexcel, SaDataProccessHandler<Void, Void, Integer> handler) {
        handler.setUrl(ServiceAPI.WEB_API_EXPEXCEL_POOL);
        SaAsyncTask<Void, Void, Integer> task = new SaAsyncTask<Void, Void, Integer>(handler) {
            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    BaseHttpResponseHandler<Integer> dataHandler = new BaseHttpResponseHandler<Integer>() {

                        @Override
                        public Integer getResult(Object jsonObject) throws SaException {
                            int result = 0;
                            try {
                                JSONObject object = (JSONObject) jsonObject;
                                result =  object.getInt("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return result;
                        }
                    };
                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(expexcel);
                    return handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(requestParameter), dataHandler);
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

    /**
     * 保存
     * @param expexcel
     * @param handler
     */
    public SaAsyncTask<Void, Void, Void> seveExpExcel(final DiExpexcel expexcel, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_EXPEXCEL_SAVE);
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
                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(expexcel);
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

    /**
     * 数据获取
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, DiExpexcel> getMyLastExpExcelInfo(SaDataProccessHandler<Void, Void, DiExpexcel> handler) {
        handler.setUrl(ServiceAPI.WEB_API_EXPEXCEL_FINDLAST);
        SaAsyncTask<Void, Void, DiExpexcel> task = new SaAsyncTask<Void, Void, DiExpexcel>(handler) {
            @Override
            protected DiExpexcel doInBackground(Void... params) {
                DiExpexcel result = null;
                try {
                    BaseHttpResponseHandler<DiExpexcel> dataHandler = new BaseHttpResponseHandler<DiExpexcel>() {
                                @Override
                                public DiExpexcel getResult(Object jsonObject) throws SaException {
                                    DiExpexcel resultData = ParseJsonToObject.getObject(DiExpexcel.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };
                    result = handler.getRequestExecutor().executeRequest(ServiceParamterUtil.genParamterJSONObject(new JSONObject()), dataHandler);
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
     * 数据获取
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<DiExpexcel>> getMyExpExcelInfo(final int start, final int length, SaDataProccessHandler<Void, Void, List<DiExpexcel>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_EXPEXCEL_FINDLIST);
        SaAsyncTask<Void, Void, List<DiExpexcel>> task = new SaAsyncTask<Void, Void, List<DiExpexcel>>(handler) {
            @Override
            protected List<DiExpexcel> doInBackground(Void... params) {
                List<DiExpexcel> result = null;
                try {
                    BaseHttpResponseHandler<List<DiExpexcel>> dataHandler = new
                            BaseHttpResponseHandler<List<DiExpexcel>>() {

                                @Override
                                public List<DiExpexcel> getResult(Object jsonObject) throws SaException {
                                    List<DiExpexcel> resultData = ParseJsonToObject.getObjectList(DiExpexcel.class, jsonObject);
                                    return resultData;
                                }
                            };
                    JSONObject requestParameter = new JSONObject();
                    try {
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

}
