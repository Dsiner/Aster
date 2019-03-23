package com.d.lib.aster.integration.volley.client;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.d.lib.aster.base.MediaType;
import com.d.lib.aster.integration.volley.body.RequestBody;
import com.d.lib.aster.utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ResponseRequest
 * Created by D on 2018/12/12.
 **/
public class ResponseRequest extends GsonRequest<RealResponse> {
    protected Map<String, String> mHeaders = new LinkedHashMap<>();
    protected String mContentType;
    protected RequestBody mRequestBody;

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
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    @Override
    public String getBodyContentType() {
        return mContentType != null ? mContentType : super.getBodyContentType();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return super.getParams();
    }

    @Override
    public byte[] getPostBody() throws AuthFailureError {
        return getMethod() == Method.POST ? getBody() : super.getPostBody();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mRequestBody != null) {
            ByteArrayOutputStream byteArrayOutputStream = null;
            DataOutputStream dataOutputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                mRequestBody.writeTo(dataOutputStream);
                dataOutputStream.flush();
                return byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                Util.closeQuietly(dataOutputStream);
                Util.closeQuietly(byteArrayOutputStream);
            }
        }
        return super.getBody();
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

    public void addHeader(String key, String value) {
        mHeaders.put(key, value);
    }

    public void setRequestBody(RequestBody requestBody) {
        if (requestBody == null) {
            return;
        }
        this.mRequestBody = requestBody;
        MediaType contentType = requestBody.contentType();
        if (contentType != null
                && !TextUtils.isEmpty(contentType.toString())) {
            this.mContentType = contentType.toString();
            addHeader("Content-Type", contentType.toString());
        }
    }
}
