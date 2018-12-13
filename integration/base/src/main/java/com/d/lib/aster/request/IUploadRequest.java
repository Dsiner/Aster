package com.d.lib.aster.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.IRequest;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.interceptor.IHeadersInterceptor;
import com.d.lib.aster.interceptor.IInterceptor;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by D on 2017/10/24.
 */
public abstract class IUploadRequest<HR extends IHttpRequest, C extends IClient>
        extends IRequest<HR, C> {

    public IUploadRequest(String url) {
        this(url, null);
    }

    public IUploadRequest(String url, Config config) {
        this.mUrl = url;
        this.mParams = new Params();
        this.mConfig = config != null ? config : Config.getDefault();
    }

    @Override
    protected C getClient() {
        return null;
    }

    protected abstract void prepare();

    public void request() {
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

    public IUploadRequest<HR, C> addParam(String paramKey, String paramValue) {
        return this;
    }

    public IUploadRequest<HR, C> addFile(String key, File file) {
        return addFile(key, file, null);
    }

    public IUploadRequest<HR, C> addFile(String key, File file, ProgressCallback callback) {
        return this;
    }

    public IUploadRequest<HR, C> addImageFile(String key, File file) {
        return this;
    }

    public IUploadRequest<HR, C> addImageFile(String key, File file, ProgressCallback callback) {
        return this;
    }

    public IUploadRequest<HR, C> addBytes(String key, byte[] bytes, String name) {
        return this;
    }

    public IUploadRequest<HR, C> addBytes(String key, byte[] bytes, String name, ProgressCallback callback) {
        return this;
    }

    public IUploadRequest<HR, C> addStream(String key, InputStream inputStream, String name) {
        return this;
    }

    public IUploadRequest<HR, C> addStream(String key, InputStream inputStream, String name, ProgressCallback callback) {
        return this;
    }

    /**
     * Singleton
     */
    public abstract static class Singleton<HRF extends IHttpRequest, C extends IClient>
            extends IRequest<HRF, C> {

        public Singleton(String url) {
            this.mUrl = url;
            this.mParams = new Params();
        }

        @Override
        protected C getClient() {
            return null;
        }

        protected abstract void prepare();

        public void request() {
        }

        public Singleton<HRF, C> addParam(String paramKey, String paramValue) {
            return this;
        }

        public Singleton<HRF, C> addFile(String key, File file) {
            return this;
        }

        public Singleton<HRF, C> addFile(String key, File file, ProgressCallback callback) {
            return this;
        }

        public Singleton<HRF, C> addImageFile(String key, File file) {
            return this;
        }

        public Singleton<HRF, C> addImageFile(String key, File file, ProgressCallback callback) {
            return this;
        }

        public Singleton<HRF, C> addBytes(String key, byte[] bytes, String name) {
            return this;
        }

        public Singleton<HRF, C> addBytes(String key, byte[] bytes, String name, ProgressCallback callback) {
            return this;
        }

        public Singleton<HRF, C> addStream(String key, InputStream inputStream, String name) {
            return this;
        }

        public Singleton<HRF, C> addStream(String key, InputStream inputStream, String name, ProgressCallback callback) {
            return this;
        }
    }
}
