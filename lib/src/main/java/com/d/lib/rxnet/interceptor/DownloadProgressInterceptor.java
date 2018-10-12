package com.d.lib.rxnet.interceptor;

import android.support.annotation.NonNull;

import com.d.lib.rxnet.body.DownloadProgressRequestBody;
import com.d.lib.rxnet.callback.DownloadCallback;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Download progress interception
 */
public class DownloadProgressInterceptor implements Interceptor {
    private DownloadCallback callback;

    public DownloadProgressInterceptor(DownloadCallback callback) {
        if (callback == null) {
            throw new NullPointerException("This callback must not be null.");
        }
        this.callback = callback;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (originalRequest.body() == null) {
            return chain.proceed(originalRequest);
        }
        Request progressRequest = originalRequest.newBuilder()
                .method(originalRequest.method(), new DownloadProgressRequestBody(originalRequest.body(), callback))
                .build();
        return chain.proceed(progressRequest);
    }
}
