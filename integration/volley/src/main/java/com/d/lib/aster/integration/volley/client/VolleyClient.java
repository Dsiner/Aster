package com.d.lib.aster.integration.volley.client;

import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.interceptor.Interceptor;

import java.net.Proxy;
import java.net.ProxySelector;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * VolleyClient
 * Created by D on 2018/12/8.
 **/
public class VolleyClient {
    @Nullable
    final Proxy proxy;
    final List<Interceptor> interceptors;
    final List<Interceptor> networkInterceptors;
    final ProxySelector proxySelector;
    final SocketFactory socketFactory;
    @Nullable
    final SSLSocketFactory sslSocketFactory;
    final boolean followSslRedirects;
    final boolean followRedirects;
    final boolean retryOnConnectionFailure;
    final int connectTimeout;
    final int readTimeout;
    final int writeTimeout;
    final int pingInterval;
    final OkHttpStack okHttpStack;

    volatile RequestQueue mRequestQueue;

    public VolleyClient() {
        this(new Builder());
    }

    VolleyClient(Builder builder) {
        this.okHttpStack = builder.okHttpStack;
        this.proxy = builder.proxy;
        this.interceptors = builder.interceptors;
        this.networkInterceptors = builder.networkInterceptors;
        this.proxySelector = builder.proxySelector;
        this.socketFactory = builder.socketFactory;

        if (builder.sslSocketFactory != null) {
            this.sslSocketFactory = builder.sslSocketFactory;
        } else {
            X509TrustManager trustManager = systemDefaultTrustManager();
            this.sslSocketFactory = systemDefaultSslSocketFactory(trustManager);
        }

        this.followSslRedirects = builder.followSslRedirects;
        this.followRedirects = builder.followRedirects;
        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            synchronized (this) {
                if (mRequestQueue == null) {
                    if (okHttpStack != null) {
                        mRequestQueue = Volley.newRequestQueue(IClient.getContext(), okHttpStack);
                    } else {
                        mRequestQueue = Volley.newRequestQueue(IClient.getContext());
                    }
                }
            }
        }
        return mRequestQueue;
    }

    private X509TrustManager systemDefaultTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }
    }

    private SSLSocketFactory systemDefaultSslSocketFactory(X509TrustManager trustManager) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            return sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static final class Builder {
        @Nullable
        Proxy proxy;
        final List<Interceptor> interceptors = new ArrayList<>();
        final List<Interceptor> networkInterceptors = new ArrayList<>();
        ProxySelector proxySelector;
        SocketFactory socketFactory;
        @Nullable
        SSLSocketFactory sslSocketFactory;
        boolean followSslRedirects;
        boolean followRedirects;
        boolean retryOnConnectionFailure;
        int connectTimeout;
        int readTimeout;
        int writeTimeout;
        int pingInterval;
        OkHttpStack okHttpStack;

        public Builder() {
            proxySelector = ProxySelector.getDefault();
            socketFactory = SocketFactory.getDefault();
            followSslRedirects = true;
            followRedirects = true;
            retryOnConnectionFailure = true;
            connectTimeout = 10_000;
            readTimeout = 10_000;
            writeTimeout = 10_000;
            pingInterval = 0;
        }

        Builder(VolleyClient volleyClient) {
            this.okHttpStack = volleyClient.okHttpStack;
            this.proxy = volleyClient.proxy;
            this.interceptors.addAll(volleyClient.interceptors);
            this.networkInterceptors.addAll(volleyClient.networkInterceptors);
            this.proxySelector = volleyClient.proxySelector;
            this.socketFactory = volleyClient.socketFactory;
            this.sslSocketFactory = volleyClient.sslSocketFactory;
            this.followSslRedirects = volleyClient.followSslRedirects;
            this.followRedirects = volleyClient.followRedirects;
            this.retryOnConnectionFailure = volleyClient.retryOnConnectionFailure;
            this.connectTimeout = volleyClient.connectTimeout;
            this.readTimeout = volleyClient.readTimeout;
            this.writeTimeout = volleyClient.writeTimeout;
            this.pingInterval = volleyClient.pingInterval;
        }

        public Builder setHurlStack(OkHttpStack okHttpStack) {
            this.okHttpStack = okHttpStack;
            return this;
        }

        public Builder connectTimeout(long connectTimeout, TimeUnit milliseconds) {
            return this;
        }

        public Builder readTimeout(long readTimeout, TimeUnit milliseconds) {
            return this;
        }

        public Builder writeTimeout(long writeTimeout, TimeUnit milliseconds) {
            return this;
        }

        public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            return this;
        }

        public Builder addNetworkInterceptor(Interceptor networkInterceptor) {
            return this;
        }

        public VolleyClient build() {
            return new VolleyClient(this);
        }
    }
}
