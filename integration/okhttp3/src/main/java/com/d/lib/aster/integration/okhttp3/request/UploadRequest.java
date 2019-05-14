package com.d.lib.aster.integration.okhttp3.request;

import android.support.annotation.NonNull;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.callback.UploadCallback;
import com.d.lib.aster.integration.okhttp3.MediaTypes;
import com.d.lib.aster.integration.okhttp3.OkHttpApi;
import com.d.lib.aster.integration.okhttp3.OkHttpClient;
import com.d.lib.aster.integration.okhttp3.body.InputStreamRequestBody;
import com.d.lib.aster.integration.okhttp3.body.UploadProgressRequestBody;
import com.d.lib.aster.integration.okhttp3.func.ApiTransformer;
import com.d.lib.aster.request.IUploadRequest;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.utils.Util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by D on 2017/10/24.
 */
public class UploadRequest extends IUploadRequest<UploadRequest, OkHttpClient> {
    protected List<MultipartBody.Part> mMultipartBodyParts = new ArrayList<>();
    protected Call mCall;
    protected Observable<Response> mObservable;
    protected UploadCallback mUploadCallback;

    public UploadRequest(String url) {
        super(url);
    }

    public UploadRequest(String url, Config config) {
        super(url, config);
    }

    @Override
    protected OkHttpClient getClient() {
        return OkHttpClient.create(IClient.TYPE_UPLOAD, mConfig.log(false));
    }

    @Override
    protected void prepare() {
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (MultipartBody.Part part : mMultipartBodyParts) {
            builder.addPart(part);
        }
        final RequestBody requestBody;
        if (mUploadCallback != null) {
            requestBody = new UploadProgressRequestBody(builder.build(), mUploadCallback);
        } else {
            requestBody = builder.build();
        }
        final OkHttpApi.Callable callable = getClient().create().postBody(mUrl, requestBody);
        mCall = callable.call;
        mObservable = callable.observable;
    }

    @Override
    public <T> void request(@NonNull UploadCallback<T> callback) {
        mUploadCallback = callback;
        prepare();
        ApiTransformer.requestUpload(mCall, mObservable, mConfig, callback, mTag);
    }

    @Override
    public UploadRequest addParam(String paramKey, String paramValue) {
        if (paramKey != null && paramValue != null) {
            mParams.put(paramKey, paramValue);
            mMultipartBodyParts.add(MultipartBody.Part.createFormData(paramKey, paramValue));
        }
        return this;
    }

