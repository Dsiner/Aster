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
    private Imp mImp;

    public VolleyApi(VolleyClient client) {
        this.mImp = new Imp(client);
    }

    public Imp getImp() {
        return mImp;
    }

    public Observable<ResponseBody> get(String url, Params params) {
        return get(url + "?" + params.getRequestParamsString());
    }

    public Observable<ResponseBody> get(final String url) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImp().get(url);
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
                    RequestFuture<RealResponse> requestFuture = getImp().post(url);
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
                    RequestFuture<RealResponse> requestFuture = getImp().post(url, params);
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
                    RequestFuture<RealResponse> requestFuture = getImp().postBody(url, null);
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
                    RequestFuture<RealResponse> requestFuture = getImp().postBody(url, requestBody);
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
                    RequestFuture<RealResponse> requestFuture = getImp().put(url, params);
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

    public Observable<ResponseBody> patch(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImp().patch(url, params);
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

    public Observable<ResponseBody> options(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImp().options(url, params);
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

    public Observable<ResponseBody> head(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImp().head(url, params);
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
                    RequestFuture<RealResponse> requestFuture = getImp().delete(url, params);
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

    public Observable<ResponseBody> download(final String url) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    RequestFuture<RealResponse> requestFuture = getImp().download(url);
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
                    RequestFuture<RealResponse> requestFuture = getImp().upload(url, multipartBodyParts);
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

    static class Imp {
        private VolleyClient mClient;

        public Imp(VolleyClient client) {
            this.mClient = client;
        }

        public RequestFuture<RealResponse> get(String url, Params params) {
            return get(url + "?" + params.getRequestParamsString());
        }

        public RequestFuture<RealResponse> get(String url) {
            return getImp(url);
        }

        private RequestFuture<RealResponse> getImp(String url) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.GET, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> patch(String url, Params params) {
            return patchImp(url, params);
        }

        private RequestFuture<RealResponse> patchImp(String url, Params params) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.PATCH, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> put(String url, Params params) {
            return putImp(url, params);
        }

        private RequestFuture<RealResponse> putImp(String url, Params params) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.PUT, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> post(String url) throws IOException {
            return postImp(url, null);
        }

        public void post(String url, final SimpleCallback<Response> callback) {
            enqueue(postImp(url, null), callback);
        }

        public RequestFuture<RealResponse> post(String url, Params params) throws IOException {
            return postImp(url, params);
        }

        public void post(String url, Params params, final SimpleCallback<Response> callback) {
            enqueue(postImp(url, params), callback);
        }

        private RequestFuture<RealResponse> postImp(String url, Params params) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.POST, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> postBody(String url, RequestBody requestBody) throws IOException {
            return postBodyImp(url, requestBody);
        }

        public void postBody(String url, RequestBody requestBody,
                             final SimpleCallback<Response> callback) {
            enqueue(postBodyImp(url, requestBody), callback);
        }

        private RequestFuture<RealResponse> postBodyImp(String url, RequestBody requestBody) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.POST, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> options(String url, Params params) throws IOException {
            return optionsImp(url, params);
        }

        private RequestFuture<RealResponse> optionsImp(String url, Params params) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.OPTIONS, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> head(String url, Params params) throws IOException {
            return headImp(url, params);
        }

        private RequestFuture<RealResponse> headImp(String url, Params params) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.HEAD, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> delete(String url, Params params) throws IOException {
            return deleteImp(url, params);
        }

        private RequestFuture<RealResponse> deleteImp(String url, Params params) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.DELETE, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> download(String url) {
            return downloadImp(url);
        }

        private RequestFuture<RealResponse> downloadImp(String url) {
            final RequestFuture<RealResponse> requestFuture = RequestFuture.newFuture();
            ResponseRequest request = new ResponseRequest(Request.Method.GET, url,
                    requestFuture, requestFuture);
            requestFuture.setRequest(request);
            mClient.getRequestQueue().add(request);
            return requestFuture;
        }

        public RequestFuture<RealResponse> upload(String url, List<MultipartBody.Part> multipartBodyParts) {
            return uploadImp(url, multipartBodyParts);
        }

        private RequestFuture<RealResponse> uploadImp(String url, List<MultipartBody.Part> multipartBodyParts) {
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
