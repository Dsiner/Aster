package com.d.lib.aster.integration.volley.client;

import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * ResponseRequest
 * Created by D on 2018/12/12.
 **/
public class ResponseRequest extends GsonRequest<RealResponse> {

    public ResponseRequest(String url,
                           Response.Listener<RealResponse> listener,
                           Response.ErrorListener errorListener) {
        super(url, null, listener, errorListener);
    }

    public ResponseRequest(int method, String url,
                           Response.Listener<RealResponse> listener,
                           Response.ErrorListener errorListener) {
        super(method, url, null, listener, errorListener);
    }

    @Override
    protected Response<RealResponse> parseNetworkResponse(NetworkResponse response) {
        RealResponse parsed;
        parsed = getRealResponse(response);
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    private RealResponse getRealResponse(@Nullable NetworkResponse response) {
        if (response != null) {
            return new RealResponse(response.statusCode,
                    "" + response.statusCode,
                    ResponseBody.create(response),
                    null);
        }
        return new RealResponse(-1, "", null, null);
    }

    public void addHeader(String headerKey, String s) {
        // TODO: @dsiner Imp... 2018/12/12
    }
}
