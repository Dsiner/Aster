package com.d.lib.aster.integration.volley.request;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.integration.volley.VolleyClient;
import com.d.lib.aster.integration.volley.client.Response;
import com.d.lib.aster.integration.volley.func.ApiTransformer;
import com.d.lib.aster.request.IDownloadRequest;
import com.d.lib.aster.scheduler.Observable;

/**
 * Created by D on 2017/10/24.
 */
@Deprecated
public class DownloadRequest extends IDownloadRequest<DownloadRequest, VolleyClient> {
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
    protected VolleyClient getClient() {
        return VolleyClient.create(IClient.TYPE_DOWNLOAD, mConfig.log(false));
    }

    @Override
    protected void prepare() {
        if (mParams == null || mParams.size() <= 0) {
            mObservable = getClient().create().get(mUrl);
        } else {
            mObservable = getClient().create().get(mUrl, mParams);
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
        ApiTransformer.requestDownload(mObservable, mConfig, path, name, callback, mTag);
    }


    /**
     * Singleton
     */
    @Deprecated
    public static class Singleton extends IDownloadRequest.Singleton<Singleton, VolleyClient> {
        protected Observable<Response> mObservable;

        public Singleton(String url) {
            super(url);
        }

        public Singleton(String url, Params params) {
            super(url, params);
        }

        @Override
        protected VolleyClient getClient() {
            return VolleyClient.getDefault(IClient.TYPE_DOWNLOAD);
        }

        @Override
        protected void prepare() {
            if (mParams == null || mParams.size() <= 0) {
                mObservable = getClient().create().get(mUrl);
            } else {
                mObservable = getClient().create().get(mUrl, mParams);
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
            ApiTransformer.requestDownload(mObservable, getClient().getHttpConfig(),
                    path, name, callback, mTag);
        }
    }
}
