package com.d.lib.aster.integration.http.client;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.d.lib.aster.base.MediaType;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.integration.http.body.FormBody;
import com.d.lib.aster.integration.http.body.MultipartBody;
import com.d.lib.aster.integration.http.body.RequestBody;
import com.d.lib.aster.interceptor.IInterceptor;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.Task;
import com.d.lib.aster.utils.Util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * HttpURLApi
 * Created by D on 2018/12/7.
 **/
public class HttpURLApi {
    private Impl mImpl;

    public HttpURLApi(HttpURLClient mClient) {
        this.mImpl = new Impl(mClient);
    }

    public Impl getImpl() {
        return mImpl;
    }

    public Callable get(String url) {
        return get(url, null);
    }

    public Callable get(final String url, Params params) {
        final String realUrl = params != null ? url + "?" + params.getRequestParamsString() : url;
        final HttpURLConnection conn = getImpl().getImpl(realUrl);
        final Observable<Response> observable = getObservable(conn);
        return new Callable(conn, observable);
    }

    public Callable post(final String url) {
        return post(url, null);
    }

    public Callable post(final String url, final Params params) {
        final HttpURLConnection conn = getImpl().postImpl(url, params);
        final Observable<Response> observable = getObservableParams(params, conn);
        return new Callable(conn, observable);
    }

    public Callable postBody(final String url, final RequestBody requestBody) {
        final HttpURLConnection conn = getImpl().postBodyImpl(url, requestBody);
        final Observable<Response> observable = getObservableBody(requestBody, conn);
        return new Callable(conn, observable);
    }

    public Callable put(final String url) {
        return put(url, null);
    }

    public Callable put(final String url, final Params params) {
        final HttpURLConnection conn = getImpl().putImpl(url, params);
        final Observable<Response> observable = getObservableParams(params, conn);
        return new Callable(conn, observable);
    }

    public Callable putBody(final String url, final RequestBody requestBody) {
        final HttpURLConnection conn = getImpl().putBodyImpl(url, requestBody);
        final Observable<Response> observable = getObservableBody(requestBody, conn);
        return new Callable(conn, observable);
    }

    public Callable head(final String url, final Params params) {
        final HttpURLConnection conn = getImpl().headImpl(url, params);
        final Observable<Response> observable = getObservableParams(params, conn);
        return new Callable(conn, observable);
    }

    public Callable delete(final String url) {
        return delete(url, null);
    }

    public Callable delete(final String url, final Params params) {
        final HttpURLConnection conn = getImpl().deleteImpl(url, params);
        final Observable<Response> observable = getObservableParams(params, conn);
        return new Callable(conn, observable);
    }

    public Callable deleteBody(final String url, final RequestBody requestBody) {
        final HttpURLConnection conn = getImpl().deleteBodyImpl(url, requestBody);
        final Observable<Response> observable = getObservableBody(requestBody, conn);
        return new Callable(conn, observable);
    }

    public Callable options(final String url) {
        return options(url, null);
    }

    public Callable options(final String url, final Params params) {
        final HttpURLConnection conn = getImpl().optionsImpl(url, params);
        final Observable<Response> observable = getObservableParams(params, conn);
        return new Callable(conn, observable);
    }

    public Callable optionsBody(final String url, final RequestBody requestBody) {
        final HttpURLConnection conn = getImpl().optionsBodyImpl(url, requestBody);
        final Observable<Response> observable = getObservableBody(requestBody, conn);
        return new Callable(conn, observable);
    }

    public Callable patch(final String url) {
        return patch(url, null);
    }

    public Callable patch(final String url, final Params params) {
        final HttpURLConnection conn = getImpl().patchImpl(url, params);
        final Observable<Response> observable = getObservableParams(params, conn);
        return new Callable(conn, observable);
    }

    public Callable patchBody(final String url, final RequestBody requestBody) {
        final HttpURLConnection conn = getImpl().patchBodyImpl(url, requestBody);
        final Observable<Response> observable = getObservableBody(requestBody, conn);
        return new Callable(conn, observable);
    }

