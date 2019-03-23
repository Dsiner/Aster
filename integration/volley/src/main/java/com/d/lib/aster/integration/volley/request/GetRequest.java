package com.d.lib.aster.integration.volley.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.Params;

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
        if (mParams != null && mParams.size() > 0) {
            mObservable = getClient().create().get(mUrl, mParams);
        } else {
            mObservable = getClient().create().get(mUrl);
        }
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
            if (mParams != null && mParams.size() > 0) {
                mObservable = getClient().create().get(mUrl, mParams);
            } else {
                mObservable = getClient().create().get(mUrl);
            }
        }
    }
}
