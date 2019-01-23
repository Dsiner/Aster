package com.d.lib.aster.integration.http.client;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.http.body.BodyWriter;
import com.d.lib.aster.integration.http.body.MultipartBody;
import com.d.lib.aster.integration.http.body.RequestBody;
import com.d.lib.aster.interceptor.IInterceptor;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.Task;
import com.d.lib.aster.utils.Util;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static com.d.lib.aster.integration.http.body.BodyWriter.BOUNDARY;
import static com.d.lib.aster.integration.http.body.BodyWriter.LINE_END;
import static com.d.lib.aster.integration.http.body.BodyWriter.TWO_HYPHENS;

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

    public Callable get(String url, Params params) {
        return get(url + "?" + params.getRequestParamsString());
    }

    public Callable get(final String url) {
        final HttpURLConnection conn = getImpl().getImpl(url);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImpl().execute(conn);
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(conn, observable);
    }

    public Callable post(final String url) {
        final HttpURLConnection conn = getImpl().postImpl(url, null);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImpl().executePostParams(conn, null);
                    checkSuccessful(response);
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(conn, observable);
    }

    public Callable post(final String url, final Params params) {
        final HttpURLConnection conn = getImpl().postImpl(url, params);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImpl().executePostParams(conn, params);
                    checkSuccessful(response);
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(conn, observable);
    }

    public Callable postForm(final String url, final Map<String, Object> forms) {
        final HttpURLConnection conn = getImpl().postBodyImpl(url, null);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImpl().execute(conn);
                    checkSuccessful(response);
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(conn, observable);
    }

    public Callable postBody(final String url, final RequestBody requestBody) {
        final HttpURLConnection conn = getImpl().postBodyImpl(url, requestBody);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImpl().execute(conn);
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(conn, observable);
    }

    public Callable put(final String url, final Params params) {
        final HttpURLConnection conn = getImpl().putImpl(url, params);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImpl().execute(conn);
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(conn, observable);
    }

    public Callable head(final String url, final Params params) {
        final HttpURLConnection conn = getImpl().headImpl(url, params);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImpl().execute(conn);
                    checkSuccessful(response);
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(conn, observable);
    }

    public Callable delete(final String url, final Params params) {
        final HttpURLConnection conn = getImpl().deleteImpl(url, params);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImpl().execute(conn);
                    checkSuccessful(response);
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(conn, observable);
    }

    public Callable options(final String url, final Params params) {
        final HttpURLConnection conn = getImpl().optionsImpl(url, params);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImpl().execute(conn);
                    checkSuccessful(response);
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(conn, observable);
    }

    public Callable patch(final String url, final Params params) {
        final HttpURLConnection conn = getImpl().patchImpl(url, params);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImpl().execute(conn);
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(conn, observable);
    }

    public Callable download(final String url, final Params params) {
        return download(url + "?" + params.getRequestParamsString());
    }

    public Callable download(final String url) {
        final HttpURLConnection conn = getImpl().downloadImpl(url);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImpl().execute(conn);
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(conn, observable);
    }

    public Callable upload(final String url, final List<MultipartBody.Part> multipartBodyParts) {
        final HttpURLConnection conn = getImpl().uploadImpl(url);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImpl().executeUpload(conn, multipartBodyParts);
                    checkSuccessful(response);
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(conn, observable);
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
        public final Observable<ResponseBody> observable;

        public Callable(HttpURLConnection conn, Observable<ResponseBody> observable) {
            this.conn = conn;
            this.observable = observable;
        }
    }

    static class Impl {
        private final static int REQUEST_TYPE_NORMAL = 0;
        private final static int REQUEST_TYPE_POSTBODY = 1;
        private final static int REQUEST_TYPE_UPLOAD = 2;

        private HttpURLClient mClient;

        public Impl(HttpURLClient client) {
            this.mClient = client;
        }

        public Response get(String url, Params params) {
            return get(url + "?" + params.getRequestParamsString());
        }

        public Response get(String url) {
            return execute(getImpl(url));
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


        public Response post(String url) throws IOException {
            return executePostParams(postImpl(url, null), null);
        }

        public Response post(String url, Params params) throws IOException {
            return executePostParams(postImpl(url, params), params);
        }

        private HttpURLConnection postImpl(String url, Params params) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                if (!TextUtils.isEmpty("")) {
                    conn.setRequestProperty("Content-Type", "");
                }
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

        public Response postBody(String url, RequestBody requestBody) throws IOException {
            return execute(postBodyImpl(url, requestBody));
        }

        private HttpURLConnection postBodyImpl(String url, RequestBody requestBody) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                if (!TextUtils.isEmpty("")) {
                    conn.setRequestProperty("Content-Type", "");
                }
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

        public Response put(String url, Params params) {
            return execute(putImpl(url, params));
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

        public Response head(String url, Params params) throws IOException {
            return execute(headImpl(url, params));
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

        public Response delete(String url, Params params) throws IOException {
            return execute(deleteImpl(url, params));
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

        public Response options(String url, Params params) throws IOException {
            return execute(optionsImpl(url, params));
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

        public Response patch(String url, Params params) {
            return execute(patchImpl(url, params));
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

        public Response download(String url) {
            return execute(downloadImpl(url));
        }

        @Nullable
        private HttpURLConnection downloadImpl(String url) {
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

        public Response upload(String url, List<MultipartBody.Part> multipartBodyParts) {
            return executeUpload(uploadImpl(url), multipartBodyParts);
        }

        @Nullable
        private HttpURLConnection uploadImpl(String url) {
            HttpURLConnection conn;
            try {
                conn = getHttpURLConnection(url, "POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("Content-Type", "multipart/form-data; BOUNDARY=" + BOUNDARY);
                intercept(conn);
                return conn;
            } catch (Exception e) {
                return null;
            }
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
                conn.connect();
                return getRealResponse(conn);
            } catch (IOException e) {
                e.printStackTrace();
                conn.disconnect();
                return getRealResponse(e);
            }
        }

        @NonNull
        private RealResponse executePostParams(@Nullable HttpURLConnection conn,
                                               @Nullable Params params) {
            if (conn == null) {
                return getRealResponse(new NullPointerException("HttpURLConnection is null."));
            }
            BufferedWriter writer = null;
            try {
                conn.connect();
                String body = params != null ? params.getRequestParamsString() : null;
                if (!TextUtils.isEmpty(body)) {
                    writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),
                            "UTF-8"));
                    writer.write(body);
                    writer.close();
                }
                return getRealResponse(conn);
            } catch (Exception e) {
                e.printStackTrace();
                Util.closeQuietly(writer);
                if (conn != null) {
                    conn.disconnect();
                }
                return getRealResponse(e);
            }
        }

        @NonNull
        private RealResponse executeUpload(@Nullable HttpURLConnection conn,
                                           List<MultipartBody.Part> multipartBodyParts) {
            if (conn == null) {
                return getRealResponse(new NullPointerException("HttpURLConnection is null."));
            }
            try {
                conn.connect();
                DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                for (MultipartBody.Part part : multipartBodyParts) {
                    if (part.requestBody == null
                            && !TextUtils.isEmpty(part.key)
                            && !TextUtils.isEmpty(part.key)) {
                        // Upload parameter
                        outputStream.write(BodyWriter.getParamsString(part.key, part.value)
                                .getBytes());
                        outputStream.flush();
                    } else {
                        part.requestBody.writeTo(outputStream);
                    }
                }
                // Write end mark
                byte[] endData = (LINE_END + TWO_HYPHENS
                        + BOUNDARY
                        + TWO_HYPHENS + LINE_END).getBytes();
                outputStream.write(endData);
                outputStream.flush();
                return getRealResponse(conn);
            } catch (Exception e) {
                return getRealResponse(e);
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

        @Deprecated
        private void enqueue(final Response response,
                             final @NonNull SimpleCallback<Response> callback) {

        }
    }
}
