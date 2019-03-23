package com.d.lib.aster.integration.http.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.MediaType;
import com.d.lib.aster.base.MediaTypes;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.http.HttpClient;
import com.d.lib.aster.integration.http.RequestManagerImpl;
import com.d.lib.aster.integration.http.body.BodyWriter;
import com.d.lib.aster.integration.http.body.MultipartBody;
import com.d.lib.aster.integration.http.body.RequestBody;
import com.d.lib.aster.integration.http.body.UploadProgressRequestBody;
import com.d.lib.aster.integration.http.client.HttpURLApi;
import com.d.lib.aster.integration.http.client.ResponseBody;
import com.d.lib.aster.integration.http.func.ApiRetryFunc;
import com.d.lib.aster.integration.http.observer.UploadObserver;
import com.d.lib.aster.request.IUploadRequest;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.DisposableObserver;
import com.d.lib.aster.scheduler.schedule.Schedulers;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by D on 2017/10/24.
 */
public class UploadRequest extends IUploadRequest<UploadRequest, HttpClient> {
    protected List<MultipartBody.Part> mMultipartBodyParts = new ArrayList<>();
    protected HttpURLConnection mConn;
    protected Observable<ResponseBody> mObservable;

    public UploadRequest(String url) {
        super(url);
    }

    public UploadRequest(String url, Config config) {
        super(url, config);
    }

    @Override
    protected HttpClient getClient() {
        return HttpClient.create(IClient.TYPE_UPLOAD, mConfig.log(false));
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
        final HttpURLApi.Callable callable = getClient().create().upload(mUrl, mMultipartBodyParts);
        mConn = callable.conn;
        mObservable = callable.observable;
    }

    @Override
    public void request() {
        request(null);
    }

    @Override
    public <R> void request(@Nullable SimpleCallback<R> callback) {
        prepare();
        requestImpl(mObservable, getClient().getHttpConfig(),
                mTag, mConn, (SimpleCallback<ResponseBody>) callback);
    }

    private static void requestImpl(final Observable<ResponseBody> observable,
                                    final Config config,
                                    final Object tag,
                                    final HttpURLConnection conn,
                                    final SimpleCallback<ResponseBody> callback) {
        DisposableObserver<ResponseBody> disposableObserver = new UploadObserver(tag, conn, callback);
        if (tag != null) {
            RequestManagerImpl.getIns().add(tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.mainThread())
                .subscribe(new ApiRetryFunc<ResponseBody>(disposableObserver,
                        config.retryCount, config.retryDelayMillis,
                        new ApiRetryFunc.OnRetry<ResponseBody>() {
                            @NonNull
                            @Override
                            public Observable.Observe<ResponseBody> observe() {
                                return observable.subscribeOn(Schedulers.io())
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
    public static class Singleton extends IUploadRequest.Singleton<Singleton, HttpClient> {
        protected List<MultipartBody.Part> mMultipartBodyParts = new ArrayList<>();
        protected HttpURLConnection mConn;
        protected Observable<ResponseBody> mObservable;

        public Singleton(String url) {
            super(url);
        }

        @Override
        protected HttpClient getClient() {
            return HttpClient.getDefault(IClient.TYPE_UPLOAD);
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
            final HttpURLApi.Callable callable = getClient().create().upload(mUrl, mMultipartBodyParts);
            mConn = callable.conn;
            mObservable = callable.observable;
        }

        @Override
        public void request() {
            request(null);
        }

        @Override
        public <R> void request(SimpleCallback<R> callback) {
            prepare();
            requestImpl(mObservable, getClient().getHttpConfig(),
                    mTag, mConn, (SimpleCallback<ResponseBody>) callback);
        }

        @Override
        public Singleton addParam(String paramKey, String paramValue) {
            if (paramKey != null && paramValue != null) {
                this.mParams.put(paramKey, paramValue);
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
