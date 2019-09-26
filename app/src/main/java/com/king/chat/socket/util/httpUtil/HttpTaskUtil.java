package com.king.chat.socket.util.httpUtil;


import android.os.Handler;
import android.os.UserManager;

import com.alibaba.fastjson.JSONObject;
import com.king.chat.socket.bean.base.BaseTaskBean;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by kongwei on 2017/3/10.
 */

public class HttpTaskUtil {

    private Map<String, Integer> reRequestMap = new HashMap();
    private ResultListener resultListener;

    private static HttpTaskUtil mInstance;

    public static String mToken = "";

    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 60;
    public final static int WRITE_TIMEOUT = 60;
    public final OkHttpClient client = new OkHttpClient();
    private Handler handler = new Handler();

    public static String getmToken() {
        return mToken;
    }

    public HttpTaskUtil() {
        client.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        client.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);//设置写的超时时间
        client.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
    }

    public static final HttpTaskUtil getInstance() {
        return HttpTaskUtilHolder.INSTANCE;
    }

    private static class HttpTaskUtilHolder {
        private static final HttpTaskUtil INSTANCE = new HttpTaskUtil();
    }

    public final MediaType JSONMediaType = MediaType.parse("application/json; charset=utf-8");

    public final ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    public void postJsonTask(final String url, final String json, final OkHttpClientManager.StringCallback callback) {
        executors.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final String response = postJson(url, json);
                    BaseTaskBean baseTaskBean = JSONObject.parseObject(response, BaseTaskBean.class);
                    if (baseTaskBean != null && baseTaskBean.getCode() == 200) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onResponse(response);
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure(null, new IOException(response));
                            }
                        });
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(null, e);
                        }
                    });
                }
            }
        });
    }

    private String postJson(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSONMediaType, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public interface ResultListener {
        public void onResponse(String response);

        public void onFailure(Request request, Exception e);
    }

    public HttpTaskUtil setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
        return this;
    }

    /**
     * HTTP GET
     */
    public void getTask(String url, final OkHttpClientManager.StringCallback callback) {
        try {
            OkHttpClientManager.getInstance()._getAsyn(url, callback);
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(null, new IOException(e != null ? e.getMessage() : "Server Error"));
            } else if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    /**
     * HTTP POST
     */
    public void postTask(String url, Map<String, String> params, final OkHttpClientManager.StringCallback callback) {
        try {
            OkHttpClientManager.getInstance().postAsyn(url, callback, params);
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(null, new IOException(e != null ? e.getMessage() : "Server Error"));
            } else if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    /**
     * HTTP POST
     */
    public void postUploadTask(String url, File file, String fileKey, final OkHttpClientManager.StringCallback callback) {
        try {
            OkHttpClientManager.getInstance()._postAsyn(url, callback, file, fileKey);
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(null, new IOException(e != null ? e.getMessage() : "Server Error"));
            } else if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

}
