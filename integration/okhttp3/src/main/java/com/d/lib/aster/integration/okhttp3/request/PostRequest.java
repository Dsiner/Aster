package com.d.lib.aster.integration.okhttp3.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.integration.okhttp3.OkHttpApi;

import okhttp3.RequestBody;

/**
 * Created by D on 2017/10/24.
 */
public class PostRequest extends BodyRequest<PostRequest> {

    public PostRequest(String url) {
        super(url);
    }

    public PostRequest(String url, Params params) {
        super(url, params);
    }

    public PostRequest(String url, Params params, Config config) {
        super(url, params, config);
    }

    @Override
    protected void prepare() {
        final OkHttpApi.Callable callable;
        if (mRequestBody != null) {
            callable = getClient().create().postBody(mUrl, mRequestBody);
        } else if (mMediaType != null && mContent != null) {
            mRequestBody = RequestBody.create(mMediaType, mContent);
            callable = getClient().create().postBody(mUrl, mRequestBody);
        } else if (mParams != null && mParams.size() > 0) {
            callable = getClient().create().post(mUrl, mParams);
        } else {
            callable = getClient().create().post(mUrl);
        }
        mCall = callable.call;
        mObservable = callable.observable;
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
            final OkHttpApi.Callable callable;
            if (mRequestBody != null) {
                callable = getClient().create().postBody(mUrl, mRequestBody);
            } else if (mMediaType != null && mContent != null) {
                mRequestBody = RequestBody.create(mMediaType, mContent);
                callable = getClient().create().postBody(mUrl, mRequestBody);
            } else if (mParams != null && mParams.size() > 0) {
                callable = getClient().create().post(mUrl, mParams);
            } else {
                callable = getClient().create().post(mUrl);
            }
            mCall = callable.call;
            mObservable = callable.observable;
        }
    }
}
