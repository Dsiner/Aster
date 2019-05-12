package com.d.lib.aster.integration.volley.body;

import com.d.lib.aster.base.MediaType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
    public void writeTo(DataOutputStream sink) throws IOException {
        BodyWriter.writeInputStream(inputStream, sink, null);
    }
}
