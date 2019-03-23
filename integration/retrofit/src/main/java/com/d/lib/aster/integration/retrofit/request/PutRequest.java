package com.d.lib.aster.integration.retrofit.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.integration.retrofit.RetrofitAPI;

import okhttp3.RequestBody;

/**
 * Singleton
 * Created by D on 2017/10/24.
 */
public class PutRequest extends BodyRequest<PutRequest> {

    public PutRequest(String url) {
        super(url);
    }

    public PutRequest(String url, Params params) {
        super(url, params);
    }

    public PutRequest(String url, Params params, Config config) {
        super(url, params, config);
    }

    @Override
    protected void prepare() {
        if (mRequestBody != null) {
            mObservable = getClient().getClient().create(RetrofitAPI.class).putBody(mUrl, mRequestBody);
        } else if (mMediaType != null && mContent != null) {
            mRequestBody = RequestBody.create(mMediaType, mContent);
            mObservable = getClient().getClient().create(RetrofitAPI.class).putBody(mUrl, mRequestBody);
        } else if (mParams != null && mParams.size() > 0) {
            mObservable = getClient().getClient().create(RetrofitAPI.class).put(mUrl, mParams);
        } else {
            mObservable = getClient().getClient().create(RetrofitAPI.class).put(mUrl);
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
                mObservable = getClient().getClient().create(RetrofitAPI.class).putBody(mUrl, mRequestBody);
            } else if (mMediaType != null && mContent != null) {
                mRequestBody = RequestBody.create(mMediaType, mContent);
                mObservable = getClient().getClient().create(RetrofitAPI.class).putBody(mUrl, mRequestBody);
            } else if (mParams != null && mParams.size() > 0) {
                mObservable = getClient().getClient().create(RetrofitAPI.class).put(mUrl, mParams);
            } else {
                mObservable = getClient().getClient().create(RetrofitAPI.class).put(mUrl);
            }
        }
    }
}
