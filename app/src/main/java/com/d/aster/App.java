package com.d.aster;

import android.app.Application;
import android.os.Environment;

import com.d.aster.api.API;
import com.d.lib.aster.Aster;
import com.d.lib.aster.base.Config;
import com.d.lib.aster.integration.okhttp3.interceptor.HeadersInterceptor;
import com.d.lib.aster.utils.SSLUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * App
 * Created by D on 2017/10/27.
 */
public class App extends Application {
    public final static String mPath = Environment.getExternalStorageDirectory().getPath() + "/aster/test/";
    public final static String mName = "1.jpg";

    @Override
    public void onCreate() {
        super.onCreate();
        Map<String, String> headers = new HashMap<>();
        headers.put(API.CommonHeader.platform, "Android");
        headers.put(API.CommonHeader.appVersion, "v1.0.0");

        Aster.init()
                .baseUrl(API.API_BASE)
                .headers(headers)
                .headers(new HeadersInterceptor.OnHeadInterceptor() {
                    @Override
                    public void intercept(Map<String, String> heads) {
                        // Add a dynamic request header such as token
                        heads.put("token", "008");
                    }
                })
                .connectTimeout(10 * 1000)
                .readTimeout(10 * 1000)
                .writeTimeout(10 * 1000)
                .retryCount(3)
                .retryDelayMillis(2 * 1000)
                .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null))
                .setLog("RetrofitLog Back = ", Config.Level.BODY)
                .setDebug(true)
                .build();
    }
}
