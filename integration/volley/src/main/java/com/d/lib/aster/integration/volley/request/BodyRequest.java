package com.d.lib.aster.integration.volley.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.MediaType;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.volley.MediaTypes;
import com.d.lib.aster.integration.volley.VolleyClient;
import com.d.lib.aster.integration.volley.body.RequestBody;
import com.d.lib.aster.integration.volley.client.Response;
import com.d.lib.aster.integration.volley.func.ApiFunc;
import com.d.lib.aster.integration.volley.func.ApiTransformer;
import com.d.lib.aster.request.IBodyRequest;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.schedule.Schedulers;

/**
 * Created by D on 2017/10/24.
 */
public abstract class BodyRequest<HR extends BodyRequest>
        extends IBodyRequest<HR, VolleyClient,
        RequestBody, MediaType> {

    protected Observable<Response> mObservable;

    public BodyRequest(String url) {
        super(url);
    }

    public BodyRequest(String url, Params params) {
        super(url, params);
    }

    public BodyRequest(String url, Params params, Config config) {
        super(url, params, config);
    }

    @Override
    protected VolleyClient getClient() {
        return VolleyClient.create(IClient.TYPE_NORMAL, mConfig.log(true));
    }

    @Override
    public <T> void request(final SimpleCallback<T> callback) {
        prepare();
        ApiTransformer.request(mObservable, mConfig, callback, mTag);
    }

    @Override
    public <T, R> void request(final AsyncCallback<T, R> callback) {
        prepare();
        ApiTransformer.requestAsync(mObservable, mConfig, callback, mTag);
    }

    @Override
    public <T> Observable.Observe<T> observable(Class<T> clazz) {
        prepare();
        return mObservable.subscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(clazz));
    }

    @Override
    public HR setRequestBody(RequestBody requestBody) {
        this.mRequestBody = requestBody;
        return (HR) this;
    }

    @Override
    public HR setString(String string) {
        this.mContent = string;
        this.mMediaType = MediaTypes.TEXT_PLAIN_TYPE;
        return (HR) this;
    }

    @Override
    public HR setJson(String json) {
        this.mContent = json;
        this.mMediaType = MediaTypes.APPLICATION_JSON_TYPE;
        return (HR) this;
    }


    /**
     * Singleton
     */
    public abstract static class Singleton<HRF extends Singleton>
            extends IBodyRequest.Singleton<HRF, VolleyClient,
            RequestBody, MediaType> {

        protected Observable<Response> mObservable;

        public Singleton(String url) {
            super(url);
        }

        public Singleton(String url, Params params) {
            super(url, params);
        }

        @Override
        protected VolleyClient getClient() {
            return VolleyClient.getDefault(IClient.TYPE_NORMAL);
        }

        @Override
        public <T> void request(final SimpleCallback<T> callback) {
            prepare();
            ApiTransformer.request(mObservable, getClient().getHttpConfig(),
                    callback, mTag);
        }

        @Override
        public <T, R> void request(final AsyncCallback<T, R> callback) {
            prepare();
            ApiTransformer.requestAsync(mObservable, getClient().getHttpConfig(),
                    callback, mTag);
        }

        @Override
        public <T> Observable.Observe<T> observable(Class<T> clazz) {
            prepare();
            return mObservable.subscribeOn(Schedulers.io())
                    .map(new ApiFunc<T>(clazz));
        }

        @Override
        public HRF setRequestBody(RequestBody requestBody) {
            this.mRequestBody = requestBody;
            return (HRF) this;
        }

        @Override
        public HRF setString(String string) {
            this.mContent = string;
            this.mMediaType = MediaTypes.TEXT_PLAIN_TYPE;
            return (HRF) this;
        }

        @Override
        public HRF setJson(String json) {
            this.mContent = json;
            this.mMediaType = MediaTypes.APPLICATION_JSON_TYPE;
            return (HRF) this;
        }
    }
}
