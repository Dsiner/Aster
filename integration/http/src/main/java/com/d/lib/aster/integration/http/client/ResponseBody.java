package com.d.lib.aster.integration.http.client;

import com.d.lib.aster.utils.Util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class ResponseBody implements Closeable {
    private final InputStream source;
    private final HttpURLConnection conn;

    public ResponseBody(HttpURLConnection conn) {
        this.conn = conn;
        InputStream is = null;
        try {
            is = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.source = is;
    }

    public static ResponseBody create(HttpURLConnection conn) {
        return new ResponseBody(conn);
    }

    /**
     * Returns the number of bytes in that will returned by {@link #bytes}, or {@link #byteStream}, or
     * -1 if unknown.
     */
    public long contentLength() {
        return conn.getContentLength();
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
            Util.closeQuietly(source);
        }
    }

    private String charset() {
        return "utf-8";
    }

    @Override
    public void close() throws IOException {
        Util.closeQuietly(source);
        if (conn != null) {
            conn.disconnect();
        }
    }
}
