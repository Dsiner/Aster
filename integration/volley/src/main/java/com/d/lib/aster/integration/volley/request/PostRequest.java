package com.d.lib.aster.integration.volley.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.integration.volley.body.RequestBody;

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
        if (mRequestBody != null) {
            mObservable = getClient().create().postBody(mUrl, mRequestBody);
        } else if (mMediaType != null && mContent != null) {
            mRequestBody = RequestBody.create(mMediaType, mContent);
            mObservable = getClient().create().postBody(mUrl, mRequestBody);
        } else if (mParams != null && mParams.size() > 0) {
            mObservable = getClient().create().post(mUrl, mParams);
        } else {
            mObservable = getClient().create().post(mUrl);
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
                mObservable = getClient().create().postBody(mUrl, mRequestBody);
            } else if (mMediaType != null && mContent != null) {
                mRequestBody = RequestBody.create(mMediaType, mContent);
                mObservable = getClient().create().postBody(mUrl, mRequestBody);
            } else if (mParams != null && mParams.size() > 0) {
                mObservable = getClient().create().post(mUrl, mParams);
            } else {
                mObservable = getClient().create().post(mUrl);
            }
        }
    }
}
