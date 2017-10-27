package com.d.lib.rxnet.request;

import android.content.Context;

import com.d.lib.rxnet.api.RetrofitAPI;
import com.d.lib.rxnet.base.RetrofitClient;

import java.util.Map;

/**
 * Instance
 * Created by D on 2017/10/24.
 */
public class DeleteRequest extends HttpRequest<DeleteRequest> {

    public DeleteRequest(Context context, String url) {
        super(context, url);
    }

    public DeleteRequest(Context context, String url, Map<String, String> params) {
        super(context, url, params);
    }

    @Override
    protected void init() {
        observable = RetrofitClient.getInstance(context).create(RetrofitAPI.class).delete(url, params);
    }

    /**
     * New
     */
    public static class DeleteRequestF extends HttpRequestF<DeleteRequestF> {

        public DeleteRequestF(Context context, String url) {
            super(context, url);
        }

        public DeleteRequestF(Context context, String url, Map<String, String> params) {
            super(context, url, params);
        }

        @Override
        protected void init() {
            observable = RetrofitClient.getRetrofit(context, config).create(RetrofitAPI.class).delete(url, params);
        }
    }
}
