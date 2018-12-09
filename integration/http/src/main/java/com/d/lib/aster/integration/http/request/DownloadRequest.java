package com.d.lib.aster.integration.http.request;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.IRequest;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.integration.http.HttpClient;
import com.d.lib.aster.integration.http.RequestManager;
import com.d.lib.aster.integration.http.client.ResponseBody;
import com.d.lib.aster.integration.http.func.ApiRetryFunc;
import com.d.lib.aster.integration.http.observer.DownloadObserver;
import com.d.lib.aster.interceptor.HeadersInterceptor;
import com.d.lib.aster.interceptor.Interceptor;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.DisposableObserver;
import com.d.lib.aster.scheduler.schedule.Schedulers;
import com.d.lib.aster.utils.Util;

import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by D on 2017/10/24.
 */
public class DownloadRequest extends IRequest<DownloadRequest, HttpClient> {
    protected Observable<ResponseBody> mObservable;

    public DownloadRequest(String url) {
        this(url, null);
    }

    public DownloadRequest(String url, Params params) {
        this(url, params, null);
    }

    public DownloadRequest(String url, Params params, Config config) {
        this.mUrl = url;
        this.mParams = params;
        this.mConfig = config != null ? config : Config.getDefault();
    }

    @Override
    protected HttpClient getClient() {
        return HttpClient.create(IClient.TYPE_DOWNLOAD, mConfig.log(false));
    }

    private void prepare() {
        if (mParams == null || mParams.size() <= 0) {
            mObservable = getClient().create().download(mUrl);
        } else {
            mObservable = getClient().create().download(mUrl, mParams);
        }
    }

    @SuppressWarnings("ConstantConditions")
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
        requestImpl(mObservable, getClient().getHttpConfig(), mTag, path, name, callback);
    }

    private static void requestImpl(final Observable<ResponseBody> observable,
                                    final Config config,
                                    final Object tag,
                                    final String path, final String name,
                                    final ProgressCallback callback) {
        if (callback != null) {
            Util.executeMain(new Runnable() {
                @Override
                public void run() {
                    callback.onStart();

                }
            });
        }
        DisposableObserver<ResponseBody> disposableObserver = new DownloadObserver(path, name, tag, callback);
        if (tag != null) {
            RequestManager.getIns().add(tag, disposableObserver);
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

    @Override
    public DownloadRequest baseUrl(String baseUrl) {
        return super.baseUrl(baseUrl);
    }

    @Override
    public DownloadRequest headers(Map<String, String> headers) {
        return super.headers(headers);
    }

    @Override
    public DownloadRequest headers(HeadersInterceptor.OnHeadInterceptor onHeadInterceptor) {
        return super.headers(onHeadInterceptor);
    }

    @Override
    public DownloadRequest connectTimeout(long timeout) {
        return super.connectTimeout(timeout);
    }

    @Override
    public DownloadRequest readTimeout(long timeout) {
        return super.readTimeout(timeout);
    }

    @Override
    public DownloadRequest writeTimeout(long timeout) {
        return super.writeTimeout(timeout);
    }

    @Override
    public DownloadRequest sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        return super.sslSocketFactory(sslSocketFactory);
    }

    @Override
    public DownloadRequest addInterceptor(Interceptor interceptor) {
        return super.addInterceptor(interceptor);
    }

    @Override
    public DownloadRequest addNetworkInterceptors(Interceptor interceptor) {
        return super.addNetworkInterceptors(interceptor);
    }

    @Override
    public DownloadRequest retryCount(int retryCount) {
        return super.retryCount(retryCount);
    }

    @Override
    public DownloadRequest retryDelayMillis(long retryDelayMillis) {
        return super.retryDelayMillis(retryDelayMillis);
    }

    /**
     * Singleton
     */
    public static class Singleton extends IRequest<Singleton, HttpClient> {
        protected Observable<ResponseBody> mObservable;

        public Singleton(String url) {
            this(url, null);
        }

        public Singleton(String url, Params params) {
            this.mUrl = url;
            this.mParams = params;
        }

        @Override
        protected HttpClient getClient() {
            return HttpClient.getDefault(IClient.TYPE_DOWNLOAD);
        }

        private void prepare() {
            if (mParams == null || mParams.size() <= 0) {
                mObservable = getClient().create().download(mUrl);
            } else {
                mObservable = getClient().create().download(mUrl, mParams);
            }
        }

        @SuppressWarnings("ConstantConditions")
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
            requestImpl(mObservable, getClient().getHttpConfig(), mTag, path, name, callback);
        }
    }
}
