package com.d.lib.aster.integration.http.interceptor;


import com.d.lib.aster.integration.http.client.Request;
import com.d.lib.aster.integration.http.client.Response;
import com.d.lib.aster.interceptor.IInterceptor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * A concrete interceptor chain that carries the entire interceptor chain: all application
 * interceptors, the OkHttp core, all network interceptors, and finally the network caller.
 */
public final class RealInterceptorChain implements Chain {
    private final List<IInterceptor<Chain, Response>> mInterceptors;
    private final HttpURLConnection mConnection;
    private final int mIndex;
    private final Request mRequest;
    private int mCalls;

    public RealInterceptorChain(List<IInterceptor<Chain, Response>> interceptors,
                                HttpURLConnection connection, int index, Request request) {
        this.mInterceptors = interceptors;
        this.mConnection = connection;
        this.mIndex = index;
        this.mRequest = request;
    }

    @Override
    public HttpURLConnection connection() {
        return mConnection;
    }

    @Override
    public Request request() {
        return mRequest;
    }

    @Override
    public Response proceed(Request request) throws IOException {
        return proceed(request, mConnection);
    }

    public Response proceed(Request request, HttpURLConnection connection) throws IOException {
        if (mIndex >= mInterceptors.size()) throw new AssertionError();

        mCalls++;

        // Call the next interceptor in the chain.
        RealInterceptorChain next = new RealInterceptorChain(mInterceptors, connection, mIndex + 1, request);
        IInterceptor<Chain, Response> interceptor = mInterceptors.get(mIndex);
        Response response = interceptor.intercept(next);

        // Confirm that the next interceptor made its required call to chain.proceed().
        if (mIndex + 1 < mInterceptors.size() && next.mCalls != 1) {
            throw new IllegalStateException("network interceptor " + interceptor
                    + " must call proceed() exactly once");
        }

        // Confirm that the intercepted response isn't null.
        if (response == null) {
            throw new NullPointerException("interceptor " + interceptor + " returned null");
        }

        return response;
    }
}