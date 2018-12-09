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
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.Task;
import com.d.lib.aster.utils.Util;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
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
    private Imp mImp;

    public HttpURLApi(HttpURLClient mClient) {
        this.mImp = new Imp(mClient);
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
                    Response response = getImp().getImp(url);
                    int code = response.code();
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new NetworkErrorException("Request error.");
                }
            }
        });
    }

    public Observable<ResponseBody> post(final String url) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImp().post(url);
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new NetworkErrorException("Request error.");
                }
            }
        });
    }

    public Observable<ResponseBody> post(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImp().post(url, params);
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new NetworkErrorException("Request error.");
                }
            }
        });
    }

    public Observable<ResponseBody> postForm(final String url, final Map<String, Object> forms) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImp().postBody(url, null);
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new NetworkErrorException("Request error.");
                }
            }
        });
    }

    public Observable<ResponseBody> postBody(final String url, final RequestBody requestBody) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImp().postBodyImp(url, requestBody);
                    int code = response.code();
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new NetworkErrorException("Request error.");
                }
            }
        });
    }

    public Observable<ResponseBody> put(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImp().putImp(url, params);
                    int code = response.code();
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new NetworkErrorException("Request error.");
                }
            }
        });
    }

    public Observable<ResponseBody> patch(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImp().putImp(url, params);
                    int code = response.code();
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new NetworkErrorException("Request error.");
                }
            }
        });
    }

    public Observable<ResponseBody> options(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImp().options(url, params);
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new NetworkErrorException("Request error.");
                }
            }
        });
    }

    public Observable<ResponseBody> head(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImp().head(url, params);
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new NetworkErrorException("Request error.");
                }
            }
        });
    }

    public Observable<ResponseBody> delete(final String url, final Params params) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImp().delete(url, params);
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new NetworkErrorException("Request error.");
                }
            }
        });
    }

    public Observable<ResponseBody> download(final String url) {
        return Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = getImp().downloadImp(url);
                    int code = response.code();
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new NetworkErrorException("Request error.");
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
                    Response response = getImp().uploadImp(url, multipartBodyParts);
                    int code = response.code();
                    return response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new NetworkErrorException("Request error.");
                }
            }
        });
    }


    static class Imp {
        private HttpURLClient mClient;

        public Imp(HttpURLClient client) {
            this.mClient = client;
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

        private RealResponse getRealResponse(@Nullable HttpURLConnection conn) {
            if (conn != null) {
                try {
                    return new RealResponse(conn.getResponseCode(),
                            conn.getResponseMessage(),
                            ResponseBody.create(conn));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return new RealResponse(-1, "", null);
        }

        public void get(String url, Params params, SimpleCallback<Response> callback) {
            get(url + "?" + params.getRequestParamsString(), callback);
        }

        public void get(String url, final SimpleCallback<Response> callback) {
            enqueue(getImp(url), callback);
        }

        private RealResponse getImp(String url) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "GET");
                conn.setDoInput(true);
                conn.connect();
                return getRealResponse(conn);
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return getRealResponse(null);
            }
        }

        private RealResponse patchImp(String url, Params params) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "PATCH");
                conn.setDoInput(true);
                conn.connect();
                return getRealResponse(conn);
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return getRealResponse(null);
            }
        }

        private RealResponse putImp(String url, Params params) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "PUT");
                conn.setDoInput(true);
                conn.connect();
                return getRealResponse(conn);
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return getRealResponse(null);
            }
        }

        public Response post(String url) throws IOException {
            return postImp(url, null);
        }

        public void post(String url, final SimpleCallback<Response> callback) {
            enqueue(postImp(url, null), callback);
        }

        public Response post(String url, Params params) throws IOException {
            return postImp(url, params);
        }

        public void post(String url, Params params, final SimpleCallback<Response> callback) {
            enqueue(postImp(url, params), callback);
        }

        private RealResponse postImp(String url, Params params) {
            HttpURLConnection conn = null;
            BufferedWriter writer = null;
            try {
                conn = getHttpURLConnection(url, "POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                if (!TextUtils.isEmpty("")) {
                    conn.setRequestProperty("Content-Type", "");
                }
                conn.connect();
                String body = params.getRequestParamsString();
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
                return getRealResponse(null);
            }
        }

        public Response postBody(String url, RequestBody requestBody) throws IOException {
            return postBodyImp(url, requestBody);
        }

        public void postBody(String url, RequestBody requestBody,
                             final SimpleCallback<Response> callback) {
            enqueue(postBodyImp(url, requestBody), callback);
        }

        private RealResponse postBodyImp(String url, RequestBody requestBody) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                if (!TextUtils.isEmpty("")) {
                    conn.setRequestProperty("Content-Type", "");
                }
                conn.connect();
                return getRealResponse(conn);
            } catch (Exception e) {
                e.printStackTrace();
                if (conn != null) {
                    conn.disconnect();
                }
                return getRealResponse(null);
            }
        }

        public Response options(String url, Params params) throws IOException {
            return optionsImp(url, params);
        }

        private RealResponse optionsImp(String url, Params params) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "OPTIONS");
                conn.setDoInput(true);
                conn.connect();
                return getRealResponse(conn);
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return getRealResponse(null);
            }
        }

        public Response head(String url, Params params) throws IOException {
            return headImp(url, params);
        }

        private RealResponse headImp(String url, Params params) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "HEAD");
                conn.setDoInput(true);
                conn.connect();
                return getRealResponse(conn);
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return getRealResponse(null);
            }
        }

        public Response delete(String url, Params params) throws IOException {
            return deleteImp(url, params);
        }

        private RealResponse deleteImp(String url, Params params) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "DELETE");
                conn.setDoInput(true);
                conn.connect();
                return getRealResponse(conn);
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return getRealResponse(null);
            }
        }

        private RealResponse downloadImp(String url) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpURLConnection(url, "GET");
                conn.setDoInput(true);
                conn.connect();
                return getRealResponse(conn);
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                return getRealResponse(null);
            }
        }

        private RealResponse uploadImp(String url, List<MultipartBody.Part> multipartBodyParts) {
            HttpURLConnection conn;
            try {
                conn = getHttpURLConnection(url, "POST");
                setConnection(conn);
                conn.connect();
                DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                for (MultipartBody.Part part : multipartBodyParts) {
                    if (part.requestBody == null
                            && !TextUtils.isEmpty(part.key)
                            && !TextUtils.isEmpty(part.key)) {
                        // 上传参数
                        outputStream.write(BodyWriter.getParamsString(part.key, part.value)
                                .getBytes());
                        outputStream.flush();
                    } else {
                        part.requestBody.writeTo(outputStream);
                    }
                }
                // 写结束标记位
                byte[] endData = (LINE_END + TWO_HYPHENS
                        + BOUNDARY
                        + TWO_HYPHENS + LINE_END).getBytes();
                outputStream.write(endData);
                outputStream.flush();
                return getRealResponse(conn);
            } catch (Exception e) {
                return getRealResponse(null);
            }
        }

        private void setConnection(HttpURLConnection conn) throws ProtocolException {
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; BOUNDARY=" + BOUNDARY);
        }

        private void enqueue(final Response response,
                             final @NonNull SimpleCallback<Response> callback) {

        }
    }
}
