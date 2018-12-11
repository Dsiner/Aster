package com.d.lib.aster.integration.volley.client;

import android.support.annotation.Nullable;

import java.io.Closeable;

public interface Response extends Closeable {

    /**
     * Returns the HTTP status code.
     */
    int code();

    /**
     * Returns true if the code is in [200..300), which means the request was successfully received,
     * understood, and accepted.
     */
    boolean isSuccessful();

    /**
     * Returns the HTTP status message.
     */
    String message();

    @Nullable
    ResponseBody body();
}
