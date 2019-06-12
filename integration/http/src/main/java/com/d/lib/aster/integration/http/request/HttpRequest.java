package com.d.lib.aster.integration.http.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.http.HttpClient;
import com.d.lib.aster.integration.http.client.Call;
import com.d.lib.aster.integration.http.client.Response;
import com.d.lib.aster.integration.http.func.ApiFunc;
import com.d.lib.aster.integration.http.func.ApiTransformer;
import com.d.lib.aster.request.IHttpRequest;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.schedule.Schedulers;

/**
 * Created by D on 2017/10/24.
 */
public abstract class HttpRequest<HR extends HttpRequest> extends IHttpRequest<HR, HttpClient> {
    protected Call mCall;
    protected Observable<Response> mObservable;

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
    protected HttpClient getClient() {
        return HttpClient.create(IClient.TYPE_NORMAL, mConfig.log(true));
    }

    @Override
    public <T> void request(final SimpleCallback<T> callback) {
        prepare();
        ApiTransformer.request(mCall, mObservable, mConfig, callback, mTag);
    }

    @Override
    public <T, R> void request(final AsyncCallback<T, R> callback) {
        prepare();
        ApiTransformer.requestAsync(mCall, mObservable, mConfig, callback, mTag);
    }

    @Override
    public <T> Observable.Observe<T> observable(Class<T> clazz) {
        prepare();
        return mObservable.subscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(clazz));
    }


    /**
     * Singleton
     */
    public static abstract class Singleton<HRF extends Singleton>
            extends IHttpRequest.Singleton<HRF, HttpClient> {
        protected Call mCall;
        protected Observable<Response> mObservable;

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
        protected HttpClient getClient() {
            return HttpClient.getDefault(IClient.TYPE_NORMAL);
        }

        @Override
        public <T> void request(final SimpleCallback<T> callback) {
            prepare();
            ApiTransformer.request(mCall, mObservable, getClient().getHttpConfig(),
                    callback, mTag);
        }

        @Override
        public <T, R> void request(final AsyncCallback<T, R> callback) {
            prepare();
            ApiTransformer.requestAsync(mCall, mObservable, getClient().getHttpConfig(),
                    callback, mTag);
        }

        @Override
        public <T> Observable.Observe<T> observable(Class<T> clazz) {
            prepare();
            return mObservable.subscribeOn(Schedulers.io())
                    .map(new ApiFunc<T>(clazz));
        }
    }
}
