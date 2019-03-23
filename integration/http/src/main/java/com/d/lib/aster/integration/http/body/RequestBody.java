package com.d.lib.aster.integration.http.body;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.d.lib.aster.base.MediaType;
import com.d.lib.aster.utils.Util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public abstract class RequestBody {

    /**
     * Returns the Content-Type header for this body.
     */
    public abstract @Nullable
    MediaType contentType();

    /**
     * Returns the number of bytes that will be written to {@code out} in a call to {@link #writeTo},
     * or -1 if that count is unknown.
     */
    public long contentLength() throws IOException {
        return -1;
    }

    /**
     * Writes the content of this request to {@code out}.
     */
    public abstract void writeTo(DataOutputStream sink) throws IOException;

    /**
     * Writes the content of this request to {@code out}.
     */
    public void writeAll(ForwardingSink sink) throws IOException {

    }

    /**
     * Returns a new request body that transmits {@code content}. If {@code contentType} is non-null
     * and lacks a charset, this will use UTF-8.
     */
    public static RequestBody create(@Nullable MediaType contentType, String content) {
        Charset charset = Util.UTF_8;
        if (contentType != null) {
            charset = contentType.charset();
            if (charset == null) {
                charset = Util.UTF_8;
                contentType = MediaType.parse(contentType + "; charset=utf-8");
            }
        }
        byte[] bytes = content.getBytes(charset);
        return create(contentType, bytes);
    }

    /**
     * Returns a new request body that transmits {@code content}.
     */
    public static RequestBody create(final @Nullable MediaType contentType, final byte[] content) {
        return create(contentType, content, 0, content.length);
    }

    /**
     * Returns a new request body that transmits {@code content}.
     */
    public static RequestBody create(final @Nullable MediaType contentType, final byte[] content,
                                     final int offset, final int byteCount) {
        if (content == null) {
            throw new NullPointerException("content == null");
        }
        return new RequestBody() {
            @Override
            public @Nullable
            MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return byteCount;
            }

            @Override
            public void writeTo(DataOutputStream sink) throws IOException {
                sink.write(content, offset, byteCount);
            }
        };
    }

    /**
     * Returns a new request body that transmits the content of {@code file}.
     */
    public static RequestBody create(final @Nullable MediaType contentType, final File file) {
        if (file == null) throw new NullPointerException("content == null");

        return new RequestBody() {
            @Override
            public @Nullable
            MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeAll(final ForwardingSink sink) throws IOException {
                FileInputStream source = null;
                try {
                    source = new FileInputStream(file);
                    byte[] buffer = new byte[1024 * 2];
                    int length;
                    DataOutputStream outputStream = sink.getDataOutputStream();
                    while ((length = source.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                        sink.write(outputStream, length);
                    }
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    Util.closeQuietly(source);
                }
            }

            @Override
            public void writeTo(final DataOutputStream sink) throws IOException {
                writeAll(new ForwardingSink() {
                    @Override
                    public DataOutputStream getDataOutputStream() {
                        return sink;
                    }

                    @Override
                    public void write(@NonNull DataOutputStream source, long byteCount) throws IOException {

                    }
                });
            }
        };
    }

    public interface ForwardingSink {
        DataOutputStream getDataOutputStream();

        @SuppressLint("CheckResult")
        void write(@NonNull DataOutputStream source, long byteCount) throws IOException;
    }
}

