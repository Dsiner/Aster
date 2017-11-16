package com.d.lib.rxnet.base;

import android.content.Context;
import android.text.TextUtils;

import com.d.lib.rxnet.interceptor.HeadersInterceptor;
import com.d.lib.rxnet.util.RxLog;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitClient
 * Created by D on 2017/7/14.
 */
public class RetrofitClient {
    private static Retrofit retrofit;

    /**
     * 单例-默认配置
     */
    public static synchronized Retrofit getInstance(Context context) {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    retrofit = getRetrofitDefault(context.getApplicationContext());
                }
            }
        }
        return retrofit;
    }

    /**
     * 实例-默认配置
     */
    public static Retrofit getRetrofitDefault(Context context) {
        return getRetrofit(context, HttpConfig.getDefaultConfig(), true);
    }

    /**
     * 实例-自定义配置
     */
    public static Retrofit getRetrofit(Context context, HttpConfig config) {
        return getRetrofit(context, config, true);
    }

    /**
     * 实例-下载配置（LogLevel.NONE）
     */
    public static Retrofit getRetrofitDown(Context context, HttpConfig config) {
        return getRetrofit(context, config, false);
    }

    /**
     * 实例-自定义配置
     */
    private static Retrofit getRetrofit(Context context, HttpConfig config, boolean log) {
        Retrofit retrofit = new Retrofit.Builder()
                //设置OKHttpClient,如果不设置会提供一个默认的
                .client(getOkHttpClient(context,
                        config.headers,
                        config.connectTimeout != -1 ? config.connectTimeout : HttpConfig.getDefaultConfig().connectTimeout,
                        config.readTimeout != -1 ? config.readTimeout : HttpConfig.getDefaultConfig().readTimeout,
                        config.writeTimeout != -1 ? config.writeTimeout : HttpConfig.getDefaultConfig().writeTimeout,
                        config.sslSocketFactory,
                        config.interceptors,
                        config.networkInterceptors,
                        log))
                //设置baseUrl
                .baseUrl(!TextUtils.isEmpty(config.baseUrl) ? config.baseUrl : HttpConfig.getDefaultConfig().baseUrl)
                //设置rx
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //添加转换器
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    private static OkHttpClient getOkHttpClient(Context context,
                                                Map<String, String> headers,
                                                long connectTimeout,
                                                long readTimeout,
                                                long writeTimeout,
                                                SSLSocketFactory sslSocketFactory,
                                                ArrayList<Interceptor> interceptors,
                                                ArrayList<Interceptor> networkInterceptors,
                                                boolean log) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);

        if (sslSocketFactory != null) {
            builder.sslSocketFactory(sslSocketFactory);
        }

        if (headers != null && headers.size() > 0) {
            builder.addInterceptor(new HeadersInterceptor(context, headers));
        }
        if (interceptors != null && interceptors.size() > 0) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        if (log) {
            builder.addInterceptor(getOkhttpLog());
        }

        if (networkInterceptors != null && networkInterceptors.size() > 0) {
            for (Interceptor networkInterceptor : networkInterceptors) {
                builder.addNetworkInterceptor(networkInterceptor);
            }
        }
        return builder.build();
    }

    private static HttpLoggingInterceptor getOkhttpLog() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

            @Override
            public void log(String s) {
                //打印retrofit日志
                RxLog.d(Config.TAG_LOG + s);
            }
        });
        loggingInterceptor.setLevel(Config.LOG_LEVEL);
        return loggingInterceptor;
    }

    private static SSLSocketFactory getSSLSocketFactory() {
        SSLContext sslContext = null;
        try {
            //取得TrustManagerFactory的X509密钥管理器实例
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            RxLog.e("SslContextFactory:" + e.getMessage());
            return null;
        }
    }
}
