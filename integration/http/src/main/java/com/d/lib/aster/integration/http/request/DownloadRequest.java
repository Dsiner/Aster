package com.d.lib.aster.integration.http.request;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.integration.http.HttpClient;
import com.d.lib.aster.integration.http.RequestManagerImpl;
import com.d.lib.aster.integration.http.client.HttpURLApi;
import com.d.lib.aster.integration.http.client.ResponseBody;
import com.d.lib.aster.integration.http.func.ApiRetryFunc;
import com.d.lib.aster.integration.http.observer.DownloadObserver;
import com.d.lib.aster.request.IDownloadRequest;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.DisposableObserver;
import com.d.lib.aster.scheduler.schedule.Schedulers;

import java.net.HttpURLConnection;

/**
 * Created by D on 2017/10/24.
 */
public class DownloadRequest extends IDownloadRequest<DownloadRequest, HttpClient> {
    protected HttpURLConnection mConn;
    protected Observable<ResponseBody> mObservable;

    public DownloadRequest(String url) {
        super(url);
    }

    public DownloadRequest(String url, Params params) {
        super(url, params);
    }

    public DownloadRequest(String url, Params params, Config config) {
        super(url, params, config);
    }

    @Override
    protected HttpClient getClient() {
        return HttpClient.create(IClient.TYPE_DOWNLOAD, mConfig.log(false));
    }

    @Override
    protected void prepare() {
        if (mParams == null || mParams.size() <= 0) {
            final HttpURLApi.Callable callable = getClient().create().download(mUrl);
            mConn = callable.conn;
            mObservable = callable.observable;
        } else {
            final HttpURLApi.Callable callable = getClient().create().download(mUrl, mParams);
            mConn = callable.conn;
            mObservable = callable.observable;
        }
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
        requestImpl(mObservable, getClient().getHttpConfig(), path, name,
                mTag, mConn, callback);
    }

    private static void requestImpl(final Observable<ResponseBody> observable,
                                    final Config config,
                                    final String path, final String name,
                                    final Object tag,
                                    final HttpURLConnection conn,
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
                conn,
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
    public static class Singleton extends IDownloadRequest.Singleton<Singleton, HttpClient> {
        protected HttpURLConnection mConn;
        protected Observable<ResponseBody> mObservable;

        public Singleton(String url) {
            super(url);
        }

        public Singleton(String url, Params params) {
            super(url, params);
        }

        @Override
        protected HttpClient getClient() {
            return HttpClient.getDefault(IClient.TYPE_DOWNLOAD);
        }

        @Override
        protected void prepare() {
            if (mParams == null || mParams.size() <= 0) {
                final HttpURLApi.Callable callable = getClient().create().download(mUrl);
                mConn = callable.conn;
                mObservable = callable.observable;
            } else {
                final HttpURLApi.Callable callable = getClient().create().download(mUrl, mParams);
                mConn = callable.conn;
                mObservable = callable.observable;
            }
        }

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
            requestImpl(mObservable, getClient().getHttpConfig(), path, name,
                    mTag, mConn, callback);
        }
    }
}
