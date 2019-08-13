package com.d.lib.aster.integration.http.client;

import com.d.lib.aster.base.Headers;
import com.d.lib.aster.base.MediaType;
import com.d.lib.aster.utils.Util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

public class ResponseBody implements Closeable {
    private final Headers mHeaders;
    private final HttpURLConnection mConn;
    private final InputStream mInputStream;
    private final ByteArrayOutputStream mByteArrayOutputStream;

    public ResponseBody(Headers headers, HttpURLConnection conn, boolean isTransfer) {
        this.mHeaders = headers;
        this.mConn = conn;
        InputStream is = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            String gzip = headers.get("Content-Encoding");
            if (gzip != null && gzip.toLowerCase().contains("gzip")) {
                // gzip
                is = new GZIPInputStream(conn.getInputStream());
            } else {
                is = conn.getInputStream();
            }
            if (!isTransfer) {
                byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024 * 4];
                int len;
                while ((len = is.read(buffer)) > -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                byteArrayOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mInputStream = is;
        this.mByteArrayOutputStream = byteArrayOutputStream;
    }

    public static ResponseBody create(Headers headers, HttpURLConnection conn, boolean isTransfer) {
        return new ResponseBody(headers, conn, isTransfer);
    }

    public MediaType contentType() {
        String contentType = mHeaders.get("Content-Type");
        return contentType != null ? MediaType.parse(contentType) : null;
    }

    /**
     * Returns the number of bytes in that will returned by {@link #bytes}, or {@link #byteStream}, or
     * -1 if unknown.
     */
    public long contentLength() {
        return mConn.getContentLength();
    }

    public final InputStream byteStream() {
        return mInputStream;
    }

    public InputStream source() {
        return mByteArrayOutputStream != null
                ? new ByteArrayInputStream(mByteArrayOutputStream.toByteArray())
                : mInputStream;
    }

    /**
     * Returns the response as a byte array.
     *
     * <p>This method loads entire response body into memory. If the response body is very large this
     * may trigger an {@link OutOfMemoryError}. Prefer to stream the response body if this is a
     * possibility for your response.
     */
    public final byte[] bytes() throws IOException {
        long contentLength = contentLength();
        if (contentLength > Integer.MAX_VALUE) {
            throw new IOException("Cannot buffer entire body for content length: " + contentLength);
        }

        byte[] bytes;
        InputStream inputStream = source();
        try {
            bytes = toByteArray(inputStream);
        } finally {
            Util.closeQuietly(mInputStream);
        }
        if (contentLength != -1 && contentLength != bytes.length) {
            throw new IOException("Content-Length ("
                    + contentLength
                    + ") and stream length ("
                    + bytes.length
                    + ") disagree");
        }
        return bytes;
    }


    private byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
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
        BufferedReader reader = null;
        InputStream inputStream = source();
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, charset()));
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
            Util.closeQuietly(reader);
            Util.closeQuietly(mInputStream);
        }
    }

    private Charset charset() {
        MediaType contentType = contentType();
        return contentType != null ? contentType.charset(Util.UTF_8) : Util.UTF_8;
    }

    @Override
    public void close() throws IOException {
        Util.closeQuietly(mInputStream);
        if (mConn != null) {
            mConn.disconnect();
        }
    }
}
