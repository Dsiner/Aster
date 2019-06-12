package com.d.lib.aster.integration.http.interceptor;

import android.support.annotation.Nullable;

import com.d.lib.aster.integration.http.client.HttpURLClient;
import com.d.lib.aster.integration.http.client.Request;
import com.d.lib.aster.integration.http.client.Response;
import com.d.lib.aster.interceptor.IInterceptor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectInterceptor implements IInterceptor<Chain, Response> {
    private final HttpURLClient mClient;
    private final int mConnectTimeout;
    private final int mReadTimeout;
    private final int mWriteTimeout;

    public ConnectInterceptor(HttpURLClient client,
                              int connectTimeout,
                              int readTimeout,
                              int writeTimeout) {
        this.mClient = client;
        this.mConnectTimeout = connectTimeout;
        this.mReadTimeout = readTimeout;
        this.mWriteTimeout = writeTimeout;
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
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(mConnectTimeout);
            conn.setReadTimeout(mReadTimeout);
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
