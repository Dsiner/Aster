package com.d.lib.aster.integration.volley.interceptor;

import android.support.annotation.NonNull;

import com.d.lib.aster.integration.volley.client.ResponseRequest;
import com.d.lib.aster.interceptor.Interceptor;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Request header interception
 */
public class HeadersInterceptor extends com.d.lib.aster.interceptor.HeadersInterceptor
        implements Interceptor<ResponseRequest, ResponseRequest> {

    public HeadersInterceptor(Map<String, String> headers) {
        super(headers);
    }

    @Override
    public ResponseRequest intercept(@NonNull ResponseRequest chain) throws IOException {
        if (mHeaders != null && mHeaders.size() > 0) {
            Set<String> keys = mHeaders.keySet();
            for (String headerKey : keys) {
                chain.addHeader(headerKey, mHeaders.get(headerKey));
            }
        }

        if (mOnHeadInterceptor != null) {
            Map<String, String> headers = new LinkedHashMap<>();
            mOnHeadInterceptor.intercept(headers);
            if (headers.size() > 0) {
                Set<String> keys = headers.keySet();
                for (String headerKey : keys) {
                    chain.addHeader(headerKey, headers.get(headerKey));
                }
            }
        }

        return chain;
    }
}
