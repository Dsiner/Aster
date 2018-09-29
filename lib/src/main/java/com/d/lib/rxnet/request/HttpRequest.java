package com.d.lib.rxnet.request;

import com.d.lib.rxnet.base.ApiManager;
import com.d.lib.rxnet.base.HttpConfig;
import com.d.lib.rxnet.base.IRequest;
import com.d.lib.rxnet.callback.AsyncCallback;
import com.d.lib.rxnet.callback.SimpleCallback;
import com.d.lib.rxnet.func.ApiFunc;
import com.d.lib.rxnet.func.ApiRetryFunc;
import com.d.lib.rxnet.func.MapFunc;
import com.d.lib.rxnet.observer.ApiObserver;
import com.d.lib.rxnet.observer.AsyncApiObserver;
import com.d.lib.rxnet.utils.Util;

import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;

/**
 * Created by D on 2017/10/24.
 */
public abstract class HttpRequest<HR extends HttpRequest> extends IRequest<HR> {
    protected Map<String, String> params;

    private HttpRequest() {
    }

    public HttpRequest(String url) {
        this.url = url;
        this.config = HttpConfig.getNewDefaultConfig();
    }

    public HttpRequest(String url, Map<String, String> params) {
        this.url = url;
        this.params = params;
        this.config = HttpConfig.getNewDefaultConfig();
    }

    protected abstract void prepare();

    public <T> void request(SimpleCallback<T> callback) {
        prepare();
        DisposableObserver disposableObserver = new ApiObserver(callback);
        if (super.tag != null) {
            ApiManager.get().add(super.tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(Util.getFirstCls(callback)))
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis))
                .subscribe(disposableObserver);
    }

    public <T, R> void request(AsyncCallback<T, R> callback) {
        prepare();
        DisposableObserver disposableObserver = new AsyncApiObserver(callback);
        if (super.tag != null) {
            ApiManager.get().add(super.tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(Util.getFirstCls(callback)))
                .map(new MapFunc<T, R>(callback))
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis))
                .subscribe(disposableObserver);
    }

    public <T> Observable<T> observable(Class<T> clazz) {
        prepare();
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(clazz))
                .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis));
    }

    @Override
    public HR baseUrl(String baseUrl) {
        config.baseUrl(baseUrl);
        return (HR) this;
    }

    @Override
    public HR headers(Map<String, String> headers) {
        config.headers(headers);
        return (HR) this;
    }

    @Override
    public HR connectTimeout(long timeout) {
        config.connectTimeout(timeout);
        return (HR) this;
    }

    @Override
    public HR readTimeout(long timeout) {
        config.readTimeout(timeout);
        return (HR) this;
    }

    @Override
    public HR writeTimeout(long timeout) {
        config.writeTimeout(timeout);
        return (HR) this;
    }

    @Override
    public HR sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        config.sslSocketFactory(sslSocketFactory);
        return (HR) this;
    }

    @Override
    public HR addInterceptor(Interceptor interceptor) {
        config.addInterceptor(interceptor);
        return (HR) this;
    }

    @Override
    public HR addNetworkInterceptors(Interceptor interceptor) {
        config.addNetworkInterceptors(interceptor);
        return (HR) this;
    }

    @Override
    public HR retryCount(int retryCount) {
        config.retryCount(retryCount);
        return (HR) this;
    }

    @Override
    public HR retryDelayMillis(long retryDelayMillis) {
        config.retryDelayMillis(retryDelayMillis);
        return (HR) this;
    }

    /**
     * Singleton
     */
    public static abstract class Singleton<HRF extends IRequest> extends IRequest<HRF> {
        protected Map<String, String> params;

        private Singleton() {
        }

        public Singleton(String url) {
            this.url = url;
            this.config = HttpConfig.getDefaultConfig();
        }

        public Singleton(String url, Map<String, String> params) {
            this.url = url;
            this.params = params;
            this.config = HttpConfig.getDefaultConfig();
        }

        protected abstract void prepare();

        public <T> void request(SimpleCallback<T> callback) {
            prepare();
            DisposableObserver disposableObserver = new ApiObserver(callback);
            if (super.tag != null) {
                ApiManager.get().add(super.tag, disposableObserver);
            }
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .map(new ApiFunc<T>(Util.getFirstCls(callback)))
                    .observeOn(AndroidSchedulers.mainThread())
                    .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis))
                    .subscribe(disposableObserver);
        }

        public <T, R> void request(AsyncCallback<T, R> callback) {
            prepare();
            DisposableObserver disposableObserver = new AsyncApiObserver(callback);
            if (super.tag != null) {
                ApiManager.get().add(super.tag, disposableObserver);
            }
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .map(new ApiFunc<T>(Util.getFirstCls(callback)))
                    .map(new MapFunc<T, R>(callback))
                    .observeOn(AndroidSchedulers.mainThread())
                    .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis))
                    .subscribe(disposableObserver);
        }

        public <T> Observable<T> observable(Class<T> clazz) {
            prepare();
            return observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .map(new ApiFunc<T>(clazz))
                    .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis));
        }
    }
}
