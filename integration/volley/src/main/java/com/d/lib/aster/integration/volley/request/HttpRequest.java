package com.d.lib.aster.integration.volley.request;

import android.support.annotation.NonNull;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.IRequest;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.volley.RequestManager;
import com.d.lib.aster.integration.volley.VolleyClient;
import com.d.lib.aster.integration.volley.client.ResponseBody;
import com.d.lib.aster.integration.volley.func.ApiFunc;
import com.d.lib.aster.integration.volley.func.ApiRetryFunc;
import com.d.lib.aster.integration.volley.func.MapFunc;
import com.d.lib.aster.integration.volley.observer.ApiObserver;
import com.d.lib.aster.integration.volley.observer.AsyncApiObserver;
import com.d.lib.aster.interceptor.IHeadersInterceptor;
import com.d.lib.aster.interceptor.IInterceptor;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.DisposableObserver;
import com.d.lib.aster.scheduler.schedule.Schedulers;
import com.d.lib.aster.utils.Util;

import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by D on 2017/10/24.
 */
public abstract class HttpRequest<HR extends HttpRequest> extends IRequest<HR, VolleyClient> {
    protected Observable<ResponseBody> mObservable;

    private HttpRequest() {
    }

    public HttpRequest(String url) {
        this(url, null);
    }

    public HttpRequest(String url, Params params) {
        this(url, params, null);
    }

    public HttpRequest(String url, Params params, Config config) {
        this.mUrl = url;
        this.mParams = params;
        this.mConfig = config != null ? config : Config.getDefault();
    }

    @Override
    protected VolleyClient getClient() {
        return VolleyClient.create(IClient.TYPE_NORMAL, mConfig.log(true));
    }

    /**
     * Initialize Observable, etc.
     */
    protected abstract void prepare();

