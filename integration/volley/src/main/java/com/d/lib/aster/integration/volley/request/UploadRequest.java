package com.d.lib.aster.integration.volley.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.IRequest;
import com.d.lib.aster.base.MediaType;
import com.d.lib.aster.base.MediaTypes;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.volley.RequestManager;
import com.d.lib.aster.integration.volley.VolleyClient;
import com.d.lib.aster.integration.volley.body.BodyWriter;
import com.d.lib.aster.integration.volley.body.MultipartBody;
import com.d.lib.aster.integration.volley.body.RequestBody;
import com.d.lib.aster.integration.volley.body.UploadProgressRequestBody;
import com.d.lib.aster.integration.volley.client.ResponseBody;
import com.d.lib.aster.integration.volley.func.ApiRetryFunc;
import com.d.lib.aster.integration.volley.observer.UploadObserver;
import com.d.lib.aster.interceptor.IHeadersInterceptor;
import com.d.lib.aster.interceptor.IInterceptor;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.DisposableObserver;
import com.d.lib.aster.scheduler.schedule.Schedulers;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by D on 2017/10/24.
 */
public class UploadRequest extends IRequest<UploadRequest, VolleyClient> {
    protected List<MultipartBody.Part> mMultipartBodyParts = new ArrayList<>();
    protected Observable<ResponseBody> mObservable;

    public UploadRequest(String url) {
        this(url, null);
    }

    public UploadRequest(String url, Config config) {
        this.mUrl = url;
        this.mParams = new Params();
        this.mConfig = config != null ? config : Config.getDefault();
    }

    @Override
    protected VolleyClient getClient() {
        return VolleyClient.create(IClient.TYPE_UPLOAD, mConfig.log(false));
    }

    protected void prepare() {
        if (mParams != null && mParams.size() > 0) {
            Iterator<Map.Entry<String, String>> entryIterator = mParams.entrySet().iterator();
            Map.Entry<String, String> entry;
            while (entryIterator.hasNext()) {
                entry = entryIterator.next();
                if (entry != null) {
                    mMultipartBodyParts.add(MultipartBody.Part.createFormData(entry.getKey(), entry.getValue()));
                }
            }
        }
        mObservable = getClient().create().upload(mUrl, mMultipartBodyParts);
    }

    public void request() {
        request(null);
    }

    public void request(@Nullable SimpleCallback<ResponseBody> callback) {
        prepare();
        requestImpl(mObservable, getClient().getHttpConfig(), mTag, callback);
    }

    @Override
    public UploadRequest baseUrl(String baseUrl) {
        return super.baseUrl(baseUrl);
    }

    @Override
    public UploadRequest headers(Map<String, String> headers) {
        return super.headers(headers);
    }

    @Override
    public UploadRequest headers(IHeadersInterceptor.OnHeadInterceptor onHeadInterceptor) {
        return super.headers(onHeadInterceptor);
    }

    @Override
    public UploadRequest connectTimeout(long timeout) {
        return super.connectTimeout(timeout);
    }

    @Override
    public UploadRequest readTimeout(long timeout) {
        return super.readTimeout(timeout);
    }

    @Override
    public UploadRequest writeTimeout(long timeout) {
        return super.writeTimeout(timeout);
    }

