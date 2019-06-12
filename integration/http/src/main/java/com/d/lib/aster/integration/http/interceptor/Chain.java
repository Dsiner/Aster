package com.d.lib.aster.integration.http.interceptor;

import android.support.annotation.Nullable;

import com.d.lib.aster.integration.http.client.Request;
import com.d.lib.aster.integration.http.client.Response;

import java.io.IOException;
import java.net.HttpURLConnection;

public interface Chain {
    Request request();

    Response proceed(Request request) throws IOException;

    /**
     * Returns the connection the request will be executed on. This is only available in the chains
     * of network interceptors; for application interceptors this is always null.
     */
    @Nullable
    HttpURLConnection connection();
}
