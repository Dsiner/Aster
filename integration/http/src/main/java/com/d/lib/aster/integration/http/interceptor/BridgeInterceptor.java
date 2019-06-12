package com.d.lib.aster.integration.http.interceptor;

import com.d.lib.aster.base.MediaType;
import com.d.lib.aster.integration.http.body.RequestBody;
import com.d.lib.aster.integration.http.client.HttpURLClient;
import com.d.lib.aster.integration.http.client.Request;
import com.d.lib.aster.integration.http.client.Response;
import com.d.lib.aster.interceptor.IInterceptor;

import java.io.IOException;

/**
 * Bridges from application code to network code. First it builds a network request from a user
 * request. Then it proceeds to call the network. Finally it builds a user response from the network
 * response.
 */
public final class BridgeInterceptor implements IInterceptor<Chain, Response> {
    private final HttpURLClient mClient;

    public BridgeInterceptor(HttpURLClient client) {
        this.mClient = client;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request userRequest = chain.request();
        Request.Builder requestBuilder = userRequest.newBuilder();

        RequestBody body = userRequest.body();
        if (body != null) {
            MediaType contentType = body.contentType();
            if (contentType != null) {
                requestBuilder.header("Content-Type", contentType.toString());
            }

//            long contentLength = body.contentLength();
//            if (contentLength != -1) {
//                requestBuilder.header("Content-Length", Long.toString(contentLength));
//                requestBuilder.removeHeader("Transfer-Encoding");
//            } else {
//                requestBuilder.header("Transfer-Encoding", "chunked");
//                requestBuilder.removeHeader("Content-Length");
//            }
        }

        if (userRequest.header("Connection") == null) {
            requestBuilder.header("Connection", "Keep-Alive");
        }

        // If we add an "Accept-Encoding: gzip" header field we're responsible for also decompressing
        // the transfer stream.
        boolean transparentGzip = false;
        if (userRequest.header("Accept-Encoding") == null && userRequest.header("Range") == null) {
            transparentGzip = true;
            requestBuilder.header("Accept-Encoding", "gzip");
        }

        Response networkResponse = chain.proceed(requestBuilder.build());

        return networkResponse;
    }
}