package com.d.lib.rxnet.request;

import android.content.Context;

import com.d.lib.rxnet.api.RetrofitAPI;
import com.d.lib.rxnet.base.RetrofitClient;

import java.util.Map;

/**
 * Instance
 * Created by D on 2017/10/24.
 */
public class OptionRequest extends HttpRequest<OptionRequest> {

    public OptionRequest(Context context, String url) {
        super(context, url);
    }

    public OptionRequest(Context context, String url, Map<String, String> params) {
        super(context, url, params);
    }

    @Override
    protected void init() {
        observable = RetrofitClient.getInstance(context).create(RetrofitAPI.class).options(url, params);
    }

    /**
     * New
     */
    public static class OptionRequestF extends HttpRequestF<OptionRequestF> {

        public OptionRequestF(Context context, String url) {
            super(context, url);
        }

        public OptionRequestF(Context context, String url, Map<String, String> params) {
            super(context, url, params);
        }

        @Override
        protected void init() {
            observable = RetrofitClient.getRetrofit(context, config).create(RetrofitAPI.class).options(url, params);
        }
    }
}
