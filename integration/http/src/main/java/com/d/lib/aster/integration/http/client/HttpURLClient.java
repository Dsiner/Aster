package com.d.lib.aster.integration.http.client;

import android.support.annotation.Nullable;

import com.d.lib.aster.interceptor.IInterceptor;

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
 * HttpURLApi
 * Created by D on 2018/12/8.
 **/
public class HttpURLClient {
    @Nullable
    final Proxy proxy;
    final List<IInterceptor> interceptors;
    final List<IInterceptor> networkInterceptors;
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

    public HttpURLClient() {
        this(new Builder());
    }

    HttpURLClient(Builder builder) {
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

    /**
     * Prepares the {@code request} to be executed at some point in the future.
     */
    public Call newCall(Request request) {
        return new RealCall(this, request);
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    private static int checkDuration(String name, long duration, TimeUnit unit) {
        if (duration < 0) throw new IllegalArgumentException(name + " < 0");
        if (unit == null) throw new NullPointerException("unit == null");
        long millis = unit.toMillis(duration);
        if (millis > Integer.MAX_VALUE) throw new IllegalArgumentException(name + " too large.");
        if (millis == 0 && duration > 0) throw new IllegalArgumentException(name + " too small.");
        return (int) millis;
    }

    public List<IInterceptor> interceptors() {
        return interceptors;
    }

    public List<IInterceptor> networkInterceptors() {
        return networkInterceptors;
    }

    public static final class Builder {
        @Nullable
        Proxy proxy;
        final List<IInterceptor> interceptors = new ArrayList<>();
        final List<IInterceptor> networkInterceptors = new ArrayList<>();
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

        Builder(HttpURLClient httpURLClient) {
            this.proxy = httpURLClient.proxy;
            this.interceptors.addAll(httpURLClient.interceptors);
            this.networkInterceptors.addAll(httpURLClient.networkInterceptors);
            this.proxySelector = httpURLClient.proxySelector;
            this.socketFactory = httpURLClient.socketFactory;
            this.sslSocketFactory = httpURLClient.sslSocketFactory;
            this.followSslRedirects = httpURLClient.followSslRedirects;
            this.followRedirects = httpURLClient.followRedirects;
            this.retryOnConnectionFailure = httpURLClient.retryOnConnectionFailure;
            this.connectTimeout = httpURLClient.connectTimeout;
            this.readTimeout = httpURLClient.readTimeout;
            this.writeTimeout = httpURLClient.writeTimeout;
            this.pingInterval = httpURLClient.pingInterval;
        }

        public Builder connectTimeout(long timeout, TimeUnit unit) {
            this.connectTimeout = checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder readTimeout(long timeout, TimeUnit unit) {
            this.readTimeout = checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder writeTimeout(long timeout, TimeUnit unit) {
            this.writeTimeout = checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
            return this;
        }

        public Builder addInterceptor(IInterceptor interceptor) {
            this.interceptors.add(interceptor);
            return this;
        }

        public Builder addNetworkInterceptor(IInterceptor networkInterceptor) {
            this.networkInterceptors.add(networkInterceptor);
            return this;
        }

        public HttpURLClient build() {
            return new HttpURLClient(this);
        }
    }
}
