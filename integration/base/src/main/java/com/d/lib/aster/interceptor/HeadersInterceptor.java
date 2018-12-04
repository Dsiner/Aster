package com.d.lib.aster.interceptor;

import java.util.Map;

/**
 * Request header interception
 */
public abstract class HeadersInterceptor {
    protected Map<String, String> mHeaders;
    protected OnHeadInterceptor mOnHeadInterceptor;

    protected HeadersInterceptor(Map<String, String> headers) {
        this.mHeaders = headers;
    }

    public interface OnHeadInterceptor {

        /**
         * Some parameters may be dynamic, such as tokens, etc. You shoule override here
         * builder.addHeader("token", "")
         */
        void intercept(Map<String, String> heads);
    }

    public HeadersInterceptor setOnHeadInterceptor(OnHeadInterceptor onHeadInterceptor) {
        this.mOnHeadInterceptor = onHeadInterceptor;
        return this;
    }
}
