package com.d.lib.aster.integration.volley.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.integration.volley.body.RequestBody;

/**
 * Created by D on 2017/10/24.
 */
public class OptionRequest extends BodyRequest<OptionRequest> {

    public OptionRequest(String url) {
        super(url);
    }

    public OptionRequest(String url, Params params) {
        super(url, params);
    }

    public OptionRequest(String url, Params params, Config config) {
        super(url, params, config);
    }

    @Override
    protected void prepare() {
        if (mRequestBody != null) {
            mObservable = getClient().create().optionsBody(mUrl, mRequestBody);
        } else if (mMediaType != null && mContent != null) {
            mRequestBody = RequestBody.create(mMediaType, mContent);
            mObservable = getClient().create().optionsBody(mUrl, mRequestBody);
        } else if (mParams != null && mParams.size() > 0) {
            mObservable = getClient().create().options(mUrl, mParams);
        } else {
            mObservable = getClient().create().options(mUrl);
        }
    }


    /**
     * Singleton
     */
    public static class Singleton extends BodyRequest.Singleton<Singleton> {

        public Singleton(String url) {
            super(url);
        }

        public Singleton(String url, Params params) {
            super(url, params);
        }

        @Override
        protected void prepare() {
            if (mRequestBody != null) {
                mObservable = getClient().create().optionsBody(mUrl, mRequestBody);
            } else if (mMediaType != null && mContent != null) {
                mRequestBody = RequestBody.create(mMediaType, mContent);
                mObservable = getClient().create().optionsBody(mUrl, mRequestBody);
            } else if (mParams != null && mParams.size() > 0) {
                mObservable = getClient().create().options(mUrl, mParams);
            } else {
                mObservable = getClient().create().options(mUrl);
            }
        }
    }
}