    private Observable<Response> getObservable(final HttpURLConnection conn) {
        return Observable.create(new Task<Response>() {
            @Override
            public Response run() throws Exception {
                try {
                    return getImpl().execute(conn);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    private Observable<Response> getObservableParams(final Params params,
                                                     final HttpURLConnection conn) {
        return Observable.create(new Task<Response>() {
            @Override
            public Response run() throws Exception {
                try {
                    return getImpl().executeParams(conn, params);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    private Observable<Response> getObservableBody(final RequestBody requestBody,
                                                   final HttpURLConnection conn) {
        return Observable.create(new Task<Response>() {
            @Override
            public Response run() throws Exception {
                try {
                    return getImpl().executeBody(conn, requestBody);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    private void checkSuccessful(@NonNull Response response) throws Exception {

    }

    private void checkSuccessfulImpl(@NonNull Response response) throws Exception {
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

    public static final class Callable {
        public final HttpURLConnection conn;
        public final Observable<Response> observable;

        public Callable(HttpURLConnection conn, Observable<Response> observable) {
            this.conn = conn;
            this.observable = observable;
        }
    }

    static class Impl {
        private final static int REQUEST_TYPE_HTTP = 0;
        private final static int REQUEST_TYPE_BODY = 1;
        private final static int REQUEST_TYPE_MULTIPARTBODY = 2;

        private HttpURLClient mClient;

        public Impl(HttpURLClient client) {
            this.mClient = client;
        }

        private HttpURLConnection getImpl(String url) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "GET");
                conn.setDoInput(true);
                intercept(conn);
                return conn;
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return null;
            }
        }

        private HttpURLConnection postImpl(String url, Params params) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                intercept(conn);
                return conn;
            } catch (Exception e) {
                e.printStackTrace();
                if (conn != null) {
                    conn.disconnect();
                }
                return null;
            }
        }

        private HttpURLConnection postBodyImpl(String url, @NonNull RequestBody requestBody) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                intercept(conn);
                return conn;
            } catch (Exception e) {
                e.printStackTrace();
                if (conn != null) {
                    conn.disconnect();
                }
                return null;
            }
        }

        private HttpURLConnection putImpl(String url, Params params) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "PUT");
                conn.setDoInput(true);
                intercept(conn);
                return conn;
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return null;
            }
        }

        private HttpURLConnection putBodyImpl(String url, final RequestBody requestBody) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "PUT");
                conn.setDoInput(true);
                intercept(conn);
                return conn;
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return null;
            }
        }

        @Nullable
        private HttpURLConnection headImpl(String url, Params params) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "HEAD");
                conn.setDoInput(true);
                intercept(conn);
                return conn;
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return null;
            }
        }

        private HttpURLConnection deleteImpl(String url, Params params) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "DELETE");
                conn.setDoInput(true);
                intercept(conn);
                return conn;
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return null;
            }
        }

        private HttpURLConnection deleteBodyImpl(String url, RequestBody requestBody) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "DELETE");
                conn.setDoInput(true);
                intercept(conn);
                return conn;
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return null;
            }
        }

        private HttpURLConnection optionsImpl(String url, Params params) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "OPTIONS");
                conn.setDoInput(true);
                intercept(conn);
                return conn;
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return null;
            }
        }

        private HttpURLConnection optionsBodyImpl(String url, RequestBody requestBody) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "OPTIONS");
                conn.setDoInput(true);
                intercept(conn);
                return conn;
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return null;
            }
        }

        private HttpURLConnection patchImpl(String url, Params params) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "PATCH");
                conn.setDoInput(true);
                intercept(conn);
                return conn;
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return null;
            }
        }

        private HttpURLConnection patchBodyImpl(String url, RequestBody requestBody) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "PATCH");
                conn.setDoInput(true);
                intercept(conn);
                return conn;
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return null;
            }
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

        private HttpURLConnection getHttpURLConnection(String requestURL, String requestMethod)
                throws IOException {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(mClient.connectTimeout);
            conn.setReadTimeout(mClient.readTimeout);
            conn.setRequestMethod(requestMethod);
            return conn;
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

        @NonNull
        private RealResponse execute(@Nullable HttpURLConnection conn) {
            if (conn == null) {
                return getRealResponse(new NullPointerException("HttpURLConnection is null."));
            }
            try {
                return getRealResponse(conn);
            } catch (Exception e) {
                e.printStackTrace();
                conn.disconnect();
                return getRealResponse(e);
            }
        }

        @NonNull
        private RealResponse executeParams(@Nullable HttpURLConnection conn,
                                           @Nullable Params params) {
            final RequestBody requestBody = getRequestBody(params);
            return executeBody(conn, requestBody);
        }

        @NonNull
        private RealResponse executeBody(@Nullable HttpURLConnection conn,
                                         @NonNull RequestBody requestBody) {
            if (conn == null) {
                return getRealResponse(new NullPointerException("HttpURLConnection is null."));
            }
            MediaType contentType = requestBody.contentType();
            if (contentType != null
                    && !TextUtils.isEmpty(contentType.toString())) {
                conn.setRequestProperty("Content-Type", contentType.toString());
            }
            DataOutputStream outputStream = null;
            try {
                outputStream = new DataOutputStream(conn.getOutputStream());
                requestBody.writeTo(outputStream);
                outputStream.flush();
                return getRealResponse(conn);
            } catch (Exception e) {
                e.printStackTrace();
                conn.disconnect();
                return getRealResponse(e);
            } finally {
                Util.closeQuietly(outputStream);
            }
        }

        @NonNull
        private RealResponse executeMultipartBody(@Nullable HttpURLConnection conn,
                                                  List<MultipartBody.Part> multipartBodyParts) {
            if (conn == null) {
                return getRealResponse(new NullPointerException("HttpURLConnection is null."));
            }
            DataOutputStream outputStream = null;
            try {
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                for (MultipartBody.Part part : multipartBodyParts) {
                    builder.addPart(part);
                }
                RequestBody requestBody = builder.build();
                MediaType contentType = requestBody.contentType();
                if (contentType != null
                        && !TextUtils.isEmpty(contentType.toString())) {
                    conn.setRequestProperty("Content-Type", contentType.toString());
                }
                outputStream = new DataOutputStream(conn.getOutputStream());
                requestBody.writeTo(outputStream);
                outputStream.flush();
                return getRealResponse(conn);
            } catch (Exception e) {
                e.printStackTrace();
                conn.disconnect();
                return getRealResponse(e);
            } finally {
                Util.closeQuietly(outputStream);
            }
        }

        private RealResponse getRealResponse(@Nullable HttpURLConnection conn) {
            if (conn != null) {
                try {
                    return new RealResponse(conn.getResponseCode(),
                            conn.getResponseMessage(),
                            ResponseBody.create(conn),
                            null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return new RealResponse(-1, "", null, null);
        }

        private RealResponse getRealResponse(@Nullable Exception e) {
            return new RealResponse(-1, "", null, e);
        }
    }
}
