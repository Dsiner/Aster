package com.d.lib.rxnet.request;

import android.content.Context;

import com.d.lib.rxnet.api.RetrofitAPI;
import com.d.lib.rxnet.base.RetrofitClient;

import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;

/**
 * Instance
 * Created by D on 2017/10/24.
 */
public class UploadRequest extends BaseRequest<UploadRequest> {
    protected List<MultipartBody.Part> parts;

    public UploadRequest(Context context, String url, List<MultipartBody.Part> parts) {
        this.context = context;
        this.url = url;
        this.parts = parts;
    }

    protected void init() {
        observable = RetrofitClient.getInstance(context).create(RetrofitAPI.class).upload(url, parts);
    }

    @Override
    protected UploadRequest baseUrl(String baseUrl) {
        return this;
    }

    @Override
    protected UploadRequest headers(Map<String, String> headers) {
        return this;
    }

    @Override
    protected UploadRequest connectTimeout(long timeout) {
        return this;
    }

    @Override
    protected UploadRequest readTimeout(long timeout) {
        return this;
    }

    @Override
    protected UploadRequest writeTimeout(long timeout) {
        return this;
    }

    @Override
    protected UploadRequest addInterceptor(Interceptor interceptor) {
        return this;
    }

    @Override
    protected UploadRequest sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        return this;
    }

    @Override
    protected UploadRequest retryCount(int retryCount) {
        return this;
    }

    @Override
    protected UploadRequest retryDelayMillis(long retryDelayMillis) {
        return this;
    }

    /**
     * New
     */
    public static class UploadRequestF extends UploadRequest {

        public UploadRequestF(Context context, String url, List<MultipartBody.Part> parts) {
            super(context, url, parts);
        }

        @Override
        protected void init() {
            observable = RetrofitClient.getRetrofit(context, config).create(RetrofitAPI.class).upload(url, parts);
        }
    }
}
