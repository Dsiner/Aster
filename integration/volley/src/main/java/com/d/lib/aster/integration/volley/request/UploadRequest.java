package com.d.lib.aster.integration.volley.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.MediaType;
import com.d.lib.aster.base.MediaTypes;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.volley.RequestManagerImpl;
import com.d.lib.aster.integration.volley.VolleyClient;
import com.d.lib.aster.integration.volley.body.BodyWriter;
import com.d.lib.aster.integration.volley.body.MultipartBody;
import com.d.lib.aster.integration.volley.body.RequestBody;
import com.d.lib.aster.integration.volley.body.UploadProgressRequestBody;
import com.d.lib.aster.integration.volley.client.ResponseBody;
import com.d.lib.aster.integration.volley.func.ApiFunc;
import com.d.lib.aster.integration.volley.func.ApiRetryFunc;
import com.d.lib.aster.integration.volley.observer.UploadObserver;
import com.d.lib.aster.request.IUploadRequest;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.DisposableObserver;
import com.d.lib.aster.scheduler.schedule.Schedulers;
import com.d.lib.aster.utils.Util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by D on 2017/10/24.
 */
@Deprecated
public class UploadRequest extends IUploadRequest<UploadRequest, VolleyClient> {
    protected List<MultipartBody.Part> mMultipartBodyParts = new ArrayList<>();
    protected Observable<ResponseBody> mObservable;

    public UploadRequest(String url) {
        super(url);
    }

    public UploadRequest(String url, Config config) {
        super(url, config);
    }

    @Override
    protected VolleyClient getClient() {
        return VolleyClient.create(IClient.TYPE_UPLOAD, mConfig.log(false));
    }

    @Override
    protected void prepare() {
        if (mParams != null && mParams.size() > 0) {
            List<MultipartBody.Part> formParts = new ArrayList<>();
            Iterator<Map.Entry<String, String>> entryIterator = mParams.entrySet().iterator();
            Map.Entry<String, String> entry;
            while (entryIterator.hasNext()) {
                entry = entryIterator.next();
                if (entry != null) {
                    formParts.add(MultipartBody.Part.createFormData(entry.getKey(), entry.getValue()));
                }
            }
            mMultipartBodyParts.addAll(0, formParts);
        }
        mObservable = getClient().create().upload(mUrl, mMultipartBodyParts);
    }

    @Override
    public void request() {
        request(null);
    }

    @Override
    public <T> void request(@Nullable SimpleCallback<T> callback) {
        prepare();
        requestImpl(mObservable, getClient().getHttpConfig(), mTag, callback);
    }

