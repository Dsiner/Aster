package com.d.lib.aster.integration.okhttp3.request;

import android.support.annotation.NonNull;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.okhttp3.MediaTypes;
import com.d.lib.aster.integration.okhttp3.OkHttpClient;
import com.d.lib.aster.integration.okhttp3.RequestManagerImpl;
import com.d.lib.aster.integration.okhttp3.func.ApiFunc;
import com.d.lib.aster.integration.okhttp3.func.ApiRetryFunc;
import com.d.lib.aster.integration.okhttp3.func.MapFunc;
import com.d.lib.aster.integration.okhttp3.observer.ApiObserver;
import com.d.lib.aster.integration.okhttp3.observer.AsyncApiObserver;
import com.d.lib.aster.request.IBodyRequest;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.DisposableObserver;
import com.d.lib.aster.scheduler.schedule.Schedulers;
import com.d.lib.aster.utils.Util;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by D on 2017/10/24.
 */
public abstract class BodyRequest<HR extends BodyRequest>
        extends IBodyRequest<HR, OkHttpClient,
        RequestBody, MediaType> {

    protected Call mCall;
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
    protected OkHttpClient getClient() {
        return OkHttpClient.create(IClient.TYPE_NORMAL, mConfig.log(true));
    }

    @Override
    public <T> void request(final SimpleCallback<T> callback) {
        prepare();
        DisposableObserver<T> disposableObserver = new ApiObserver<T>(mTag, mCall, callback);
        if (mTag != null) {
            RequestManagerImpl.getIns().add(mTag, disposableObserver);
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

    @Override
    public <T, R> void request(final AsyncCallback<T, R> callback) {
        prepare();
        DisposableObserver<R> disposableObserver = new AsyncApiObserver<T, R>(mTag, mCall, callback);
        if (mTag != null) {
            RequestManagerImpl.getIns().add(mTag, disposableObserver);
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
            extends IBodyRequest.Singleton<HRF, OkHttpClient,
            RequestBody, MediaType> {

        protected Call mCall;
        protected Observable<ResponseBody> mObservable;

        public Singleton(String url) {
            super(url);
        }

        public Singleton(String url, Params params) {
            super(url, params);
        }

        @Override
        protected OkHttpClient getClient() {
            return OkHttpClient.getDefault(IClient.TYPE_NORMAL);
        }

        @Override
        public <T> void request(final SimpleCallback<T> callback) {
            prepare();
            DisposableObserver<T> disposableObserver = new ApiObserver<T>(mTag, mCall, callback);
            if (mTag != null) {
                RequestManagerImpl.getIns().add(mTag, disposableObserver);
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

        @Override
        public <T, R> void request(final AsyncCallback<T, R> callback) {
            prepare();
            DisposableObserver<R> disposableObserver = new AsyncApiObserver<T, R>(mTag, mCall, callback);
            if (mTag != null) {
                RequestManagerImpl.getIns().add(mTag, disposableObserver);
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