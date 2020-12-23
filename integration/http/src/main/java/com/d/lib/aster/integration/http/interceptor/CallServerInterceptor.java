package com.d.lib.aster.integration.http.interceptor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.d.lib.aster.base.Headers;
import com.d.lib.aster.integration.http.body.MultipartBody;
import com.d.lib.aster.integration.http.body.RequestBody;
import com.d.lib.aster.integration.http.client.HttpURLClient;
import com.d.lib.aster.integration.http.client.RealResponse;
import com.d.lib.aster.integration.http.client.Request;
import com.d.lib.aster.integration.http.client.Response;
import com.d.lib.aster.integration.http.client.ResponseBody;
import com.d.lib.aster.integration.http.sink.BufferedSink;
import com.d.lib.aster.interceptor.IInterceptor;
import com.d.lib.aster.util.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Set;

public class CallServerInterceptor implements IInterceptor<Chain, Response> {
    private final HttpURLClient mClient;

    public CallServerInterceptor(HttpURLClient client) {
        this.mClient = client;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        RealInterceptorChain realChain = (RealInterceptorChain) chain;
        HttpURLConnection connection = chain.connection();
        if (connection == null) {
            throw new IOException("HttpURLConnection is null.");
        }
        Request request = realChain.request();
        RequestBody requestBody = request.body();

        // Connection settings
        final boolean withBody = requestBody != null && requestBody.contentType() != null;
        if (withBody) {
            connection.setDoInput(true);
            connection.setDoOutput(true);
            return executeBody(realChain);
        } else {
            connection.setDoInput(true);
            return execute(realChain);
        }
    }

    @NonNull
    private RealResponse execute(Chain chain) {
        HttpURLConnection conn = chain.connection();
        if (conn == null) {
            return getRealResponse(new NullPointerException("HttpURLConnection is null."));
        }
        setHeaders(conn, chain.request().headers());
        try {
            return getRealResponse(conn, chain.request().isTransfer());
        } catch (Exception e) {
            e.printStackTrace();
            conn.disconnect();
            return getRealResponse(e);
        }
    }

    @NonNull
    private RealResponse executeBody(Chain chain) {
        HttpURLConnection conn = chain.connection();
        RequestBody requestBody = chain.request().body();
        if (conn == null) {
            return getRealResponse(new NullPointerException("HttpURLConnection is null."));
        }
        setHeaders(conn, chain.request().headers());
        DataOutputStream outputStream = null;
        try {
            outputStream = new DataOutputStream(conn.getOutputStream());
            requestBody.writeTo(new BufferedSink(outputStream));
            outputStream.flush();
            return getRealResponse(conn, chain.request().isTransfer());
        } catch (Exception e) {
            e.printStackTrace();
            conn.disconnect();
            return getRealResponse(e);
        } finally {
            Utils.closeQuietly(outputStream);
        }
    }

    @NonNull
    private RealResponse executeMultipartBody(@Nullable HttpURLConnection conn,
                                              List<MultipartBody.Part> multipartBodyParts) {
        if (conn == null) {
            return getRealResponse(new NullPointerException("HttpURLConnection is null."));
        }
        DataOutputStream outputStream = null;
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (MultipartBody.Part part : multipartBodyParts) {
                builder.addPart(part);
            }
            RequestBody requestBody = builder.build();
            outputStream = new DataOutputStream(conn.getOutputStream());
            requestBody.writeTo(new BufferedSink(outputStream));
            outputStream.flush();
            return getRealResponse(conn, false);
        } catch (Exception e) {
            e.printStackTrace();
            conn.disconnect();
            return getRealResponse(e);
        } finally {
            Utils.closeQuietly(outputStream);
        }
    }

    private void setHeaders(HttpURLConnection conn, Headers headers) {
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String name : keys) {
                conn.setRequestProperty(name, headers.get(name));
            }
        }
    }

    private Headers getHttpResponseHeader(HttpURLConnection conn) {
        Headers header = new Headers();
        for (int i = 0; ; i++) {
            String mine = conn.getHeaderField(i);
            if (mine == null) {
                break;
            }
            header.put(conn.getHeaderFieldKey(i), mine);
        }
        return header;
    }

    private RealResponse getRealResponse(@Nullable HttpURLConnection conn, boolean isTransfer) {
        if (conn != null) {
            try {
                Headers headers;
                return new RealResponse(conn.getResponseCode(),
                        conn.getResponseMessage(),
                        headers = getHttpResponseHeader(conn),
                        ResponseBody.create(headers, conn, isTransfer),
                        null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new RealResponse(-1, "", new Headers(), null, null);
    }

    private RealResponse getRealResponse(@Nullable Exception e) {
        return new RealResponse(-1, "", new Headers(), null, e);
    }
}
