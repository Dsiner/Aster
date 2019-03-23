package com.d.lib.aster.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.interceptor.IHeadersInterceptor;
import com.d.lib.aster.interceptor.IInterceptor;

import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by D on 2017/10/24.
 */
public abstract class IBodyRequest<HR extends IBodyRequest, C extends IClient,
        RB, MT>
        extends IHttpRequest<HR, C> {

    protected RB mRequestBody;
    protected MT mMediaType;
    protected String mContent;

    public IBodyRequest(String url) {
        super(url);
    }

    public IBodyRequest(String url, Params params) {
        super(url, params);
    }

    public IBodyRequest(String url, Params params, Config config) {
        super(url, params, config);
    }

    public abstract HR setRequestBody(RB requestBody);

    public abstract HR setString(String string);

    public abstract HR setJson(String json);

    @Override
    public HR tag(Object tag) {
        return super.tag(tag);
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
    public abstract static class Singleton<HRF extends Singleton, C extends IClient,
            RB, MT>
            extends IHttpRequest.Singleton<HRF, C> {

        protected RB mRequestBody;
        protected MT mMediaType;
        protected String mContent;

        public Singleton(String url) {
            super(url);
        }

        public Singleton(String url, Params params) {
            super(url, params);
        }

        public Singleton(String url, Params params, Config config) {
            super(url, params, config);
        }

        public abstract HRF setRequestBody(RB requestBody);

        public abstract HRF setString(String string);

        public abstract HRF setJson(String json);
    }
}
