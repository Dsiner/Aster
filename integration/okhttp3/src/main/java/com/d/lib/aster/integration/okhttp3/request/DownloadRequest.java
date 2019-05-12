package com.d.lib.aster.integration.okhttp3.request;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.integration.okhttp3.OkHttpApi;
import com.d.lib.aster.integration.okhttp3.OkHttpClient;
import com.d.lib.aster.integration.okhttp3.func.ApiTransformer;
import com.d.lib.aster.request.IDownloadRequest;
import com.d.lib.aster.scheduler.Observable;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by D on 2017/10/24.
 */
public class DownloadRequest extends IDownloadRequest<DownloadRequest, OkHttpClient> {
    protected Call mCall;
    protected Observable<Response> mObservable;

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
            callable = getClient().create().get(mUrl, mParams);
        } else {
            callable = getClient().create().get(mUrl);
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
        ApiTransformer.requestDownload(mCall, mObservable, mConfig, path, name, callback, mTag);
    }


    /**
     * Singleton
     */
    public static class Singleton extends IDownloadRequest.Singleton<Singleton, OkHttpClient> {
        protected Observable<Response> mObservable;
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
                callable = getClient().create().get(mUrl, mParams);
            } else {
                callable = getClient().create().get(mUrl);
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
            ApiTransformer.requestDownload(mCall, mObservable, getClient().getHttpConfig(),
                    path, name, callback, mTag);
        }
    }
}
