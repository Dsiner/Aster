package com.d.aster;

import android.app.Application;
import android.os.Environment;

import com.d.aster.api.API;
import com.d.lib.aster.Aster;
import com.d.lib.aster.base.Config;
import com.d.lib.aster.integration.okhttp3.interceptor.HeadersInterceptor;
import com.d.lib.aster.integration.retrofit.RetrofitModule;
import com.d.lib.aster.utils.SSLUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * App
 * Created by D on 2017/10/27.
 */
public class App extends Application {
    public final static String FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/Aster/test/";
    public final static String PIC_NAME = "1.jpg";

    @Override
    public void onCreate() {
        super.onCreate();

        Aster.init(getApplicationContext(),
                RetrofitModule.factory(getApplicationContext()));

//        init();
    }

    private void init() {
        Map<String, String> headers = new HashMap<>();
        headers.put(API.CommonHeader.platform, "Android");
        headers.put(API.CommonHeader.appVersion, "v1.0.0");

        Aster.init(getApplicationContext())
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
                .log("RetrofitLog Back = ", Config.Level.BODY)
                .debug(true)
                .build();
    }
}
