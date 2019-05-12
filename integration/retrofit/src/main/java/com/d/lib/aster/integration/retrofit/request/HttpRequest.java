package com.d.lib.aster.integration.retrofit.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.retrofit.RetrofitClient;
import com.d.lib.aster.integration.retrofit.func.ApiFunc;
import com.d.lib.aster.integration.retrofit.func.ApiRetryFunc;
import com.d.lib.aster.integration.retrofit.func.ApiTransformer;
import com.d.lib.aster.request.IHttpRequest;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by D on 2017/10/24.
 */
public abstract class HttpRequest<HR extends HttpRequest>
        extends IHttpRequest<HR, RetrofitClient> {
    protected Observable<ResponseBody> mObservable;

    public HttpRequest(String url) {
        super(url);
    }

    public HttpRequest(String url, Params params) {
        super(url, params);
    }

    public HttpRequest(String url, Params params, Config config) {
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


    /**
     * Singleton
     */
    public static abstract class Singleton<HRF extends Singleton>
            extends IHttpRequest.Singleton<HRF, RetrofitClient> {

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
    }
}
