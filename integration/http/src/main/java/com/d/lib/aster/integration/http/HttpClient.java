package com.d.lib.aster.integration.http;

import android.support.annotation.NonNull;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.integration.http.client.HttpURLApi;
import com.d.lib.aster.integration.http.client.HttpURLClient;
import com.d.lib.aster.integration.http.interceptor.HttpLoggingInterceptor;
import com.d.lib.aster.interceptor.IHeadersInterceptor;
import com.d.lib.aster.interceptor.IInterceptor;
import com.d.lib.aster.util.ULog;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

/**
 * HttpClient
 * Created by D on 2017/7/14.
 */
public class HttpClient extends IClient {
    private HttpURLClient mClient;
    private HttpURLApi mHttpURLApi;

    private static class Default {
        private static final HttpClient INSTANCE = create(TYPE_NORMAL, Config.getDefault().log(true));
    }

    private static class Transfer {
        private static final HttpClient DOWNLOAD = create(TYPE_DOWNLOAD, Config.getDefault().log(false));
        private static final HttpClient UPLOAD = create(TYPE_UPLOAD, Config.getDefault().log(false));
    }

    private HttpClient(@State int type, @NonNull Config config) {
        super(type, config);
        this.mClient = getClient(config);
        this.mHttpURLApi = new HttpURLApi(mClient);
    }

    @NonNull
    public HttpURLClient getClient() {
        return mClient;
    }

    @NonNull
    public HttpURLApi create() {
        return mHttpURLApi;
    }

    public static HttpClient create(@State int type, @NonNull Config config) {
        return new HttpClient(type, config);
    }

    /**
     * Singleton - Default configuration
     */
    @NonNull
    public static HttpClient getDefault(@State int type) {
        if (type == TYPE_DOWNLOAD) {
            return Transfer.DOWNLOAD;
        } else if (type == TYPE_UPLOAD) {
            return Transfer.UPLOAD;
        } else {
            return HttpClient.Default.INSTANCE;
        }
    }

    /**
     * New instance - Custom configuration
     *
     * @param config Configuration
     * @return HttpURLClient
     */
    @NonNull
    public static HttpURLClient getClient(@NonNull Config config) {
        return getHttpURLClient(config.headers,
                config.onHeadInterceptor,
                config.connectTimeout != -1 ? config.connectTimeout : Config.getDefault().connectTimeout,
                config.readTimeout != -1 ? config.readTimeout : Config.getDefault().readTimeout,
                config.writeTimeout != -1 ? config.writeTimeout : Config.getDefault().writeTimeout,
                config.sslSocketFactory,
                config.interceptors,
                config.networkInterceptors,
                config.log);
    }

    private static HttpURLClient getHttpURLClient(Map<String, String> headers,
                                                  IHeadersInterceptor.OnHeadInterceptor onHeadInterceptor,
                                                  long connectTimeout,
                                                  long readTimeout,
                                                  long writeTimeout,
                                                  SSLSocketFactory sslSocketFactory,
                                                  ArrayList<IInterceptor> interceptors,
                                                  ArrayList<IInterceptor> networkInterceptors,
                                                  boolean log) {
        HttpURLClient.Builder builder = new HttpURLClient().newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);

        if (sslSocketFactory != null) {
            builder.sslSocketFactory(sslSocketFactory);
        }

        if (headers != null && headers.size() > 0 || onHeadInterceptor != null) {
            builder.addInterceptor((IInterceptor) new com.d.lib.aster.integration.http.interceptor
                    .HeadersInterceptor(headers)
                    .setOnHeadInterceptor(onHeadInterceptor));
        }
        if (interceptors != null && interceptors.size() > 0) {
            for (IInterceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        if (log) {
            builder.addInterceptor(getHttpURLLog());
        }

        if (networkInterceptors != null && networkInterceptors.size() > 0) {
            for (IInterceptor networkInterceptor : networkInterceptors) {
                builder.addNetworkInterceptor((IInterceptor) networkInterceptor);
            }
        }
        return builder.build();
    }

    private static HttpLoggingInterceptor getHttpURLLog() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

            @Override
            public void log(String s) {
                // Print log
                ULog.d(Config.Default.TAG_LOG + s);
            }
        });
        loggingInterceptor.setLevel(getLoggingLevel());
        return loggingInterceptor;
    }

    @NonNull
    private static HttpLoggingInterceptor.Level getLoggingLevel() {
        HttpLoggingInterceptor.Level level;
        if (Config.Level.BODY == Config.Default.LOG_LEVEL) {
            level = HttpLoggingInterceptor.Level.BODY;
        } else if (Config.Level.HEADERS == Config.Default.LOG_LEVEL) {
            level = HttpLoggingInterceptor.Level.HEADERS;
        } else if (Config.Level.BASIC == Config.Default.LOG_LEVEL) {
            level = HttpLoggingInterceptor.Level.BASIC;
        } else {
            level = HttpLoggingInterceptor.Level.NONE;
        }
        return level;
    }
}
