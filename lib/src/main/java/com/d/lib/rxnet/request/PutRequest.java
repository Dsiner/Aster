package com.d.lib.rxnet.request;

import com.d.lib.rxnet.api.RetrofitAPI;
import com.d.lib.rxnet.base.RetrofitClient;

import java.util.Map;

/**
 * Singleton
 * Created by D on 2017/10/24.
 */
public class PutRequest extends HttpRequest<PutRequest> {

    public PutRequest(String url) {
        super(url);
    }

    public PutRequest(String url, Map<String, String> params) {
        super(url, params);
    }

    @Override
    protected void init() {
        observable = RetrofitClient.getIns().create(RetrofitAPI.class).put(url, params);
    }

    /**
     * New instance
     */
    public static class PutRequestF extends HttpRequestF<PutRequestF> {

        public PutRequestF(String url) {
            super(url);
        }

        public PutRequestF(String url, Map<String, String> params) {
            super(url, params);
        }

        @Override
        protected void init() {
            observable = RetrofitClient.getRetrofit(config).create(RetrofitAPI.class).put(url, params);
        }
    }
}
