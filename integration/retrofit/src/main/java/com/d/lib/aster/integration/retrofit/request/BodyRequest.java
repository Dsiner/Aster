package com.d.lib.aster.integration.retrofit.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.okhttp3.MediaTypes;
import com.d.lib.aster.integration.retrofit.RetrofitClient;
import com.d.lib.aster.integration.retrofit.func.ApiFunc;
import com.d.lib.aster.integration.retrofit.func.ApiRetryFunc;
import com.d.lib.aster.integration.retrofit.func.ApiTransformer;
import com.d.lib.aster.request.IBodyRequest;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by D on 2017/10/24.
 */
public abstract class BodyRequest<HR extends BodyRequest>
        extends IBodyRequest<HR, RetrofitClient,
        RequestBody, MediaType> {

    protected Observable<ResponseBody> mObservable;

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
    protected RetrofitClient getClient() {
        return RetrofitClient.create(IClient.TYPE_NORMAL, mConfig.log(true));
    }

    @Override
    public <T> void request(SimpleCallback<T> callback) {
        prepare();
        ApiTransformer.request(mObservable, mConfig, callback, mTag);
    }

    @Override
    public <T, R> void request(AsyncCallback<T, R> callback) {
        prepare();
        ApiTransformer.requestAsync(mObservable, mConfig, callback, mTag);
    }

    @Override
    public <T> com.d.lib.aster.scheduler.Observable.Observe<T> observable(Class<T> clazz) {
        return null;
    }

    public <T> Observable<T> observableRx(Class<T> clazz) {
        prepare();
        return mObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(clazz))
                .retryWhen(new ApiRetryFunc(mConfig.retryCount, mConfig.retryDelayMillis));
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
            extends IBodyRequest.Singleton<HRF, RetrofitClient,
            RequestBody, MediaType> {

        protected Observable<ResponseBody> mObservable;

        public Singleton(String url) {
            super(url);
        }

        public Singleton(String url, Params params) {
            super(url, params);
        }

        public Singleton(String url, Params params, Config config) {
            super(url, params, config);
        }

        @Override
        protected RetrofitClient getClient() {
            return RetrofitClient.getDefault(IClient.TYPE_NORMAL);
        }

        @Override
        public <T> void request(SimpleCallback<T> callback) {
            prepare();
            ApiTransformer.request(mObservable, getClient().getHttpConfig(), callback, mTag);
        }

        @Override
        public <T, R> void request(AsyncCallback<T, R> callback) {
            prepare();
            ApiTransformer.requestAsync(mObservable, getClient().getHttpConfig(), callback, mTag);
        }

        @Override
        public <T> com.d.lib.aster.scheduler.Observable.Observe<T> observable(Class<T> clazz) {
            return null;
        }

        public <T> Observable<T> observableRx(Class<T> clazz) {
            prepare();
            return mObservable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .map(new ApiFunc<T>(clazz))
                    .retryWhen(new ApiRetryFunc(getClient().getHttpConfig().retryCount,
                            getClient().getHttpConfig().retryDelayMillis));
        }

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
