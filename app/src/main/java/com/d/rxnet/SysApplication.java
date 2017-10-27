package com.d.rxnet;

import android.app.Application;

import com.d.lib.rxnet.RxNet;
import com.d.lib.rxnet.api.API;
import com.d.lib.rxnet.util.SSLUtil;

/**
 * e.g
 * Created by D on 2017/10/27.
 */
public class SysApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RxNet.init(getApplicationContext())
                .baseUrl(API.API_BASE)
                .headers(null)
                .connectTimeout(10 * 1000)
                .readTimeout(10 * 1000)
                .readTimeout(10 * 1000)
                .retryCount(3)
                .retryDelayMillis(2 * 1000)
                .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null))
                .build();
    }
}
