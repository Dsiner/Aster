package com.d.rxnet;

import android.app.Application;

import com.d.lib.rxnet.RxNet;
import com.d.lib.rxnet.utils.SSLUtil;
import com.d.rxnet.api.API;

import java.util.HashMap;
import java.util.Map;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * App
 * Created by D on 2017/10/27.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initRxNet();
    }

    private void initRxNet() {
        Map<String, String> headers = new HashMap<>();
        headers.put(API.CommonHeader.platform, "Android");
        headers.put(API.CommonHeader.app_version, "v1.0.0");

        RxNet.init()
                .baseUrl(API.API_BASE)
                /**
                 * Add a dynamic request header method such as token
                 * 1: Override HeadersInterceptor.intercept()
                 * 2: addInterceptor()
                 */
                .headers(headers)
                .connectTimeout(10 * 1000)
                .readTimeout(10 * 1000)
                .writeTimeout(10 * 1000)
                .retryCount(3)
                .retryDelayMillis(2 * 1000)
                .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null))
                .setLog("RetrofitLog Back = ", HttpLoggingInterceptor.Level.BODY)
                .setDebug(true)
                .build();
    }
}
