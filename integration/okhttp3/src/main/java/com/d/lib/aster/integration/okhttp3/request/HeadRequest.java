package com.d.lib.aster.integration.okhttp3.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.integration.okhttp3.OkHttpApi;

/**
 * Created by D on 2017/10/24.
 */
public class HeadRequest extends HttpRequest<HeadRequest> {

    public HeadRequest(String url) {
        super(url);
    }

    public HeadRequest(String url, Config config) {
        super(url, null, config);
    }

    @Override
    protected void prepare() {
        final OkHttpApi.Callable callable = getClient().create().head(mUrl);
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

        @Override
        protected void prepare() {
            final OkHttpApi.Callable callable = getClient().create().head(mUrl);
            mCall = callable.call;
            mObservable = callable.observable;
        }
    }
}
