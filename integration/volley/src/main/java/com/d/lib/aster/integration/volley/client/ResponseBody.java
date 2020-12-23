package com.d.lib.aster.integration.volley.client;

import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;
import com.d.lib.aster.util.Utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ResponseBody implements Closeable {
    private final InputStream source;
    private final NetworkResponse response;
    private final String charset;

    public ResponseBody(NetworkResponse response) {
        this.response = response;
        this.source = response != null && response.data != null ?
                new ByteArrayInputStream(response.data)
                : null;
        this.charset = response != null && response.data != null ?
                HttpHeaderParser.parseCharset(response.headers)
                : "utf-8";
    }

    public static ResponseBody create(NetworkResponse response) {
        return new ResponseBody(response);
    }

    /**
     * Returns the number of bytes in that will returned by {@link #bytes}, or {@link #byteStream}, or
     * -1 if unknown.
     */
    public long contentLength() {
        return response.data.length;
    }

    public final InputStream byteStream() {
        return source;
    }

    public InputStream source() {
        return source;
    }

    /**
     * Returns the response as a byte array.
     *
     * <p>This method loads entire response body into memory. If the response body is very large this
     * may trigger an {@link OutOfMemoryError}. Prefer to stream the response body if this is a
     * possibility for your response.
     */
    public final byte[] bytes() throws IOException {
        return null;
    }

    /**
     * Returns the response as a string decoded with the charset of the Content-Type header. If that
     * header is either absent or lacks a charset, this will attempt to decode the response body in
     * accordance to <a href="https://en.wikipedia.org/wiki/Byte_order_mark">its BOM</a> or UTF-8.
     * Closes {@link ResponseBody} automatically.
     *
     * <p>This method loads entire response body into memory. If the response body is very large this
     * may trigger an {@link OutOfMemoryError}. Prefer to stream the response body if this is a
     * possibility for your response.
     */
    public final String string() {
        String buf;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(source, charset()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            buf = sb.toString();
            return buf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            Utils.closeQuietly(source);
        }
    }

    @Deprecated
    public final String string2() {
        String data;
        try {
            data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            data = new String(response.data);
        }
        return data;
    }

    private String charset() {
        return charset;
    }

    @Override
    public void close() throws IOException {
        Utils.closeQuietly(source);
    }
}
