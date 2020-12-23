package com.d.lib.aster.integration.http.client;

import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.http.interceptor.BridgeInterceptor;
import com.d.lib.aster.integration.http.interceptor.CallServerInterceptor;
import com.d.lib.aster.integration.http.interceptor.Chain;
import com.d.lib.aster.integration.http.interceptor.ConnectInterceptor;
import com.d.lib.aster.integration.http.interceptor.RealInterceptorChain;
import com.d.lib.aster.interceptor.IInterceptor;
import com.d.lib.aster.util.ULog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class RealCall implements Call {
    final HttpURLClient mClient;

    /**
     * The application's original request unadulterated by redirects or auth headers.
     */
    final Request mOriginalRequest;

    // Guarded by this.
    private boolean mExecuted;
    private volatile boolean mCanceled;

    RealCall(HttpURLClient client, Request originalRequest) {
        this.mClient = client;
        this.mOriginalRequest = originalRequest;
    }

    @Override
    public Request request() {
        return mOriginalRequest;
    }

    @Override
    public Response execute() throws IOException {
        synchronized (this) {
            if (mExecuted) {
                throw new IllegalStateException("Already Executed");
            }
            mExecuted = true;
        }
        Response result = getResponseWithInterceptorChain();
        if (result == null) {
            throw new IOException("Canceled");
        }
        return result;
    }

    @Override
    public void enqueue(SimpleCallback<Response> responseCallback) {
        synchronized (this) {
            if (mExecuted) {
                throw new IllegalStateException("Already Executed");
            }
            mExecuted = true;
        }
        new AsyncCall(responseCallback).execute();
    }

    @Override
    public void cancel() {
        mCanceled = true;
    }

    @Override
    public synchronized boolean isExecuted() {
        return mExecuted;
    }

    @Override
    public boolean isCanceled() {
        return mCanceled;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    // We are a final type & this saves clearing state.
    @Override
    public RealCall clone() {
        return new RealCall(mClient, mOriginalRequest);
    }

    final class AsyncCall {
        private final SimpleCallback<Response> responseCallback;

        AsyncCall(SimpleCallback<Response> responseCallback) {
            this.responseCallback = responseCallback;
        }

        String url() {
            return mOriginalRequest.url();
        }

        Request request() {
            return mOriginalRequest;
        }

        RealCall get() {
            return RealCall.this;
        }

        protected void execute() {
            boolean signalledCallback = false;
            try {
                Response response = getResponseWithInterceptorChain();
                if (isCanceled()) {
                    signalledCallback = true;
                    responseCallback.onError(new IOException("Canceled"));
                } else {
                    signalledCallback = true;
                    responseCallback.onSuccess(response);
                }
            } catch (IOException e) {
                if (signalledCallback) {
                    // Do not signal the callback twice!
                    e.printStackTrace();
                    ULog.i("Callback failure for " + toLoggableString());
                } else {
                    responseCallback.onError(e);
                }
            }
        }
    }

    /**
     * Returns a string that describes this call. Doesn't include a full URL as that might contain
     * sensitive information.
     */
    String toLoggableString() {
        return (isCanceled() ? "canceled " : "")
                + "call"
                + " to " + redactedUrl();
    }

    String redactedUrl() {
        return mOriginalRequest.url();
    }

    Response getResponseWithInterceptorChain() throws IOException {
        // Build a full stack of interceptors.
        List<IInterceptor<Chain, Response>> interceptors = new ArrayList<>();
        for (IInterceptor interceptor : mClient.interceptors()) {
            interceptors.add(interceptor);
        }
        interceptors.add(new BridgeInterceptor(mClient));
        interceptors.add(new ConnectInterceptor(mClient));
        for (IInterceptor interceptor : mClient.networkInterceptors()) {
            interceptors.add(interceptor);
        }
        interceptors.add(new CallServerInterceptor(mClient));

        Chain chain = new RealInterceptorChain(interceptors, null, 0, mOriginalRequest);
        return chain.proceed(mOriginalRequest);
    }
}