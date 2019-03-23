package com.d.lib.aster.integration.volley.request;

import com.d.lib.aster.base.Config;

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
        mObservable = getClient().create().head(mUrl);
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
            mObservable = getClient().create().head(mUrl);
        }
    }
}
