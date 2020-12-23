package com.d.lib.aster.integration.http.client;

import android.support.annotation.Nullable;

import com.d.lib.aster.base.Headers;
import com.d.lib.aster.util.Utils;

public final class RealResponse implements Response {
    final int code;
    final String message;
    final Headers headers;
    final @Nullable
    ResponseBody body;
    final Exception exception;

    public RealResponse(int code, String message, Headers headers, @Nullable ResponseBody body,
                        @Nullable Exception exception) {
        this.code = code;
        this.message = message;
        this.headers = headers;
        this.body = body;
        this.exception = exception;
    }

    /**
     * Returns the HTTP status code.
     */
    @Override
    public int code() {
        return code;
    }

    /**
     * Returns true if the code is in [200..300), which means the request was successfully received,
     * understood, and accepted.
     */
    @Override
    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    /**
     * Returns the HTTP status message.
     */
    @Override
    public String message() {
        return message;
    }

    @Override
    public Headers headers() {
        return headers;
    }

    @Nullable
    @Override
    public ResponseBody body() {
        return body;
    }

    @Override
    public void close() {
        Utils.closeQuietly(body);
    }
}
