package com.d.lib.aster.integration.retrofit.request;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.callback.UploadCallback;
import com.d.lib.aster.integration.okhttp3.MediaTypes;
import com.d.lib.aster.integration.okhttp3.body.InputStreamRequestBody;
import com.d.lib.aster.integration.okhttp3.body.UploadProgressRequestBody;
import com.d.lib.aster.integration.retrofit.RetrofitAPI;
import com.d.lib.aster.integration.retrofit.RetrofitClient;
import com.d.lib.aster.integration.retrofit.func.ApiTransformer;
import com.d.lib.aster.request.IUploadRequest;
import com.d.lib.aster.utils.Util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by D on 2017/10/24.
 */
public class UploadRequest extends IUploadRequest<UploadRequest, RetrofitClient> {
    protected List<MultipartBody.Part> mMultipartBodyParts = new ArrayList<>();
    protected Observable<ResponseBody> mObservable;
    protected UploadCallback mUploadCallback;

    public UploadRequest(String url) {
        super(url);
    }

    public UploadRequest(String url, Config config) {
        super(url, config);
    }

    @Override
    protected RetrofitClient getClient() {
        return RetrofitClient.create(IClient.TYPE_UPLOAD, mConfig.log(false));
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
        mObservable = getClient().getClient().create(RetrofitAPI.class).postBody(mUrl, requestBody);
    }

    @Override
    public <T> void request(UploadCallback<T> callback) {
        mUploadCallback = callback;
        prepare();
        ApiTransformer.requestUpload(mObservable, mConfig,
                mMultipartBodyParts, callback, mTag);
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
    public UploadRequest addFile(String name, File file) {
        return addFile(name, file, null);
    }

    @Override
    public UploadRequest addFile(String name, File file, ProgressCallback callback) {
        final RequestBody requestBody = RequestBody.create(MediaType.parse(Util.guessMimeType(file.getName())), file);
        final MultipartBody.Part part = callback != null
                ? MultipartBody.Part.createFormData(name, file.getName(),
                new UploadProgressRequestBody(requestBody, callback))
                : MultipartBody.Part.createFormData(name, file.getName(), requestBody);
        mMultipartBodyParts.add(part);
        return this;
    }

    @Override
    public UploadRequest addImageFile(String name, File file) {
        return addImageFile(name, file, null);
    }

    @Override
    public UploadRequest addImageFile(String name, File file, ProgressCallback callback) {
        final RequestBody requestBody = RequestBody.create(MediaTypes.IMAGE_TYPE, file);
        final MultipartBody.Part part = callback != null
                ? MultipartBody.Part.createFormData(name, file.getName(),
                new UploadProgressRequestBody(requestBody, callback))
                : MultipartBody.Part.createFormData(name, file.getName(), requestBody);
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

    /**
     * Singleton
     */
    public static class Singleton extends IUploadRequest.Singleton<Singleton, RetrofitClient> {
        protected List<MultipartBody.Part> mMultipartBodyParts = new ArrayList<>();
        protected Observable<ResponseBody> mObservable;
        protected UploadCallback mUploadCallback;

        public Singleton(String url) {
            super(url);
        }

        @Override
        protected RetrofitClient getClient() {
            return RetrofitClient.getDefault(IClient.TYPE_UPLOAD);
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
            mObservable = getClient().getClient().create(RetrofitAPI.class).postBody(mUrl, requestBody);
        }

        @Override
        public <T> void request(UploadCallback<T> callback) {
            mUploadCallback = callback;
            prepare();
            ApiTransformer.requestUpload(mObservable, getClient().getHttpConfig(),
                    mMultipartBodyParts, callback, mTag);
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
        public Singleton addFile(String name, File file) {
            return addFile(name, file, null);
        }

        @Override
        public Singleton addFile(String name, File file, ProgressCallback callback) {
            final RequestBody requestBody = RequestBody.create(MediaType.parse(Util.guessMimeType(file.getName())), file);
            final MultipartBody.Part part = callback != null
                    ? MultipartBody.Part.createFormData(name, file.getName(),
                    new UploadProgressRequestBody(requestBody, callback))
                    : MultipartBody.Part.createFormData(name, file.getName(), requestBody);
            mMultipartBodyParts.add(part);
            return this;
        }

        @Override
        public Singleton addImageFile(String name, File file) {
            return addImageFile(name, file, null);
        }

        @Override
        public Singleton addImageFile(String name, File file, ProgressCallback callback) {
            final RequestBody requestBody = RequestBody.create(MediaTypes.IMAGE_TYPE, file);
            final MultipartBody.Part part = callback != null
                    ? MultipartBody.Part.createFormData(name, file.getName(),
                    new UploadProgressRequestBody(requestBody, callback))
                    : MultipartBody.Part.createFormData(name, file.getName(), requestBody);
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
    }
}
