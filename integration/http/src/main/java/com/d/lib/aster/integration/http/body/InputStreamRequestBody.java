package com.d.lib.aster.integration.http.body;

import com.d.lib.aster.base.MediaType;
import com.d.lib.aster.integration.http.sink.BufferedSink;
import com.d.lib.aster.util.Utils;

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
    public void writeTo(BufferedSink sink) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            sink.write(buffer, 0, length);
        }
        sink.flush();
        Utils.closeQuietly(inputStream);
    }
}
