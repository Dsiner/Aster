package com.d.lib.aster.integration.okhttp3.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Request header interception
 */
public class HeadersInterceptor extends com.d.lib.aster.interceptor.HeadersInterceptor
        implements Interceptor, com.d.lib.aster.interceptor.Interceptor<Interceptor.Chain, Response> {

    public HeadersInterceptor(Map<String, String> headers) {
        super(headers);
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (mHeaders != null && mHeaders.size() > 0) {
            Set<String> keys = mHeaders.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, mHeaders.get(headerKey));
            }
        }

        if (mOnHeadInterceptor != null) {
            Map<String, String> headers = new LinkedHashMap<>();
            mOnHeadInterceptor.intercept(headers);
            if (headers.size() > 0) {
                Set<String> keys = headers.keySet();
                for (String headerKey : keys) {
                    builder.addHeader(headerKey, headers.get(headerKey));
                }
            }
        }

        return chain.proceed(builder.build());
    }
}
