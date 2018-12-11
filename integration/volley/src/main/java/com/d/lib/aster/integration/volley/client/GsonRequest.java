package com.d.lib.aster.integration.volley.client;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

/**
 * GsonRequest
 * Created by D on 2018/12/12.
 **/
public class GsonRequest<T> extends Request<T> {

    /**
     * Lock to guard mListener as it is cleared on cancel() and read on delivery.
     */
    final Object mLock = new Object();
    final Class<T> mClazz;

    // @GuardedBy("mLock")
    Response.Listener<T> mListener;

    /**
     * Creates a new GET request.
     *
     * @param url           URL to fetch the string at
     * @param listener      Listener to receive the T response
     * @param errorListener Error listener, or null to ignore errors
     */
    public GsonRequest(String url, Class<T> clazz,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener);
    }

    /**
     * Creates a new request with the given method.
     *
     * @param method        the request {@link Method} to use
     * @param url           URL to fetch the string at
     * @param listener      Listener to receive the T response
     * @param errorListener Error listener, or null to ignore errors
     */
    public GsonRequest(int method, String url, Class<T> clazz,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mClazz = clazz;
    }

    @Override
    public void cancel() {
        super.cancel();
        synchronized (mLock) {
            mListener = null;
        }
    }

    @Override
    protected void deliverResponse(T response) {
        Response.Listener<T> listener;
        synchronized (mLock) {
            listener = mListener;
        }
        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        T parsed;
        String data;
        try {
            data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            data = new String(response.data);
        }
        parsed = apply(data, mClazz);
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @SuppressWarnings("unchecked")
    T apply(String responseBody, Class<T> clazz) {
        Gson gson = new Gson();
        if (clazz.equals(String.class)) {
            return (T) responseBody;
        } else {
            return gson.fromJson(responseBody, clazz);
        }
    }
}
