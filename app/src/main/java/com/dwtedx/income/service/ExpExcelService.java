package com.dwtedx.income.service;

import android.annotation.SuppressLint;

import com.dwtedx.income.connect.BaseHttpResponseHandler;
import com.dwtedx.income.connect.SaAsyncTask;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaError;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiExpexcel;
import com.dwtedx.income.entity.DiTopic;
import com.dwtedx.income.entity.DiTopicimg;
import com.dwtedx.income.entity.DiTopictalk;
import com.dwtedx.income.entity.DiTopicvote;
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

}