    @Override
    public UploadRequest sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        return super.sslSocketFactory(sslSocketFactory);
    }

    @Override
    public UploadRequest addInterceptor(IInterceptor interceptor) {
        return super.addInterceptor(interceptor);
    }

    @Override
    public UploadRequest addNetworkInterceptors(IInterceptor interceptor) {
        return super.addNetworkInterceptors(interceptor);
    }

    @Override
    public UploadRequest retryCount(int retryCount) {
        return super.retryCount(retryCount);
    }

    @Override
    public UploadRequest retryDelayMillis(long retryDelayMillis) {
        return super.retryDelayMillis(retryDelayMillis);
    }

    private static void requestImpl(final Observable<ResponseBody> observable,
                                    final Config config,
                                    final Object tag,
                                    final SimpleCallback<ResponseBody> callback) {
        DisposableObserver<ResponseBody> disposableObserver = new UploadObserver(tag, callback);
        if (tag != null) {
            RequestManager.getIns().add(tag, disposableObserver);
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

    public UploadRequest addParam(String paramKey, String paramValue) {
        if (paramKey != null && paramValue != null) {
            this.mParams.put(paramKey, paramValue);
        }
        return this;
    }

    public UploadRequest addFile(String key, File file) {
        return addFile(key, file, null);
    }

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

    public UploadRequest addImageFile(String key, File file) {
        return addImageFile(key, file, null);
    }

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

    public UploadRequest addBytes(String key, byte[] bytes, String name) {
        return addBytes(key, bytes, name, null);
    }

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

    public UploadRequest addStream(String key, InputStream inputStream, String name) {
        return addStream(key, inputStream, name, null);
    }

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
    public static class Singleton extends IRequest<Singleton, VolleyClient> {
        protected List<MultipartBody.Part> multipartBodyParts = new ArrayList<>();
        protected Observable<ResponseBody> mObservable;

        public Singleton(String url) {
            this.mUrl = url;
            this.mParams = new Params();
        }

        @Override
        protected VolleyClient getClient() {
            return VolleyClient.getDefault(IClient.TYPE_UPLOAD);
        }

        protected void prepare() {
            if (mParams != null && mParams.size() > 0) {
                Iterator<Map.Entry<String, String>> entryIterator = mParams.entrySet().iterator();
                Map.Entry<String, String> entry;
                while (entryIterator.hasNext()) {
                    entry = entryIterator.next();
                    if (entry != null) {
                        multipartBodyParts.add(MultipartBody.Part.createFormData(entry.getKey(), entry.getValue()));
                    }
                }
            }
            mObservable = getClient().create().upload(mUrl, multipartBodyParts);
        }

        public void request() {
            request(null);
        }

        public void request(@Nullable SimpleCallback<ResponseBody> callback) {
            prepare();
            requestImpl(mObservable, getClient().getHttpConfig(), mTag, callback);
        }

        public Singleton addParam(String paramKey, String paramValue) {
            if (paramKey != null && paramValue != null) {
                this.mParams.put(paramKey, paramValue);
            }
            return this;
        }

        public Singleton addFile(String key, File file) {
            return addFile(key, file, null);
        }

        public Singleton addFile(String key, File file, ProgressCallback callback) {
            if (key == null || file == null) {
                return this;
            }
            RequestBody requestBody = RequestBody.create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, file);
            if (callback != null) {
                UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, callback);
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), uploadProgressRequestBody);
                this.multipartBodyParts.add(part);
            } else {
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
                this.multipartBodyParts.add(part);
            }
            return this;
        }

        public Singleton addImageFile(String key, File file) {
            return addImageFile(key, file, null);
        }

        public Singleton addImageFile(String key, File file, ProgressCallback callback) {
            if (key == null || file == null) {
                return this;
            }
            RequestBody requestBody = RequestBody.create(MediaTypes.IMAGE_TYPE, file);
            if (callback != null) {
                UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, callback);
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), uploadProgressRequestBody);
                this.multipartBodyParts.add(part);
            } else {
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
                this.multipartBodyParts.add(part);
            }
            return this;
        }

        public Singleton addBytes(String key, byte[] bytes, String name) {
            return addBytes(key, bytes, name, null);
        }

        public Singleton addBytes(String key, byte[] bytes, String name, ProgressCallback callback) {
            if (key == null || bytes == null || name == null) {
                return this;
            }
            RequestBody requestBody = RequestBody.create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, bytes);
            if (callback != null) {
                UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, callback);
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, uploadProgressRequestBody);
                this.multipartBodyParts.add(part);
            } else {
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, requestBody);
                this.multipartBodyParts.add(part);
            }
            return this;
        }

        public Singleton addStream(String key, InputStream inputStream, String name) {
            return addStream(key, inputStream, name, null);
        }

        public Singleton addStream(String key, InputStream inputStream, String name, ProgressCallback callback) {
            if (key == null || inputStream == null || name == null) {
                return this;
            }
            RequestBody requestBody = create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, inputStream);
            if (callback != null) {
                UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, callback);
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, uploadProgressRequestBody);
                this.multipartBodyParts.add(part);
            } else {
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, requestBody);
                this.multipartBodyParts.add(part);
            }
            return this;
        }
    }
}
