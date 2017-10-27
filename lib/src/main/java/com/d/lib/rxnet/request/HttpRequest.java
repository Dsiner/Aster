package com.d.lib.rxnet.request;

import android.content.Context;

import com.d.lib.rxnet.base.ApiManager;
import com.d.lib.rxnet.func.ApiFunc;
import com.d.lib.rxnet.func.ApiRetryFunc;
import com.d.lib.rxnet.func.MapFunc;
import com.d.lib.rxnet.listener.AsyncCallBack;
import com.d.lib.rxnet.listener.SimpleCallBack;
import com.d.lib.rxnet.observer.ApiObserver;
import com.d.lib.rxnet.observer.SyncApiObserver;
import com.d.lib.rxnet.util.RxUtil;

import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;

/**
 * Instance
 * Created by D on 2017/10/24.
 */
public class HttpRequest<HR extends HttpRequest> extends BaseRequest<HR> {
    protected Map<String, String> params;

    private HttpRequest() {
    }

    public HttpRequest(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    public HttpRequest(Context context, String url, Map<String, String> params) {
        this.context = context;
        this.url = url;
        this.params = params;
    }

    protected void init() {

    }

    public <T> void request(SimpleCallBack<T> callback) {
        init();
        DisposableObserver disposableObserver = new ApiObserver(callback);
        if (super.tag != null) {
            ApiManager.get().add(super.tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(RxUtil.getFirstCls(callback)))
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis))
                .subscribe(disposableObserver);
    }

    public <T, R> void request(AsyncCallBack<T, R> callback) {
        init();
        DisposableObserver disposableObserver = new SyncApiObserver(callback);
        if (super.tag != null) {
            ApiManager.get().add(super.tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(RxUtil.getFirstCls(callback)))
                .map(new MapFunc<T, R>(callback))
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis))
                .subscribe(disposableObserver);
    }

    public <T> Observable<T> observable(Class<T> clazz) {
        init();
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(clazz))
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis));
    }

    /******************* Config *******************/
    @Override
    protected HR baseUrl(String baseUrl) {
        config.baseUrl(baseUrl);
        return (HR) this;
    }

    @Override
    protected HR headers(Map<String, String> headers) {
        config.headers(headers);
        return (HR) this;
    }

    @Override
    protected HR connectTimeout(long timeout) {
        config.connectTimeout(timeout);
        return (HR) this;
    }

    @Override
    protected HR readTimeout(long timeout) {
        config.readTimeout(timeout);
        return (HR) this;
    }

    @Override
    protected HR writeTimeout(long timeout) {
        config.writeTimeout(timeout);
        return (HR) this;
    }

    @Override
    protected HR addInterceptor(Interceptor interceptor) {
        config.addInterceptor(interceptor);
        return (HR) this;
    }

    @Override
    protected HR sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        config.sslSocketFactory(sslSocketFactory);
        return (HR) this;
    }

    @Override
    protected HR retryCount(int retryCount) {
        config.retryCount(retryCount);
        return (HR) this;
    }

    @Override
    protected HR retryDelayMillis(long retryDelayMillis) {
        config.retryDelayMillis(retryDelayMillis);
        return (HR) this;
    }

    /**
     * New
     */
    public static class HttpRequestF<HRF extends HttpRequest> extends HttpRequest<HRF> {

        public HttpRequestF(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        public HttpRequestF(Context context, String url, Map<String, String> params) {
            this.context = context;
            this.url = url;
            this.params = params;
        }

        @Override
        protected void init() {

        }

        @Override
        public HRF tag(Object tag) {
            this.tag = tag;
            return (HRF) this;
        }

        /******************* Config *******************/
        @Override
        public HRF baseUrl(String baseUrl) {
            config.baseUrl(baseUrl);
            return (HRF) this;
        }

        @Override
        public HRF headers(Map<String, String> headers) {
            config.headers(headers);
            return (HRF) this;
        }

        @Override
        public HRF connectTimeout(long timeout) {
            config.connectTimeout(timeout);
            return (HRF) this;
        }

        @Override
        public HRF readTimeout(long timeout) {
            config.readTimeout(timeout);
            return (HRF) this;
        }

        @Override
        public HRF writeTimeout(long timeout) {
            config.writeTimeout(timeout);
            return (HRF) this;
        }

        @Override
        public HRF addInterceptor(Interceptor interceptor) {
            config.addInterceptor(interceptor);
            return (HRF) this;
        }

        @Override
        public HRF sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            config.sslSocketFactory(sslSocketFactory);
            return (HRF) this;
        }

        @Override
        public HRF retryCount(int retryCount) {
            config.retryCount(retryCount);
            return (HRF) this;
        }

        @Override
        public HRF retryDelayMillis(long retryDelayMillis) {
            config.retryDelayMillis(retryDelayMillis);
            return (HRF) this;
        }
    }
}
