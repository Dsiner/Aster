package com.d.lib.aster.integration.volley.client;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.volley.body.MultipartBody;
import com.d.lib.aster.integration.volley.body.RequestBody;
import com.d.lib.aster.interceptor.IInterceptor;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.Task;

import java.io.IOException;
import java.net.HttpURLConnection;
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
                    RequestFuture<RealResponse> requestFuture = getImpl().get(url);
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
                    RequestFuture<RealResponse> requestFuture = getImpl().post(url);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (IOException e) {
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
                    RequestFuture<RealResponse> requestFuture = getImpl().post(url, params);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> postForm(final String url, final Map<String, Object> forms) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().postBody(url, null);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (IOException e) {
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
                    RequestFuture<RealResponse> requestFuture = getImpl().postBody(url, requestBody);
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

    public Observable<ResponseBody> put(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().put(url, params);
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

    public Observable<ResponseBody> head(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().head(url, params);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> delete(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().delete(url, params);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> options(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().options(url, params);
                    if (requestFuture.isCancelled()) {
                        throw new NetworkErrorException("Request cancelled.");
                    }
                    RealResponse response = requestFuture.get();
                    checkSuccessful(response);
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    public Observable<ResponseBody> patch(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImpl().patch(url, params);
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
                    RequestFuture<RealResponse> requestFuture = getImpl().download(url);
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
                    RequestFuture<RealResponse> requestFuture = getImpl().upload(url, multipartBodyParts);
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

        public Impl(VolleyClient client) {
            this.mClient = client;
        }

        public RequestFuture<RealResponse> get(String url, Params params) {
            return get(url + "?" + params.getRequestParamsString());
        }

        public RequestFuture<RealResponse> get(String url) {
            return getImpl(url);
        }

        private RequestFuture<RealResponse> getImpl(String url) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.GET, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }


        public RequestFuture<RealResponse> post(String url) throws IOException {
            return postImpl(url, null);
        }

        public void post(String url, final SimpleCallback<Response> callback) {
            enqueue(postImpl(url, null), callback);
        }

        public RequestFuture<RealResponse> post(String url, Params params) throws IOException {
            return postImpl(url, params);
        }

        public void post(String url, Params params, final SimpleCallback<Response> callback) {
            enqueue(postImpl(url, params), callback);
        }

        private RequestFuture<RealResponse> postImpl(String url, Params params) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.POST, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> postBody(String url, RequestBody requestBody) throws IOException {
            return postBodyImpl(url, requestBody);
        }

        public void postBody(String url, RequestBody requestBody,
                             final SimpleCallback<Response> callback) {
            enqueue(postBodyImpl(url, requestBody), callback);
        }

        private RequestFuture<RealResponse> postBodyImpl(String url, RequestBody requestBody) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.POST, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> put(String url, Params params) {
            return putImpl(url, params);
        }

        private RequestFuture<RealResponse> putImpl(String url, Params params) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.PUT, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> head(String url, Params params) throws IOException {
            return headImpl(url, params);
        }

        private RequestFuture<RealResponse> headImpl(String url, Params params) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.HEAD, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> delete(String url, Params params) throws IOException {
            return deleteImpl(url, params);
        }

        private RequestFuture<RealResponse> deleteImpl(String url, Params params) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.DELETE, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> options(String url, Params params) throws IOException {
            return optionsImpl(url, params);
        }

        private RequestFuture<RealResponse> optionsImpl(String url, Params params) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.OPTIONS, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> patch(String url, Params params) {
            return patchImpl(url, params);
        }

        private RequestFuture<RealResponse> patchImpl(String url, Params params) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.PATCH, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> download(String url) {
            return downloadImpl(url);
        }

        private RequestFuture<RealResponse> downloadImpl(String url) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.GET, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> upload(String url, List<MultipartBody.Part> multipartBodyParts) {
            return uploadImpl(url, multipartBodyParts);
        }

        private RequestFuture<RealResponse> uploadImpl(String url, List<MultipartBody.Part> multipartBodyParts) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.POST, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        private void intercept(HttpURLConnection conn) throws IOException {
            List<IInterceptor> interceptors = mClient.interceptors;
            if (interceptors == null) {
                return;
            }
            for (IInterceptor interceptor : interceptors) {
                interceptor.intercept(conn);
            }
        }

        private void enqueue(final RequestFuture<RealResponse> response,
                             final @NonNull SimpleCallback<Response> callback) {

        }
    }
}
