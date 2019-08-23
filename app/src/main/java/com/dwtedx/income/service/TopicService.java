package com.dwtedx.income.service;

import android.annotation.SuppressLint;

import com.dwtedx.income.connect.BaseHttpResponseHandler;
import com.dwtedx.income.connect.SaAsyncTask;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaError;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.DiTopic;
import com.dwtedx.income.utility.ParseJsonToObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@SuppressWarnings({"JavaDoc", "UnusedReturnValue", "UnnecessaryLocalVariable"})
@SuppressLint("StaticFieldLeak")
public class TopicService {
    private static TopicService mService = null;

    private TopicService() {
    }

    public static TopicService getInstance() {
        if (mService == null)
            mService = new TopicService();
        return mService;
    }

    /**
     * 数据获取
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<DiTopic>> getTopicinfo(final int start, final int length, SaDataProccessHandler<Void, Void, List<DiTopic>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TOPIC_INDEX);
        SaAsyncTask<Void, Void, List<DiTopic>> task = new SaAsyncTask<Void, Void, List<DiTopic>>(handler) {
            @Override
            protected List<DiTopic> doInBackground(Void... params) {
                List<DiTopic> result = null;
                try {
                    BaseHttpResponseHandler<List<DiTopic>> dataHandler = new
                            BaseHttpResponseHandler<List<DiTopic>>() {

                                @Override
                                public List<DiTopic> getResult(Object jsonObject) throws SaException {
                                    List<DiTopic> resultData = ParseJsonToObject.getObjectList(DiTopic.class, jsonObject);
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
