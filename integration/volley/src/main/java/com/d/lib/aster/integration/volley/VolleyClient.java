package com.d.lib.aster.integration.volley;

import android.support.annotation.NonNull;

import com.d.lib.aster.base.AsterModule;
import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.integration.volley.client.VolleyApi;
import com.d.lib.aster.integration.volley.interceptor.HttpLoggingInterceptor;
import com.d.lib.aster.interceptor.IHeadersInterceptor;
import com.d.lib.aster.interceptor.IInterceptor;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

/**
 * VolleyClient
 * Created by D on 2017/7/14.
 */
public class VolleyClient extends IClient {
    private com.d.lib.aster.integration.volley.client.VolleyClient mClient;
    private VolleyApi mVolleyApi;

    private static class Default {
        private final static VolleyClient INSTANCE = create(TYPE_NORMAL, Config.getDefault().log(true));
    }

    private static class Transfer {
        private final static VolleyClient DOWNLOAD = create(TYPE_DOWNLOAD, Config.getDefault().log(false));
        private final static VolleyClient UPLOAD = create(TYPE_UPLOAD, Config.getDefault().log(false));
    }

    private VolleyClient(@State int type, @NonNull Config config) {
        super(type, config);
        this.mClient = getClient(config);
        this.mVolleyApi = new VolleyApi(mClient);
    }

    @NonNull
    public com.d.lib.aster.integration.volley.client.VolleyClient getClient() {
        return mClient;
    }

    @NonNull
    public VolleyApi create() {
        return mVolleyApi;
    }

    public static VolleyClient create(@State int type, @NonNull Config config) {
        return new VolleyClient(type, config);
    }

    /**
     * Singleton - Default configuration
     */
    @NonNull
    public static VolleyClient getDefault(@State int type) {
        if (type == TYPE_DOWNLOAD) {
            return Transfer.DOWNLOAD;
        } else if (type == TYPE_UPLOAD) {
            return Transfer.UPLOAD;
        } else {
            return VolleyClient.Default.INSTANCE;
        }
    }

    /**
     * New instance - Custom configuration
     *
     * @param config Configuration
     * @return VolleyClient
     */
    @NonNull
    public static com.d.lib.aster.integration.volley.client.VolleyClient getClient(@NonNull Config config) {
        return getVolleyClient(config.headers,
                config.onHeadInterceptor,
                config.connectTimeout != -1 ? config.connectTimeout : Config.getDefault().connectTimeout,
                config.readTimeout != -1 ? config.readTimeout : Config.getDefault().readTimeout,
                config.writeTimeout != -1 ? config.writeTimeout : Config.getDefault().writeTimeout,
                config.sslSocketFactory,
                config.interceptors,
                config.networkInterceptors,
                config.log);
    }

    private static com.d.lib.aster.integration.volley.client.VolleyClient getVolleyClient(Map<String, String> headers,
                                                                                          IHeadersInterceptor.OnHeadInterceptor onHeadInterceptor,
                                                                                          long connectTimeout,
                                                                                          long readTimeout,
                                                                                          long writeTimeout,
                                                                                          SSLSocketFactory sslSocketFactory,
                                                                                          ArrayList<IInterceptor> interceptors,
                                                                                          ArrayList<IInterceptor> networkInterceptors,
                                                                                          boolean log) {
        com.d.lib.aster.integration.volley.client.VolleyClient.Builder builder
                = new com.d.lib.aster.integration.volley.client.VolleyClient().newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);

        AsterModule.Registry registry = AsterModule.getRegistry();
        if (registry != null
                && registry instanceof VolleyRegistry) {
            builder.setHurlStack(((VolleyRegistry) registry).getHttpStack());
        }

        if (sslSocketFactory != null) {
            builder.sslSocketFactory(sslSocketFactory);
        }

        if (headers != null && headers.size() > 0 || onHeadInterceptor != null) {
            builder.addInterceptor((IInterceptor) new com.d.lib.aster.integration.volley.interceptor.
                    HeadersInterceptor(headers)
                    .setOnHeadInterceptor(onHeadInterceptor));
        }
        if (interceptors != null && interceptors.size() > 0) {
            for (IInterceptor interceptor : interceptors) {
                builder.addInterceptor((IInterceptor) interceptor);
            }
        }
        if (log) {
            builder.addInterceptor(getVolleyLog());
        }

        if (networkInterceptors != null && networkInterceptors.size() > 0) {
            for (IInterceptor networkInterceptor : networkInterceptors) {
                builder.addNetworkInterceptor((IInterceptor) networkInterceptor);
            }
        }
        return builder.build();
    }

    private static HttpLoggingInterceptor getVolleyLog() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        return loggingInterceptor;
    }
}
