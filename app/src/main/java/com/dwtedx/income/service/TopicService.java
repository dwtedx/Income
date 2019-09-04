package com.dwtedx.income.service;

import android.annotation.SuppressLint;

import com.dwtedx.income.connect.BaseHttpResponseHandler;
import com.dwtedx.income.connect.SaAsyncTask;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaError;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
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

    /**
     * 数据获取
     *
     * @param start
     * @param length
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, List<DiTopic>> getMyTopicinfo(final int start, final int length, SaDataProccessHandler<Void, Void, List<DiTopic>>
            handler) {
        handler.setUrl(ServiceAPI.WEB_API_TOPIC_MYTOPIC);
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


    /**
     * 查找
     *
     * @param id
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, DiTopic> findTopic(final int id, SaDataProccessHandler<Void, Void, DiTopic> handler) {
        handler.setUrl(ServiceAPI.WEB_API_TOPIC_FINDTOPIC);
        SaAsyncTask<Void, Void, DiTopic> task = new SaAsyncTask<Void, Void, DiTopic>(handler) {
            @Override
            protected DiTopic doInBackground(Void... params) {
                DiTopic result = null;
                try {
                    BaseHttpResponseHandler<DiTopic> dataHandler = new
                            BaseHttpResponseHandler<DiTopic>() {

                                @Override
                                public DiTopic getResult(Object jsonObject) throws SaException {
                                    DiTopic resultData = ParseJsonToObject.getObject(DiTopic.class,
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
     * 保存投票
     * @param topicid
     * @param topicvoteid
     * @param userid
     * @param name
     * @param handler
     */
    public SaAsyncTask<Void, Void, List<DiTopicvote>> seveVoteResult(final int topicid, final int topicvoteid, final int userid, final String name, SaDataProccessHandler<Void, Void, List<DiTopicvote>> handler) {
        handler.setUrl(ServiceAPI.WEB_API_TOPIC_SEVEVOTERESULT);
        SaAsyncTask<Void, Void, List<DiTopicvote>> task = new SaAsyncTask<Void, Void, List<DiTopicvote>>(handler) {
            @Override
            protected List<DiTopicvote> doInBackground(Void... params) {
                List<DiTopicvote> result = null;
                try {
                    BaseHttpResponseHandler<List<DiTopicvote>> dataHandler = new
                            BaseHttpResponseHandler<List<DiTopicvote>>() {

                                @Override
                                public List<DiTopicvote> getResult(Object jsonObject) throws SaException {
                                    List<DiTopicvote> resultData = ParseJsonToObject.getObjectList(DiTopicvote.class, jsonObject);
                                    return resultData;
                                }
                            };
                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("topicid", topicid);
                        requestParameter.put("topicvoteid", topicvoteid);
                        requestParameter.put("userid", userid);
                        requestParameter.put("name", name);

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
     * 点赞
     * @param topicid
     * @param handler
     */
    public SaAsyncTask<Void, Void, Void> topicLicked(final int topicid, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_TOPIC_SEVETOPICLIKED);
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
                        requestParameter.put("id", topicid);
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

    /**
     * 上传图片
     *
     * @param imgdata
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, DiTopicimg> uploadImg(final String imgdata, SaDataProccessHandler<Void, Void, DiTopicimg> handler) {
        handler.setUrl(ServiceAPI.WEB_API_TOPIC_UPLOADIMG);
        SaAsyncTask<Void, Void, DiTopicimg> task = new SaAsyncTask<Void, Void, DiTopicimg>(handler) {
            @Override
            protected DiTopicimg doInBackground(Void... params) {
                DiTopicimg result = null;
                try {
                    BaseHttpResponseHandler<DiTopicimg> dataHandler = new
                            BaseHttpResponseHandler<DiTopicimg>() {

                                @Override
                                public DiTopicimg getResult(Object jsonObject) throws SaException {
                                    DiTopicimg resultData = ParseJsonToObject.getObject(DiTopicimg.class,
                                            (JSONObject) jsonObject);
                                    return resultData;
                                }
                            };

                    JSONObject requestParameter = new JSONObject();
                    try {
                        requestParameter.put("path", imgdata);
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
     * 保存
     * @param topic
     * @param handler
     */
    public SaAsyncTask<Void, Void, Void> seveTopic(final DiTopic topic, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_TOPIC_SEVETOPIC);
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
                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(topic);
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
     * 保存
     * @param topic
     * @param handler
     */
    public SaAsyncTask<Void, Void, Void> seveTopicTalk(final DiTopictalk topic, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_TOPIC_SEVETOPICTALK);
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
                    JSONObject requestParameter = ParseJsonToObject.getJsonFromObj(topic);
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
     * 保存分享
     * @param topicid
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, Void> topicShare(final int topicid, final int userid, String sharetype,SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_TOPIC_SEVETOPICSHARE);
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
                        requestParameter.put("id", topicid);
                        requestParameter.put("userid", userid);
                        requestParameter.put("sharetype", sharetype);
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


    /**
     * 删除
     * @param topicid
     * @param handler
     * @return
     */
    public SaAsyncTask<Void, Void, Void> deleteTopic(final int topicid, SaDataProccessHandler<Void, Void, Void> handler) {
        handler.setUrl(ServiceAPI.WEB_API_TOPIC_DELETETOPIC);
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
                        requestParameter.put("id", topicid);
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
