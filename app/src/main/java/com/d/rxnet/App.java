package com.d.rxnet;

import android.app.Application;
import android.os.Environment;

import com.d.lib.rxnet.RxNet;
import com.d.lib.rxnet.interceptor.HeadersInterceptor;
import com.d.lib.rxnet.utils.SSLUtil;
import com.d.rxnet.api.API;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * App
 * Created by D on 2017/10/27.
 */
public class App extends Application {
    public final static String mPath = Environment.getExternalStorageDirectory().getPath() + "/rxnet/test/";

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
                .headers(headers)
                .headers(new HeadersInterceptor.OnHeadInterceptor() {
                    @Override
                    public void intercept(Request.Builder builder) {
                        // Add a dynamic request header such as token
                        builder.addHeader("token", "008");
                    }
                })
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
