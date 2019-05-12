package com.d.lib.aster.integration.okhttp3.body;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class InputStreamRequestBody extends RequestBody {
    private final MediaType mediaType;
    private final InputStream inputStream;

    public static RequestBody create(final MediaType mediaType, final InputStream inputStream) {
        return new InputStreamRequestBody(mediaType, inputStream);
    }

    public InputStreamRequestBody(MediaType mediaType, InputStream inputStream) {
        this.mediaType = mediaType;
        this.inputStream = inputStream;
    }

    @Override
    public MediaType contentType() {
        return mediaType;
    }

    @Override
    public long contentLength() {
        try {
            return inputStream.available();
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(inputStream);
            sink.writeAll(source);
        } finally {
            okhttp3.internal.Util.closeQuietly(source);
        }
    }
}
