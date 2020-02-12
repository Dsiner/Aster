package com.d.lib.aster.integration.volley.body;

import com.d.lib.aster.base.MediaType;
import com.d.lib.aster.integration.volley.sink.BufferedSink;
import com.d.lib.aster.utils.Util;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class FormBody extends RequestBody {

    private static final MediaType CONTENT_TYPE =
            MediaType.parse("application/x-www-form-urlencoded");

    private final Map<String, String> params;

    FormBody(Map<String, String> params) {
        this.params = params;
    }

    /**
     * The number of key-value pairs in this form-encoded body.
     */
    public int size() {
        return params.size();
    }

    @Override
    public MediaType contentType() {
        return CONTENT_TYPE;
    }

    @Override
    public long contentLength() {
        return toString().getBytes(Util.UTF_8).length;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (i > 0) {
                sink.writeUtf8("&");
            }
            sink.writeUtf8(entry.getKey());
            sink.writeUtf8("=");
            sink.writeUtf8(entry.getValue());
            i++;
        }
        sink.flush();
    }

    @Override
    public String toString() {
        StringBuilder param = new StringBuilder();
        if (size() > 0) {
            Iterator ite = params.entrySet().iterator();
            while (ite.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) ite.next();
                String key = entry.getKey();
                String value = entry.getValue();
                param.append(key + "=" + value + "&");
            }
            param.deleteCharAt(param.length() - 1);
        }
        return param.toString();
    }

    public static final class Builder {
        private final Map<String, String> params = new LinkedHashMap<>();

        public Builder add(String key, String value) {
            params.put(key, value);
            return this;
        }

        public FormBody build() {
            return new FormBody(params);
        }
    }
}
