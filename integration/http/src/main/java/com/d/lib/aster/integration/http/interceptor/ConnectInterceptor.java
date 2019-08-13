package com.d.lib.aster.integration.http.interceptor;

import android.support.annotation.Nullable;

import com.d.lib.aster.integration.http.client.HttpURLClient;
import com.d.lib.aster.integration.http.client.Request;
import com.d.lib.aster.integration.http.client.Response;
import com.d.lib.aster.interceptor.IInterceptor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class ConnectInterceptor implements IInterceptor<Chain, Response> {
    private final HttpURLClient mClient;

    public ConnectInterceptor(HttpURLClient client) {
        this.mClient = client;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        RealInterceptorChain realChain = (RealInterceptorChain) chain;
        Request request = realChain.request();
        HttpURLConnection connection = getHttpURLConnection(request);
        if (connection == null) {
            throw new IOException("HttpURLConnection is null.");
        }
        return realChain.proceed(request, connection);
    }

    @Nullable
    private HttpURLConnection getHttpURLConnection(Request request) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(request.url());
            if ("https".equals(url.getProtocol())) {
                HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
                connHttps.setSSLSocketFactory(mClient.sslSocketFactory);
                connHttps.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true; // Default authentication
                    }
                });
                conn = connHttps;
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setConnectTimeout(mClient.connectTimeout);
            conn.setReadTimeout(mClient.readTimeout);
            conn.setInstanceFollowRedirects(mClient.followRedirects);
            conn.setUseCaches(false);
            conn.setRequestMethod(request.method());
            return conn;
        } catch (Exception e) {
            if (conn != null) {
                conn.disconnect();
            }
            return null;
        }
    }
}
