package com.d.lib.rxnet.request;

import android.content.Context;

import com.d.lib.rxnet.api.RetrofitAPI;
import com.d.lib.rxnet.base.RetrofitClient;

import java.util.Map;

/**
 * Instance
 * Created by D on 2017/10/24.
 */
public class GetRequest extends HttpRequest<GetRequest> {

    public GetRequest(Context context, String url) {
        super(context, url);
    }

    public GetRequest(Context context, String url, Map<String, String> params) {
        super(context, url, params);
    }

    @Override
    protected void init() {
        if (params == null) {
            observable = RetrofitClient.getInstance(context).create(RetrofitAPI.class).get(url);
        } else {
            observable = RetrofitClient.getInstance(context).create(RetrofitAPI.class).get(url, params);
        }
    }

    /**
     * New
     */
    public static class GetRequestF extends HttpRequestF<GetRequestF> {

        public GetRequestF(Context context, String url) {
            super(context, url);
        }

        public GetRequestF(Context context, String url, Map<String, String> params) {
            super(context, url, params);
        }

        @Override
        protected void init() {
            if (params == null) {
                observable = RetrofitClient.getRetrofit(context, config).create(RetrofitAPI.class).get(url);
            } else {
                observable = RetrofitClient.getRetrofit(context, config).create(RetrofitAPI.class).get(url, params);
            }
        }
    }
}
