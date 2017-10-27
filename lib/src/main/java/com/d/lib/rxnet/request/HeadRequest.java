package com.d.lib.rxnet.request;

import android.content.Context;

import com.d.lib.rxnet.api.RetrofitAPI;
import com.d.lib.rxnet.base.RetrofitClient;

import java.util.Map;

/**
 * Instance
 * Created by D on 2017/10/24.
 */
public class HeadRequest extends HttpRequest<HeadRequest> {
    protected Map<String, String> params;

    public HeadRequest(Context context, String url) {
        super(context, url);
    }

    public HeadRequest(Context context, String url, Map<String, String> params) {
        super(context, url, params);
    }

    @Override
    protected void init() {
        observable = RetrofitClient.getInstance(context).create(RetrofitAPI.class).head(url, params);
    }

    /**
     * New
     */
    public static class HeadRequestF extends HttpRequestF<HeadRequestF> {

        public HeadRequestF(Context context, String url) {
            super(context, url);
        }

        public HeadRequestF(Context context, String url, Map<String, String> params) {
            super(context, url, params);
        }

        @Override
        protected void init() {
            observable = RetrofitClient.getRetrofit(context, config).create(RetrofitAPI.class).head(url, params);
        }
    }
}
