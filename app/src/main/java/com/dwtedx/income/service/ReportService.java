package com.dwtedx.income.service;

import android.annotation.SuppressLint;

import com.dwtedx.income.alipay.AlipayModel;
import com.dwtedx.income.connect.BaseHttpResponseHandler;
import com.dwtedx.income.connect.SaAsyncTask;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaError;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiUserinviteinfo;
import com.dwtedx.income.entity.IdInfo;
import com.dwtedx.income.entity.UserVipInfo;
import com.dwtedx.income.utility.ParseJsonToObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@SuppressWarnings({"JavaDoc", "UnusedReturnValue", "UnnecessaryLocalVariable"})
@SuppressLint("StaticFieldLeak")
public class ReportService {
    private static ReportService mService = null;

    private ReportService() {
    }

    public static ReportService getInstance() {
        if (mService == null)
            mService = new ReportService();
        return mService;
    }

    public SaAsyncTask<Void, Void, Void> saveReport(final int id, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_REPORT_SAVE);
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
                        requestParameter.put("reportid", id);
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

}
