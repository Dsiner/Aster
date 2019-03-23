package com.d.lib.aster.integration.okhttp3.request;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.integration.okhttp3.OkHttpApi;
import com.d.lib.aster.integration.okhttp3.OkHttpClient;
import com.d.lib.aster.integration.okhttp3.RequestManagerImpl;
import com.d.lib.aster.integration.okhttp3.func.ApiRetryFunc;
import com.d.lib.aster.integration.okhttp3.observer.DownloadObserver;
import com.d.lib.aster.request.IDownloadRequest;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.DisposableObserver;
import com.d.lib.aster.scheduler.schedule.Schedulers;

import okhttp3.Call;
import okhttp3.ResponseBody;

/**
 * Created by D on 2017/10/24.
 */
public class DownloadRequest extends IDownloadRequest<DownloadRequest, OkHttpClient> {
    protected Call mCall;
    protected Observable<ResponseBody> mObservable;

    public DownloadRequest(String url) {
        super(url, null);
    }

    public DownloadRequest(String url, Params params) {
        super(url, params);
    }

    public DownloadRequest(String url, Params params, Config config) {
        super(url, params, config);
    }

    @Override
    protected OkHttpClient getClient() {
        return OkHttpClient.create(IClient.TYPE_DOWNLOAD, mConfig.log(false));
    }

    @Override
    protected void prepare() {
        final OkHttpApi.Callable callable;
        if (mParams != null && mParams.size() > 0) {
            callable = getClient().create().download(mUrl, mParams);
        } else {
            callable = getClient().create().download(mUrl);
        }
        mCall = callable.call;
        mObservable = callable.observable;
    }

    @Override
    public void request(@NonNull final String path, @NonNull final String name,
                        @NonNull final ProgressCallback callback) {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("This path can not be empty!");
        }
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("This name can not be empty!");
        }
        if (callback == null) {
            throw new NullPointerException("This callback must not be null!");
        }
        prepare();
        requestImpl(mObservable, getClient().getHttpConfig(), path, name, mTag, mCall, callback);
    }

    private static void requestImpl(final Observable<ResponseBody> observable,
                                    final Config config,
                                    final String path, final String name,
                                    final Object tag,
                                    final Call call,
                                    final ProgressCallback callback) {
        if (callback != null) {
            Observable.executeMain(new Runnable() {
                @Override
                public void run() {
                    callback.onStart();
                }
            });
        }
        DisposableObserver<ResponseBody> disposableObserver = new DownloadObserver(path, name,
                tag,
                call,
                callback);
        if (tag != null) {
            RequestManagerImpl.getIns().add(tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new ApiRetryFunc<ResponseBody>(disposableObserver,
                        config.retryCount, config.retryDelayMillis,
                        new ApiRetryFunc.OnRetry<ResponseBody>() {
                            @NonNull
                            @Override
                            public Observable.Observe<ResponseBody> observe() {
                                return observable.subscribeOn(Schedulers.io())
                                        .observeOn(Schedulers.io());
                            }
                        }));
    }


    /**
     * Singleton
     */
    public static class Singleton extends IDownloadRequest.Singleton<Singleton, OkHttpClient> {
        protected Observable<ResponseBody> mObservable;
        protected Call mCall;

        public Singleton(String url) {
            super(url);
        }

        public Singleton(String url, Params params) {
            super(url, params);
        }

        @Override
        protected OkHttpClient getClient() {
            return OkHttpClient.getDefault(IClient.TYPE_DOWNLOAD);
        }

        @Override
        protected void prepare() {
            final OkHttpApi.Callable callable;
            if (mParams != null && mParams.size() > 0) {
                callable = getClient().create().download(mUrl, mParams);
            } else {
                callable = getClient().create().download(mUrl);
            }
            mCall = callable.call;
            mObservable = callable.observable;
        }

        @Override
        public void request(@NonNull final String path, @NonNull final String name,
                            @NonNull final ProgressCallback callback) {
            if (TextUtils.isEmpty(path)) {
                throw new IllegalArgumentException("This path can not be empty!");
            }
            if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("This name can not be empty!");
            }
            if (callback == null) {
                throw new NullPointerException("This callback must not be null!");
            }
            prepare();
            requestImpl(mObservable, getClient().getHttpConfig(), path, name, mTag, mCall, callback);
        }
    }
}
