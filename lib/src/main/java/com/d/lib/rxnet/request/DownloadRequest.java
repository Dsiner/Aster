package com.d.lib.rxnet.request;

import android.content.Context;

import com.d.lib.rxnet.api.RetrofitAPI;
import com.d.lib.rxnet.base.ApiManager;
import com.d.lib.rxnet.base.RetrofitClient;
import com.d.lib.rxnet.func.ApiRetryFunc;
import com.d.lib.rxnet.listener.DownloadCallBack;
import com.d.lib.rxnet.observer.DownloadObserver;

import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;

/**
 * Instance
 * Created by D on 2017/10/24.
 */
public class DownloadRequest extends BaseRequest<DownloadRequest> {
    protected Map<String, String> params;

    public DownloadRequest(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    public DownloadRequest(Context context, String url, Map<String, String> params) {
        this.context = context;
        this.url = url;
        this.params = params;
    }

    protected void init() {
        if (params == null) {
            observable = RetrofitClient.getInstance(context).create(RetrofitAPI.class).download(url);
        } else {
            observable = RetrofitClient.getInstance(context).create(RetrofitAPI.class).download(url, params);
        }
    }

    public void request(String path, String name, DownloadCallBack callback) {
        init();
        DisposableObserver disposableObserver = new DownloadObserver(path, name, callback);
        if (super.tag != null) {
            ApiManager.get().add(super.tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis))
                .subscribe(disposableObserver);
    }

    @Override
    protected DownloadRequest baseUrl(String baseUrl) {
        return this;
    }

    @Override
    protected DownloadRequest headers(Map<String, String> headers) {
        return this;
    }

    @Override
    protected DownloadRequest connectTimeout(long timeout) {
        return this;
    }

    @Override
    protected DownloadRequest readTimeout(long timeout) {
        return this;
    }

    @Override
    protected DownloadRequest writeTimeout(long timeout) {
        return this;
    }

    @Override
    protected DownloadRequest addInterceptor(Interceptor interceptor) {
        return this;
    }

    @Override
    protected DownloadRequest sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        return this;
    }

    @Override
    protected DownloadRequest retryCount(int retryCount) {
        return this;
    }

    @Override
    protected DownloadRequest retryDelayMillis(long retryDelayMillis) {
        return this;
    }

    /**
     * New
     */
    public static class DownloadRequestF extends DownloadRequest {

        public DownloadRequestF(Context context, String url) {
            super(context, url);
        }

        public DownloadRequestF(Context context, String url, Map<String, String> params) {
            super(context, url, params);
        }

        @Override
        protected void init() {
            if (params == null) {
                observable = RetrofitClient.getRetrofit(context, config).create(RetrofitAPI.class).download(url);
            } else {
                observable = RetrofitClient.getRetrofit(context, config).create(RetrofitAPI.class).download(url, params);
            }
        }

        @Override
        public DownloadRequestF tag(Object tag) {
            this.tag = tag;
            return this;
        }

        /******************* Config *******************/
        @Override
        public DownloadRequestF baseUrl(String baseUrl) {
            config.baseUrl(baseUrl);
            return this;
        }

        @Override
        public DownloadRequestF headers(Map<String, String> headers) {
            config.headers(headers);
            return this;
        }

        @Override
        public DownloadRequestF connectTimeout(long timeout) {
            config.connectTimeout(timeout);
            return this;
        }

        @Override
        public DownloadRequestF readTimeout(long timeout) {
            config.readTimeout(timeout);
            return this;
        }

        @Override
        public DownloadRequestF writeTimeout(long timeout) {
            config.writeTimeout(timeout);
            return this;
        }

        @Override
        public DownloadRequestF addInterceptor(Interceptor interceptor) {
            config.addInterceptor(interceptor);
            return this;
        }

        @Override
        public DownloadRequestF sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            config.sslSocketFactory(sslSocketFactory);
            return this;
        }

        @Override
        public DownloadRequestF retryCount(int retryCount) {
            config.retryCount(retryCount);
            return this;
        }

        @Override
        public DownloadRequestF retryDelayMillis(long retryDelayMillis) {
            config.retryDelayMillis(retryDelayMillis);
            return this;
        }
    }
}
