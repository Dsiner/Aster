package com.d.lib.aster.base;

import com.d.lib.aster.interceptor.HeadersInterceptor;
import com.d.lib.aster.interceptor.Interceptor;

import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

/**
 * IConfig
 * Created by D on 2017/10/25.
 */
public abstract class IConfig<R> {
    protected abstract R baseUrl(String baseUrl);

    protected abstract R headers(Map<String, String> headers);

    protected abstract R headers(HeadersInterceptor.OnHeadInterceptor onHeadInterceptor);

    protected abstract R connectTimeout(long timeout);

    protected abstract R readTimeout(long timeout);

    protected abstract R writeTimeout(long timeout);

    protected abstract R sslSocketFactory(SSLSocketFactory sslSocketFactory);

    protected abstract R addInterceptor(Interceptor interceptor);

    protected abstract R addNetworkInterceptors(Interceptor interceptor);

    /************************** Retry **************************/
    protected abstract R retryCount(int retryCount);

    protected abstract R retryDelayMillis(long retryDelayMillis);
}