    @Override
    public UploadRequest addParam(Params params) {
        if (params != null && params.size() > 0) {
            mParams.putAll(params);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                mMultipartBodyParts.add(MultipartBody.Part.createFormData(entry.getKey(),
                        entry.getValue()));
            }
        }
        return this;
    }

    @Override
    public UploadRequest addFile(String name, String filename, File file) {
        return addFile(name, filename, file, null);
    }

    @Override
    public UploadRequest addFile(String name, String filename, File file, ProgressCallback callback) {
        final RequestBody requestBody = RequestBody.create(MediaType.parse(Util.guessMimeType(file.getName())), file);
        final MultipartBody.Part part = callback != null
                ? MultipartBody.Part.createFormData(name, filename,
                new UploadProgressRequestBody(requestBody, callback))
                : MultipartBody.Part.createFormData(name, filename, requestBody);
        mMultipartBodyParts.add(part);
        return this;
    }

    @Override
    public UploadRequest addImageFile(String name, String filename, File file) {
        return addImageFile(name, filename, file, null);
    }

    @Override
    public UploadRequest addImageFile(String name, String filename, File file, ProgressCallback callback) {
        final RequestBody requestBody = RequestBody.create(MediaTypes.IMAGE_TYPE, file);
        final MultipartBody.Part part = callback != null
                ? MultipartBody.Part.createFormData(name, filename,
                new UploadProgressRequestBody(requestBody, callback))
                : MultipartBody.Part.createFormData(name, filename, requestBody);
        mMultipartBodyParts.add(part);
        return this;
    }

    @Override
    public UploadRequest addBytes(String name, String filename, byte[] bytes) {
        return addBytes(name, filename, bytes, null);
    }

    @Override
    public UploadRequest addBytes(String name, String filename, byte[] bytes, ProgressCallback callback) {
        final RequestBody requestBody = RequestBody.create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, bytes);
        final MultipartBody.Part part = callback != null
                ? MultipartBody.Part.createFormData(name, filename,
                new UploadProgressRequestBody(requestBody, callback))
                : MultipartBody.Part.createFormData(name, filename, requestBody);
        mMultipartBodyParts.add(part);
        return this;
    }

    @Override
    public UploadRequest addStream(String name, String filename, InputStream inputStream) {
        return addStream(name, filename, inputStream, null);
    }

    @Override
    public UploadRequest addStream(String name, String filename, InputStream inputStream, ProgressCallback callback) {
        final RequestBody requestBody = InputStreamRequestBody.create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, inputStream);
        final MultipartBody.Part part = callback != null
                ? MultipartBody.Part.createFormData(name, filename,
                new UploadProgressRequestBody(requestBody, callback))
                : MultipartBody.Part.createFormData(name, filename, requestBody);
        mMultipartBodyParts.add(part);
        return this;
    }

    @Override
    public UploadRequest addPart(Object part) {
        mMultipartBodyParts.add((MultipartBody.Part) part);
        return this;
    }

    /**
     * Singleton
     */
    public static class Singleton extends IUploadRequest.Singleton<Singleton, OkHttpClient> {
        protected List<MultipartBody.Part> mMultipartBodyParts = new ArrayList<>();
        protected Call mCall;
        protected Observable<Response> mObservable;
        protected UploadCallback mUploadCallback;

        public Singleton(String url) {
            super(url);
        }

        @Override
        protected OkHttpClient getClient() {
            return OkHttpClient.getDefault(IClient.TYPE_UPLOAD);
        }

        @Override
        protected void prepare() {
            final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (MultipartBody.Part part : mMultipartBodyParts) {
                builder.addPart(part);
            }
            final RequestBody requestBody;
            if (mUploadCallback != null) {
                requestBody = new UploadProgressRequestBody(builder.build(), mUploadCallback);
            } else {
                requestBody = builder.build();
            }
            final OkHttpApi.Callable callable = getClient().create().postBody(mUrl, requestBody);
            mCall = callable.call;
            mObservable = callable.observable;
        }

        @Override
        public <T> void request(UploadCallback<T> callback) {
            mUploadCallback = callback;
            prepare();
            ApiTransformer.requestUpload(mCall, mObservable, getClient().getHttpConfig(),
                    callback, mTag);
        }

        @Override
        public Singleton addParam(String paramKey, String paramValue) {
            if (paramKey != null && paramValue != null) {
                mParams.put(paramKey, paramValue);
                mMultipartBodyParts.add(MultipartBody.Part.createFormData(paramKey, paramValue));
            }
            return this;
        }

        @Override
        public Singleton addParam(Params params) {
            if (params != null && params.size() > 0) {
                mParams.putAll(params);
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    mMultipartBodyParts.add(MultipartBody.Part.createFormData(entry.getKey(),
                            entry.getValue()));
                }
            }
            return this;
        }

        @Override
        public Singleton addFile(String name, String filename, File file) {
            return addFile(name, filename, file, null);
        }

        @Override
        public Singleton addFile(String name, String filename, File file, ProgressCallback callback) {
            final RequestBody requestBody = RequestBody.create(MediaType.parse(Util.guessMimeType(file.getName())), file);
            final MultipartBody.Part part = callback != null
                    ? MultipartBody.Part.createFormData(name, filename,
                    new UploadProgressRequestBody(requestBody, callback))
                    : MultipartBody.Part.createFormData(name, filename, requestBody);
            mMultipartBodyParts.add(part);
            return this;
        }

        @Override
        public Singleton addImageFile(String name, String filename, File file) {
            return addImageFile(name, filename, file, null);
        }

        @Override
        public Singleton addImageFile(String name, String filename, File file, ProgressCallback callback) {
            final RequestBody requestBody = RequestBody.create(MediaTypes.IMAGE_TYPE, file);
            final MultipartBody.Part part = callback != null
                    ? MultipartBody.Part.createFormData(name, filename,
                    new UploadProgressRequestBody(requestBody, callback))
                    : MultipartBody.Part.createFormData(name, filename, requestBody);
            mMultipartBodyParts.add(part);
            return this;
        }

        @Override
        public Singleton addBytes(String name, String filename, byte[] bytes) {
            return addBytes(name, filename, bytes, null);
        }

        @Override
        public Singleton addBytes(String name, String filename, byte[] bytes, ProgressCallback callback) {
            final RequestBody requestBody = RequestBody.create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, bytes);
            final MultipartBody.Part part = callback != null
                    ? MultipartBody.Part.createFormData(name, filename,
                    new UploadProgressRequestBody(requestBody, callback))
                    : MultipartBody.Part.createFormData(name, filename, requestBody);
            mMultipartBodyParts.add(part);
            return this;
        }

        @Override
        public Singleton addStream(String name, String filename, InputStream inputStream) {
            return addStream(name, filename, inputStream, null);
        }

        @Override
        public Singleton addStream(String name, String filename, InputStream inputStream, ProgressCallback callback) {
            final RequestBody requestBody = InputStreamRequestBody.create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, inputStream);
            final MultipartBody.Part part = callback != null
                    ? MultipartBody.Part.createFormData(name, filename,
                    new UploadProgressRequestBody(requestBody, callback))
                    : MultipartBody.Part.createFormData(name, filename, requestBody);
            mMultipartBodyParts.add(part);
            return this;
        }

        @Override
        public Singleton addPart(Object part) {
            mMultipartBodyParts.add((MultipartBody.Part) part);
            return this;
        }
    }
}
