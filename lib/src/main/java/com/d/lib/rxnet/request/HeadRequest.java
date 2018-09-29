package com.d.lib.rxnet.request;

import com.d.lib.rxnet.api.RetrofitAPI;
import com.d.lib.rxnet.base.RetrofitClient;

import java.util.Map;

/**
 * Singleton
 * Created by D on 2017/10/24.
 */
public class HeadRequest extends HttpRequest<HeadRequest> {
    protected Map<String, String> params;

    public HeadRequest(String url) {
        super(url);
    }

    public HeadRequest(String url, Map<String, String> params) {
        super(url, params);
    }

    @Override
    protected void init() {
        observable = RetrofitClient.getIns().create(RetrofitAPI.class).head(url, params);
    }

    /**
     * New instance
     */
    public static class HeadRequestF extends HttpRequestF<HeadRequestF> {

        public HeadRequestF(String url) {
            super(url);
        }

        public HeadRequestF(String url, Map<String, String> params) {
            super(url, params);
        }

        @Override
        protected void init() {
            observable = RetrofitClient.getRetrofit(config).create(RetrofitAPI.class).head(url, params);
        }
    }
}
