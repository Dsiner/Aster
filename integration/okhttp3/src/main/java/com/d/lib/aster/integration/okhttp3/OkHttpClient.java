package com.d.lib.aster.integration.okhttp3;

import android.support.annotation.NonNull;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.integration.okhttp3.interceptor.HeadersInterceptor;
import com.d.lib.aster.interceptor.IInterceptor;
import com.d.lib.aster.util.ULog;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OkHttpClient
 * Created by D on 2017/7/14.
 */
public class OkHttpClient extends IClient {
    private okhttp3.OkHttpClient mClient;
    private OkHttpApi mOkHttpApi;

    private static class Default {
        private static final OkHttpClient INSTANCE = create(TYPE_NORMAL, Config.getDefault().log(true));
    }

    private static class Transfer {
        private static final OkHttpClient DOWNLOAD = create(TYPE_DOWNLOAD, Config.getDefault().log(false));
        private static final OkHttpClient UPLOAD = create(TYPE_UPLOAD, Config.getDefault().log(false));
    }

    private OkHttpClient(@State int type, @NonNull Config config) {
        super(type, config);
        this.mClient = getClient(config);
        this.mOkHttpApi = new OkHttpApi(mClient);
    }

    @NonNull
    public okhttp3.OkHttpClient getClient() {
        return mClient;
    }

    @NonNull
    public OkHttpApi create() {
        return mOkHttpApi;
    }

    public static OkHttpClient create(@State int type, @NonNull Config config) {
        return new OkHttpClient(type, config);
    }

    /**
     * Singleton - Default configuration
     */
    @NonNull
    public static OkHttpClient getDefault(@State int type) {
        if (type == TYPE_DOWNLOAD) {
            return Transfer.DOWNLOAD;
        } else if (type == TYPE_UPLOAD) {
            return Transfer.UPLOAD;
        } else {
            return OkHttpClient.Default.INSTANCE;
        }
    }

    /**
     * New instance - Custom configuration
     *
     * @param config Configuration
     * @return OkHttpClient
     */
    @NonNull
    public static okhttp3.OkHttpClient getClient(@NonNull Config config) {
        return getOkHttpClient(config.headers,
                config.onHeadInterceptor,
                config.connectTimeout != -1 ? config.connectTimeout : Config.getDefault().connectTimeout,
                config.readTimeout != -1 ? config.readTimeout : Config.getDefault().readTimeout,
                config.writeTimeout != -1 ? config.writeTimeout : Config.getDefault().writeTimeout,
                config.sslSocketFactory,
                config.interceptors,
                config.networkInterceptors,
                config.log);
    }

    private static okhttp3.OkHttpClient getOkHttpClient(Map<String, String> headers,
                                                        HeadersInterceptor.OnHeadInterceptor onHeadInterceptor,
                                                        long connectTimeout,
                                                        long readTimeout,
                                                        long writeTimeout,
                                                        SSLSocketFactory sslSocketFactory,
                                                        ArrayList<IInterceptor> interceptors,
                                                        ArrayList<IInterceptor> networkInterceptors,
                                                        boolean log) {
        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient().newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);

        if (sslSocketFactory != null) {
            builder.sslSocketFactory(sslSocketFactory);
        }

        if (headers != null && headers.size() > 0 || onHeadInterceptor != null) {
            builder.addInterceptor((okhttp3.Interceptor) new HeadersInterceptor(headers)
                    .setOnHeadInterceptor(onHeadInterceptor));
        }
        if (interceptors != null && interceptors.size() > 0) {
            for (IInterceptor interceptor : interceptors) {
                builder.addInterceptor((okhttp3.Interceptor) interceptor);
            }
        }
        if (log) {
            builder.addInterceptor(getOkHttpLog());
        }

        if (networkInterceptors != null && networkInterceptors.size() > 0) {
            for (IInterceptor networkInterceptor : networkInterceptors) {
                builder.addNetworkInterceptor((okhttp3.Interceptor) networkInterceptor);
            }
        }
        return builder.build();
    }

    private static HttpLoggingInterceptor getOkHttpLog() {
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