    public <T> void request(final SimpleCallback<T> callback) {
        prepare();
        DisposableObserver<T> disposableObserver = new ApiObserver<T>(mTag, callback);
        if (mTag != null) {
            RequestManager.getIns().add(mTag, disposableObserver);
        }
        mObservable.subscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(Util.getFirstCls(callback)))
                .observeOn(Schedulers.mainThread())
                .subscribe(new ApiRetryFunc<T>(disposableObserver,
                        mConfig.retryCount,
                        mConfig.retryDelayMillis,
                        new ApiRetryFunc.OnRetry<T>() {
                            @NonNull
                            @Override
                            public Observable.Observe<T> observe() {
                                return mObservable.subscribeOn(Schedulers.io())
                                        .map(new ApiFunc<T>(Util.getFirstCls(callback)))
                                        .observeOn(Schedulers.mainThread());
                            }
                        }));
    }

    public <T, R> void request(final AsyncCallback<T, R> callback) {
        prepare();
        DisposableObserver<R> disposableObserver = new AsyncApiObserver<T, R>(mTag, callback);
        if (mTag != null) {
            RequestManager.getIns().add(mTag, disposableObserver);
        }
        mObservable.subscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(Util.getFirstCls(callback)))
                .map(new MapFunc<T, R>(callback))
                .observeOn(Schedulers.mainThread())
                .subscribe(new ApiRetryFunc<R>(disposableObserver,
                        mConfig.retryCount, mConfig.retryDelayMillis,
                        new ApiRetryFunc.OnRetry<R>() {
                            @NonNull
                            @Override
                            public Observable.Observe<R> observe() {
                                return mObservable.subscribeOn(Schedulers.io())
                                        .map(new ApiFunc<T>(Util.getFirstCls(callback)))
                                        .map(new MapFunc<T, R>(callback))
                                        .observeOn(Schedulers.mainThread());
                            }
                        }));
    }

    public <T> Observable.Observe<T> observable(Class<T> clazz) {
        prepare();
        return mObservable.subscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(clazz));
    }

    @Override
    public HR baseUrl(String baseUrl) {
        mConfig.baseUrl(baseUrl);
        return (HR) this;
    }

    @Override
    public HR headers(Map<String, String> headers) {
        mConfig.headers(headers);
        return (HR) this;
    }

    @Override
    public HR headers(IHeadersInterceptor.OnHeadInterceptor onHeadInterceptor) {
        mConfig.headers(onHeadInterceptor);
        return (HR) this;
    }

    @Override
    public HR connectTimeout(long timeout) {
        mConfig.connectTimeout(timeout);
        return (HR) this;
    }

    @Override
    public HR readTimeout(long timeout) {
        mConfig.readTimeout(timeout);
        return (HR) this;
    }

    @Override
    public HR writeTimeout(long timeout) {
        mConfig.writeTimeout(timeout);
        return (HR) this;
    }

    @Override
    public HR sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        mConfig.sslSocketFactory(sslSocketFactory);
        return (HR) this;
    }

    @Override
    public HR addInterceptor(IInterceptor interceptor) {
        mConfig.addInterceptor(interceptor);
        return (HR) this;
    }

    @Override
    public HR addNetworkInterceptors(IInterceptor interceptor) {
        mConfig.addNetworkInterceptors(interceptor);
        return (HR) this;
    }

    @Override
    public HR retryCount(int retryCount) {
        mConfig.retryCount(retryCount);
        return (HR) this;
    }

    @Override
    public HR retryDelayMillis(long retryDelayMillis) {
        mConfig.retryDelayMillis(retryDelayMillis);
        return (HR) this;
    }

    /**
     * Singleton
     */
    public static abstract class Singleton<HRF extends IRequest> extends IRequest<HRF, VolleyClient> {
        protected Observable<ResponseBody> mObservable;

        private Singleton() {
        }

        public Singleton(String url) {
            this(url, null);
        }

        public Singleton(String url, Params params) {
            this(url, params, null);
        }

        public Singleton(String url, Params params, Config config) {
            this.mUrl = url;
            this.mParams = params;
            this.mConfig = config != null ? config : Config.getDefault();
        }

        @Override
        protected VolleyClient getClient() {
            return VolleyClient.getDefault(IClient.TYPE_NORMAL);
        }

        /**
         * Initialize Observable, etc.
         */
        protected abstract void prepare();

        public <T> void request(final SimpleCallback<T> callback) {
            prepare();
            DisposableObserver<T> disposableObserver = new ApiObserver<T>(mTag, callback);
            if (mTag != null) {
                RequestManager.getIns().add(mTag, disposableObserver);
            }
            mObservable.subscribeOn(Schedulers.io())
                    .map(new ApiFunc<T>(Util.getFirstCls(callback)))
                    .observeOn(Schedulers.mainThread())
                    .subscribe(new ApiRetryFunc<T>(disposableObserver,
                            getClient().getHttpConfig().retryCount,
                            getClient().getHttpConfig().retryDelayMillis,
                            new ApiRetryFunc.OnRetry<T>() {
                                @NonNull
                                @Override
                                public Observable.Observe<T> observe() {
                                    return mObservable.subscribeOn(Schedulers.io())
                                            .map(new ApiFunc<T>(Util.getFirstCls(callback)))
                                            .observeOn(Schedulers.mainThread());
                                }
                            }));
        }

        public <T, R> void request(final AsyncCallback<T, R> callback) {
            prepare();
            DisposableObserver<R> disposableObserver = new AsyncApiObserver<T, R>(mTag, callback);
            if (mTag != null) {
                RequestManager.getIns().add(mTag, disposableObserver);
            }
            mObservable.subscribeOn(Schedulers.io())
                    .map(new ApiFunc<T>(Util.getFirstCls(callback)))
                    .map(new MapFunc<T, R>(callback))
                    .observeOn(Schedulers.mainThread())
                    .subscribe(new ApiRetryFunc<R>(disposableObserver,
                            getClient().getHttpConfig().retryCount,
                            getClient().getHttpConfig().retryDelayMillis,
                            new ApiRetryFunc.OnRetry<R>() {
                                @NonNull
                                @Override
                                public Observable.Observe<R> observe() {
                                    return mObservable.subscribeOn(Schedulers.io())
                                            .map(new ApiFunc<T>(Util.getFirstCls(callback)))
                                            .map(new MapFunc<T, R>(callback))
                                            .observeOn(Schedulers.mainThread());
                                }
                            }));
        }

        public <T> Observable.Observe<T> observable(Class<T> clazz) {
            prepare();
            return mObservable.subscribeOn(Schedulers.io())
                    .map(new ApiFunc<T>(clazz));
        }
    }
}
