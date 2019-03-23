package com.d.lib.aster.integration.retrofit.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.integration.retrofit.RetrofitAPI;

import okhttp3.RequestBody;

/**
 * Created by D on 2017/10/24.
 */
public class PatchRequest extends BodyRequest<PatchRequest> {

    public PatchRequest(String url) {
        super(url);
    }

    public PatchRequest(String url, Params params) {
        super(url, params);
    }

    public PatchRequest(String url, Params params, Config config) {
        super(url, params, config);
    }

    @Override
    protected void prepare() {
        if (mRequestBody != null) {
            mObservable = getClient().getClient().create(RetrofitAPI.class).patchBody(mUrl, mRequestBody);
        } else if (mMediaType != null && mContent != null) {
            mRequestBody = RequestBody.create(mMediaType, mContent);
            mObservable = getClient().getClient().create(RetrofitAPI.class).patchBody(mUrl, mRequestBody);
        } else if (mParams != null && mParams.size() > 0) {
            mObservable = getClient().getClient().create(RetrofitAPI.class).patch(mUrl, mParams);
        } else {
            mObservable = getClient().getClient().create(RetrofitAPI.class).patch(mUrl);
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
                mObservable = getClient().getClient().create(RetrofitAPI.class).patchBody(mUrl, mRequestBody);
            } else if (mMediaType != null && mContent != null) {
                mRequestBody = RequestBody.create(mMediaType, mContent);
                mObservable = getClient().getClient().create(RetrofitAPI.class).patchBody(mUrl, mRequestBody);
            } else if (mParams != null && mParams.size() > 0) {
                mObservable = getClient().getClient().create(RetrofitAPI.class).patch(mUrl, mParams);
            } else {
                mObservable = getClient().getClient().create(RetrofitAPI.class).patch(mUrl);
            }
        }
    }
}
