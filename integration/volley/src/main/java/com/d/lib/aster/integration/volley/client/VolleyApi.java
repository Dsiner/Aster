package com.d.lib.aster.integration.volley.client;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.d.lib.aster.base.MediaType;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.integration.volley.body.FormBody;
import com.d.lib.aster.integration.volley.body.MultipartBody;
import com.d.lib.aster.integration.volley.body.RequestBody;
import com.d.lib.aster.interceptor.IInterceptor;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.Task;
import com.d.lib.aster.utils.Util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * VolleyApi
 * Created by D on 2018/12/7.
 **/
public class VolleyApi {
    private Impl mImpl;

    public VolleyApi(VolleyClient client) {
        this.mImpl = new Impl(client);
    }

    public Impl getImpl() {
        return mImpl;
    }

    public Observable<ResponseBody> get(String url, Params params) {
        return get(url + "?" + params.getRequestParamsString());
    }

    public Observable<ResponseBody> get(final String url) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().getImpl(url);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> post(final String url) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().postImpl(url);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> post(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().postImpl(url, params);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> postBody(final String url, final RequestBody requestBody) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().postBodyImpl(url, requestBody);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> put(final String url) {
        return put(url, null);
    }

    public Observable<ResponseBody> put(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().putImpl(url, params);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> putBody(final String url, final RequestBody requestBody) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().putBodyImpl(url, requestBody);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> head(final String url) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().headImpl(url);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> delete(final String url) {
        return delete(url, null);
    }

    public Observable<ResponseBody> delete(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().deleteImpl(url, params);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> deleteBody(final String url, final RequestBody requestBody) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().deleteBodyImpl(url, requestBody);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> options(final String url) {
        return options(url, null);
    }

    public Observable<ResponseBody> options(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().optionsImpl(url, params);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> optionsBody(final String url, final RequestBody requestBody) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().optionsBodyImpl(url, requestBody);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> patch(final String url) {
        return patch(url, null);
    }

    public Observable<ResponseBody> patch(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().patchImpl(url, params);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> patchBody(final String url, final RequestBody requestBody) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().patchBodyImpl(url, requestBody);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> download(final String url) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().downloadImpl(url);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> download(final String url, final Params params) {
        return download(url + "?" + params.getRequestParamsString());
    }

    public Observable<ResponseBody> upload(final String url, final List<MultipartBody.Part> multipartBodyParts) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().uploadImpl(url, multipartBodyParts);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    private void checkSuccessful(@NonNull Response response) throws Exception {
        if (!response.isSuccessful()) {
            if (((RealResponse) response).exception != null) {
                throw ((RealResponse) response).exception;
            }
            String message = ((RealResponse) response).message;
            throw new NetworkErrorException(!TextUtils.isEmpty(message) ?
                    "Request is not successful for " + message
                    : "Request is not successful.");
        }
    }

    static class Impl {
        private VolleyClient mClient;

        private Impl(VolleyClient client) {
            this.mClient = client;
        }

        private RequestFuture<RealResponse> getImpl(final String url)
                throws IOException {
            return getImpl(url, null);
        }

        private RequestFuture<RealResponse> getImpl(final String url, final Params params)
                throws IOException {
            final String realUrl = params != null ? url + "?" + params.getRequestParamsString() : url;
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.GET, realUrl,
                    requestFuture, requestFuture);
            intercept(request);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        private RequestFuture<RealResponse> postImpl(final String url)
                throws IOException {
            return postImpl(url, null);
        }

        private RequestFuture<RealResponse> postImpl(final String url, final Params params)
                throws IOException {
            final RequestBody requestBody = getRequestBody(params);
            return postBodyImpl(url, requestBody);
        }

        private RequestFuture<RealResponse> postBodyImpl(final String url, final RequestBody requestBody)
                throws IOException {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.POST, url,
                    requestFuture, requestFuture);
            request.setRequestBody(requestBody);
            intercept(request);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        private RequestFuture<RealResponse> putImpl(final String url, final Params params)
                throws IOException {
            final RequestBody requestBody = getRequestBody(params);
            return putBodyImpl(url, requestBody);
        }

        private RequestFuture<RealResponse> putBodyImpl(final String url, final RequestBody requestBody)
                throws IOException {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.PUT, url,
                    requestFuture, requestFuture);
            request.setRequestBody(requestBody);
            intercept(request);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        private RequestFuture<RealResponse> headImpl(final String url)
                throws IOException {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.HEAD, url,
                    requestFuture, requestFuture);
            intercept(request);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        private RequestFuture<RealResponse> deleteImpl(final String url, final Params params)
                throws IOException {
            final RequestBody requestBody = getRequestBody(params);
            return deleteBodyImpl(url, requestBody);
        }

        private RequestFuture<RealResponse> deleteBodyImpl(final String url, final RequestBody requestBody)
                throws IOException {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.DELETE, url,
                    requestFuture, requestFuture);
            request.setRequestBody(requestBody);
            intercept(request);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        private RequestFuture<RealResponse> optionsImpl(final String url, final Params params)
                throws IOException {
            final RequestBody requestBody = getRequestBody(params);
            return optionsBodyImpl(url, requestBody);
        }

        private RequestFuture<RealResponse> optionsBodyImpl(final String url, final RequestBody requestBody)
                throws IOException {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.OPTIONS, url,
                    requestFuture, requestFuture);
            request.setRequestBody(requestBody);
            intercept(request);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        private RequestFuture<RealResponse> patchImpl(final String url, final Params params)
                throws IOException {
            final RequestBody requestBody = getRequestBody(params);
            return patchBodyImpl(url, requestBody);
        }

        private RequestFuture<RealResponse> patchBodyImpl(final String url, final RequestBody requestBody)
                throws IOException {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.PATCH, url,
                    requestFuture, requestFuture);
            request.setRequestBody(requestBody);
            intercept(request);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        private RequestFuture<RealResponse> downloadImpl(final String url)
                throws IOException {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.GET, url,
                    requestFuture, requestFuture);
            intercept(request);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        private RequestFuture<RealResponse> uploadImpl(final String url,
                                                       final List<MultipartBody.Part> multipartBodyParts)
                throws IOException {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.POST, url,
                    requestFuture, requestFuture);
            intercept(request);
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (MultipartBody.Part part : multipartBodyParts) {
                builder.addPart(part);
            }
            RequestBody requestBody = builder.build();
            MediaType contentType = requestBody.contentType();
            if (contentType != null
                    && !TextUtils.isEmpty(contentType.toString())) {
                request.addHeader("Content-Type", contentType.toString());
            }
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        @NonNull
        private RequestBody getRequestBody(@Nullable Params params) {
            if (params == null) {
                return RequestBody.create(null, Util.EMPTY_BYTE_ARRAY);
            }
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            return builder.build();
        }

        private void intercept(ResponseRequest request) throws IOException {
            List<IInterceptor> interceptors = mClient.interceptors;
            if (interceptors == null) {
                return;
            }
            for (IInterceptor interceptor : interceptors) {
                interceptor.intercept(request);
            }
        }
    }
}
