package com.dwtedx.income.service;

import android.annotation.SuppressLint;

import com.dwtedx.income.connect.BaseHttpResponseHandler;
import com.dwtedx.income.connect.SaAsyncTask;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaError;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.TaobaoActivityInfo;
import com.dwtedx.income.entity.TaobaoItemInfo;
import com.dwtedx.income.entity.TaobaoSearchItemInfo;
import com.dwtedx.income.entity.TbCategoryInfo;
import com.dwtedx.income.utility.ParseJsonToObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@SuppressWarnings({"JavaDoc", "UnusedReturnValue", "UnnecessaryLocalVariable"})
@SuppressLint("StaticFieldLeak")
public class TaobaoService {
    private static TaobaoService mService = null;

    private TaobaoService() {
    }

    public static TaobaoService getInstance() {
        if (mService == null)
            mService = new TaobaoService();
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
    public SaAsyncTask<Void, Void, List<TaobaoSearchItemInfo>> taobaoItemSearch(final String search, final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoSearchItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_SEARCH);
        SaAsyncTask<Void, Void, List<TaobaoSearchItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoSearchItemInfo>>(handler) {
            @Override
            protected List<TaobaoSearchItemInfo> doInBackground(Void... params) {
                List<TaobaoSearchItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoSearchItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoSearchItemInfo>>() {

                                @Override
                                public List<TaobaoSearchItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoSearchItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoSearchItemInfo.class, jsonObject);
                                    return resultData;
                                }
                            };
                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("search", search);
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
     * 数据获取 推荐
     *
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoSearchItemInfo>> taobaoItemRecommend(final String numid, SaDataProccessHandler<Void, Void, List<TaobaoSearchItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_RECOMMEND);
        SaAsyncTask<Void, Void, List<TaobaoSearchItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoSearchItemInfo>>(handler) {
            @Override
            protected List<TaobaoSearchItemInfo> doInBackground(Void... params) {
                List<TaobaoSearchItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoSearchItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoSearchItemInfo>>() {

                                @Override
                                public List<TaobaoSearchItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoSearchItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoSearchItemInfo.class, jsonObject);
                                    return resultData;
                                }
                            };
                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("numid", numid);

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
     * 数据获取 省钱精选
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> topTaobaoItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_TOPTAOBAOITEMINFO);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * 数据获取 省钱精选
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getWomensClothingItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_WOMENSCLOTHINGITEM);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * 数据获取 今天特价
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getCouponItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_COUPONITEM);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * 数据获取 男装
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getManClothingItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_MANCLOTHINGITEM);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * 数据获取 零食
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getSnacksItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_SNACKSITEM);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * 数据获取 美妆
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getMakeupsItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_MAKEUPSITEM);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * 数据获取 3C
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getDigitalItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_DIGITALITEM);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * 数据获取 内衣
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getUnderwearItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_UNDERWEARITEM);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * 数据获取 童装
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getChildrenItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_CHILDREN);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * 数据获取 getMotherBabyItem
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getMotherBabyItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_MOTHERBABYITEM);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * 数据获取 居家
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getLiveHomeItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_LIVEHOME);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * 数据获取 箱包
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getLuggageItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_LUGGAGEITEM);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * 数据获取 其它
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getOtherItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_OTHERITEM);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * 数据获取 9.9
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getNineNineItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_NINENINETAOBAOITEM);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * 数据获取 水机
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getRandItem(final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_RANDITEM);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
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

    /**
     * getTaobaoActivityInfo
     *
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, TaobaoActivityInfo> getTaobaoActivityInfo(SaDataProccessHandler<Void, Void, TaobaoActivityInfo> handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_TAOBAOACTIVITYINFO);
        SaAsyncTask<Void, Void, TaobaoActivityInfo> task = new SaAsyncTask<Void, Void, TaobaoActivityInfo>(handler) {
            @Override
            protected TaobaoActivityInfo doInBackground(Void... params) {
                TaobaoActivityInfo result = null;
                try {
                    BaseHttpResponseHandler<TaobaoActivityInfo> dataHandler = new
                            BaseHttpResponseHandler<TaobaoActivityInfo>() {

                                @Override
                                public TaobaoActivityInfo getResult(Object jsonObject) throws SaException {
                                    TaobaoActivityInfo resultData = ParseJsonToObject.getObject(TaobaoActivityInfo.class, (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };
                            JSONObject requestParameter = new JSONObject();
                            try {
                                requestParameter.put("search", "activity");
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
     * 数据获取 水机
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TbCategoryInfo>> getCategoryTop(SaDataProccessHandler<Void, Void, List<TbCategoryInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_CATEGORYTOP);
        SaAsyncTask<Void, Void, List<TbCategoryInfo>> task = new SaAsyncTask<Void, Void, List<TbCategoryInfo>>(handler) {
            @Override
            protected List<TbCategoryInfo> doInBackground(Void... params) {
                List<TbCategoryInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TbCategoryInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TbCategoryInfo>>() {

                                @Override
                                public List<TbCategoryInfo> getResult(Object jsonObject) throws SaException {
                                    List<TbCategoryInfo> resultData = ParseJsonToObject.getObjectList(TbCategoryInfo.class, jsonObject);
                                    return resultData;
                                }
                            };
                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("search", "categoryTop");

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
     * 数据获取 分类获取产品
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<TaobaoItemInfo>> getCategoryIdItem(final int categoryId, final int start, final int length, SaDataProccessHandler<Void, Void, List<TaobaoItemInfo>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_CATEGORYIDITEM);
        SaAsyncTask<Void, Void, List<TaobaoItemInfo>> task = new SaAsyncTask<Void, Void, List<TaobaoItemInfo>>(handler) {
            @Override
            protected List<TaobaoItemInfo> doInBackground(Void... params) {
                List<TaobaoItemInfo> result = null;
                try {
                    BaseHttpResponseHandler<List<TaobaoItemInfo>> dataHandler = new
                            BaseHttpResponseHandler<List<TaobaoItemInfo>>() {

                                @Override
                                public List<TaobaoItemInfo> getResult(Object jsonObject) throws SaException {
                                    List<TaobaoItemInfo> resultData = ParseJsonToObject.getObjectList(TaobaoItemInfo.class, jsonObject);
                                    return resultData;
                                }
                            };
                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("categoryId", categoryId);
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
     * 数据刷新 （测试用）
     *
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, Void> refreshTaobaoIteminfo(SaDataProccessHandler<Void, Void, Void>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TAOBAO_REFRESHTAOBAOITEMINFO);
        SaAsyncTask<Void, Void, Void> task = new SaAsyncTask<Void, Void, Void>(handler) {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    BaseHttpResponseHandler<Void> dataHandler = new
                            BaseHttpResponseHandler<Void>() {

                                @Override
                                public Void getResult(Object jsonObject) throws SaException {
                                    return null;
                                }
                            };
                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("search", "refresh");

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
