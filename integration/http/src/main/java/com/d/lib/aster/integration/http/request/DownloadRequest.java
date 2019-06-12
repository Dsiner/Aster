package com.d.lib.aster.integration.http.request;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.integration.http.HttpClient;
import com.d.lib.aster.integration.http.client.Call;
import com.d.lib.aster.integration.http.client.HttpURLApi;
import com.d.lib.aster.integration.http.client.Response;
import com.d.lib.aster.integration.http.func.ApiTransformer;
import com.d.lib.aster.request.IDownloadRequest;
import com.d.lib.aster.scheduler.Observable;

/**
 * Created by D on 2017/10/24.
 */
public class DownloadRequest extends IDownloadRequest<DownloadRequest, HttpClient> {
    protected Call mCall;
    protected Observable<Response> mObservable;

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
            mCall = callable.call;
            mObservable = callable.observable;
        } else {
            final HttpURLApi.Callable callable = getClient().create().download(mUrl, mParams);
            mCall = callable.call;
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
        ApiTransformer.requestDownload(mCall, mObservable, mConfig, path, name, callback, mTag);
    }


    /**
     * Singleton
     */
    public static class Singleton extends IDownloadRequest.Singleton<Singleton, HttpClient> {
        protected Call mCall;
        protected Observable<Response> mObservable;

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
                mCall = callable.call;
                mObservable = callable.observable;
            } else {
                final HttpURLApi.Callable callable = getClient().create().download(mUrl, mParams);
                mCall = callable.call;
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
            ApiTransformer.requestDownload(mCall, mObservable, getClient().getHttpConfig(),
                    path, name, callback, mTag);
        }
    }
}