    private static <T> void requestImpl(final Observable<ResponseBody> observable,
                                        final Config config,
                                        final Object tag,
                                        final SimpleCallback<T> callback) {
        DisposableObserver<T> disposableObserver = new UploadObserver<T>(tag, callback);
        if (tag != null) {
            RequestManagerImpl.getIns().add(tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(Util.getFirstCls(callback)))
                .observeOn(Schedulers.mainThread())
                .subscribe(new ApiRetryFunc<T>(disposableObserver,
                        config.retryCount,
                        config.retryDelayMillis,
                        new ApiRetryFunc.OnRetry<T>() {
                            @NonNull
                            @Override
                            public Observable.Observe<T> observe() {
                                return observable.subscribeOn(Schedulers.io())
                                        .map(new ApiFunc<T>(Util.getFirstCls(callback)))
                                        .observeOn(Schedulers.mainThread());
                            }
                        }));
    }

    @Override
    public UploadRequest addParam(String paramKey, String paramValue) {
        if (paramKey != null && paramValue != null) {
            this.mParams.put(paramKey, paramValue);
        }
        return this;
    }

    @Override
    public UploadRequest addParam(Params params) {
        if (params != null) {
            this.mParams.putAll(params);
        }
        return this;
    }

    @Override
    public UploadRequest addFile(String key, File file) {
        return addFile(key, file, null);
    }

    @Override
    public UploadRequest addFile(String key, File file, ProgressCallback callback) {
        if (key == null || file == null) {
            return this;
        }
        RequestBody requestBody = RequestBody.create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, file);
        if (callback != null) {
            UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, callback);
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), uploadProgressRequestBody);
            this.mMultipartBodyParts.add(part);
        } else {
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
            this.mMultipartBodyParts.add(part);
        }
        return this;
    }

    @Override
    public UploadRequest addImageFile(String key, File file) {
        return addImageFile(key, file, null);
    }

    @Override
    public UploadRequest addImageFile(String key, File file, ProgressCallback callback) {
        if (key == null || file == null) {
            return this;
        }
        RequestBody requestBody = RequestBody.create(MediaTypes.IMAGE_TYPE, file);
        if (callback != null) {
            UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, callback);
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), uploadProgressRequestBody);
            this.mMultipartBodyParts.add(part);
        } else {
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
            this.mMultipartBodyParts.add(part);
        }
        return this;
    }

    @Override
    public UploadRequest addBytes(String key, byte[] bytes, String name) {
        return addBytes(key, bytes, name, null);
    }

    @Override
    public UploadRequest addBytes(String key, byte[] bytes, String name, ProgressCallback callback) {
        if (key == null || bytes == null || name == null) {
            return this;
        }
        RequestBody requestBody = RequestBody.create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, bytes);
        if (callback != null) {
            UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, callback);
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, uploadProgressRequestBody);
            this.mMultipartBodyParts.add(part);
        } else {
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, requestBody);
            this.mMultipartBodyParts.add(part);
        }
        return this;
    }

    @Override
    public UploadRequest addStream(String key, InputStream inputStream, String name) {
        return addStream(key, inputStream, name, null);
    }

    @Override
    public UploadRequest addStream(String key, InputStream inputStream, String name, ProgressCallback callback) {
        if (key == null || inputStream == null || name == null) {
            return this;
        }
        RequestBody requestBody = create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, inputStream);
        if (callback != null) {
            UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, callback);
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, uploadProgressRequestBody);
            this.mMultipartBodyParts.add(part);
        } else {
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, requestBody);
            this.mMultipartBodyParts.add(part);
        }
        return this;
    }

    private static RequestBody create(final MediaType mediaType, final InputStream inputStream) {
        return new RequestBody() {
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
            public void writeTo(DataOutputStream sink) throws IOException {
                BodyWriter.writeInputStream(inputStream, sink, null);
            }
        };
    }


    /**
     * Singleton
     */
    @Deprecated
    public static class Singleton extends IUploadRequest.Singleton<Singleton, VolleyClient> {
        protected List<MultipartBody.Part> mMultipartBodyParts = new ArrayList<>();
        protected Observable<ResponseBody> mObservable;

        public Singleton(String url) {
            super(url);
        }

        @Override
        protected VolleyClient getClient() {
            return VolleyClient.getDefault(IClient.TYPE_UPLOAD);
        }

        @Override
        protected void prepare() {
            if (mParams != null && mParams.size() > 0) {
                List<MultipartBody.Part> formParts = new ArrayList<>();
                Iterator<Map.Entry<String, String>> entryIterator = mParams.entrySet().iterator();
                Map.Entry<String, String> entry;
                while (entryIterator.hasNext()) {
                    entry = entryIterator.next();
                    if (entry != null) {
                        formParts.add(MultipartBody.Part.createFormData(entry.getKey(), entry.getValue()));
                    }
                }
                mMultipartBodyParts.addAll(0, formParts);
            }
            mObservable = getClient().create().upload(mUrl, mMultipartBodyParts);
        }

        @Override
        public void request() {
            request(null);
        }

        @Override
        public <R> void request(SimpleCallback<R> callback) {
            prepare();
            requestImpl(mObservable, getClient().getHttpConfig(),
                    mTag, (SimpleCallback<ResponseBody>) callback);
        }

        @Override
        public Singleton addParam(String paramKey, String paramValue) {
            if (paramKey != null && paramValue != null) {
                this.mParams.put(paramKey, paramValue);
            }
            return this;
        }

        @Override
        public Singleton addParam(Params params) {
            if (params != null) {
                this.mParams.putAll(params);
            }
            return this;
        }

        @Override
        public Singleton addFile(String key, File file) {
            return addFile(key, file, null);
        }

        @Override
        public Singleton addFile(String key, File file, ProgressCallback callback) {
            if (key == null || file == null) {
                return this;
            }
            RequestBody requestBody = RequestBody.create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, file);
            if (callback != null) {
                UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, callback);
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), uploadProgressRequestBody);
                this.mMultipartBodyParts.add(part);
            } else {
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
                this.mMultipartBodyParts.add(part);
            }
            return this;
        }

        @Override
        public Singleton addImageFile(String key, File file) {
            return addImageFile(key, file, null);
        }

        @Override
        public Singleton addImageFile(String key, File file, ProgressCallback callback) {
            if (key == null || file == null) {
                return this;
            }
            RequestBody requestBody = RequestBody.create(MediaTypes.IMAGE_TYPE, file);
            if (callback != null) {
                UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, callback);
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), uploadProgressRequestBody);
                this.mMultipartBodyParts.add(part);
            } else {
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
                this.mMultipartBodyParts.add(part);
            }
            return this;
        }

        @Override
        public Singleton addBytes(String key, byte[] bytes, String name) {
            return addBytes(key, bytes, name, null);
        }

        @Override
        public Singleton addBytes(String key, byte[] bytes, String name, ProgressCallback callback) {
            if (key == null || bytes == null || name == null) {
                return this;
            }
            RequestBody requestBody = RequestBody.create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, bytes);
            if (callback != null) {
                UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, callback);
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, uploadProgressRequestBody);
                this.mMultipartBodyParts.add(part);
            } else {
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, requestBody);
                this.mMultipartBodyParts.add(part);
            }
            return this;
        }

        @Override
        public Singleton addStream(String key, InputStream inputStream, String name) {
            return addStream(key, inputStream, name, null);
        }

        @Override
        public Singleton addStream(String key, InputStream inputStream, String name, ProgressCallback callback) {
            if (key == null || inputStream == null || name == null) {
                return this;
            }
            RequestBody requestBody = create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, inputStream);
            if (callback != null) {
                UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, callback);
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, uploadProgressRequestBody);
                this.mMultipartBodyParts.add(part);
            } else {
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, requestBody);
                this.mMultipartBodyParts.add(part);
            }
            return this;
        }
    }
}
