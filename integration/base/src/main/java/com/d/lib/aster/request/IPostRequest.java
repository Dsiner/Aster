package com.d.lib.aster.request;

import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.MediaType;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.interceptor.IHeadersInterceptor;
import com.d.lib.aster.interceptor.IInterceptor;
import com.d.lib.aster.scheduler.Observable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by D on 2017/10/24.
 */
public abstract class IPostRequest<HR extends IHttpRequest, C extends IClient>
        extends IHttpRequest<HR, C> {

    public IPostRequest(String url) {
        super(url);
    }

    public IPostRequest(String url, Params params) {
        super(url, params);
    }

    @Override
    protected void prepare() {

    }

    public <T> void request(SimpleCallback<T> callback) {
        super.request(callback);
    }

    @Override
    public <T, R> void request(AsyncCallback<T, R> callback) {
        super.request(callback);
    }

    @Override
    public <T> Observable.Observe<T> observable(Class<T> clazz) {
        return super.observable(clazz);
    }

    public HR addForm(String formKey, Object formValue) {
        return (HR) this;
    }

//    public HR setRequestBody(RequestBody requestBody) {
//        return (HR) this;
//    }

    public HR setString(String string) {
        return (HR) this;
    }

    public HR setString(String string, MediaType mediaType) {
        return (HR) this;
    }

    public HR setJson(String json) {
        return (HR) this;
    }

    public HR setJson(JSONObject jsonObject) {
        return (HR) this;
    }

    public HR setJson(JSONArray jsonArray) {
        return (HR) this;
    }

    @Override
    public HR baseUrl(String baseUrl) {
        return super.baseUrl(baseUrl);
    }

    @Override
    public HR headers(Map<String, String> headers) {
        return super.headers(headers);
    }

    @Override
    public HR headers(IHeadersInterceptor.OnHeadInterceptor onHeadInterceptor) {
        return super.headers(onHeadInterceptor);
    }

    @Override
    public HR connectTimeout(long timeout) {
        return super.connectTimeout(timeout);
    }

    @Override
    public HR readTimeout(long timeout) {
        return super.readTimeout(timeout);
    }

    @Override
    public HR writeTimeout(long timeout) {
        return super.writeTimeout(timeout);
    }

    @Override
    public HR sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        return super.sslSocketFactory(sslSocketFactory);
    }

    @Override
    public HR addInterceptor(IInterceptor interceptor) {
        return super.addInterceptor(interceptor);
    }

    @Override
    public HR addNetworkInterceptors(IInterceptor interceptor) {
        return super.addNetworkInterceptors(interceptor);
    }

    @Override
    public HR retryCount(int retryCount) {
        return super.retryCount(retryCount);
    }

    @Override
    public HR retryDelayMillis(long retryDelayMillis) {
        return super.retryDelayMillis(retryDelayMillis);
    }

    /**
     * Singleton
     */
    public abstract static class Singleton<HRF extends IHttpRequest, C extends IClient>
            extends IHttpRequest.Singleton<HRF, C> {

        public Singleton(String url) {
            super(url);
        }

        public Singleton(String url, Params params) {
            super(url, params);
        }

        @Override
        public <T> void request(SimpleCallback<T> callback) {
            super.request(callback);
        }

        @Override
        public <T, R> void request(AsyncCallback<T, R> callback) {
            super.request(callback);
        }

        @Override
        public <T> Observable.Observe<T> observable(Class<T> clazz) {
            return super.observable(clazz);
        }

        public Singleton<HRF, C> addForm(String formKey, Object formValue) {
            return this;
        }

//        public Singleton setRequestBody(RequestBody requestBody) {
//            return this;
//        }

        public Singleton<HRF, C> setString(String string) {
            return this;
        }

        public Singleton<HRF, C> setString(String string, MediaType mediaType) {
            return this;
        }

        public Singleton<HRF, C> setJson(String json) {
            return this;
        }

        public Singleton<HRF, C> setJson(JSONObject jsonObject) {
            return this;
        }

        public Singleton<HRF, C> setJson(JSONArray jsonArray) {
            return this;
        }
    }
}
