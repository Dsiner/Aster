package com.d.lib.aster.integration.okhttp3.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.integration.okhttp3.OkHttpApi;

/**
 * Created by D on 2017/10/24.
 */
public class GetRequest extends HttpRequest<GetRequest> {

    public GetRequest(String url) {
        super(url);
    }

    public GetRequest(String url, Params params) {
        super(url, params);
    }

    public GetRequest(String url, Params params, Config config) {
        super(url, params, config);
    }

    @Override
    protected void prepare() {
        final OkHttpApi.Callable callable;
        if (mParams != null && mParams.size() > 0) {
            callable = getClient().create().get(mUrl, mParams);
        } else {
            callable = getClient().create().get(mUrl);
        }
        mCall = callable.call;
        mObservable = callable.observable;
    }


    /**
     * Singleton
     */
    public static class Singleton extends HttpRequest.Singleton<Singleton> {

        public Singleton(String url) {
            super(url);
        }

        public Singleton(String url, Params params) {
            super(url, params);
        }

        @Override
        protected void prepare() {
            final OkHttpApi.Callable callable;
            if (mParams != null && mParams.size() > 0) {
                callable = getClient().create().get(mUrl, mParams);
            } else {
                callable = getClient().create().get(mUrl);
            }
            mCall = callable.call;
            mObservable = callable.observable;
        }
    }
}
