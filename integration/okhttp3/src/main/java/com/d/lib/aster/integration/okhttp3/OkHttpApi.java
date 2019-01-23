package com.d.lib.aster.integration.okhttp3;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.Task;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OkHttpApi
 * Created by D on 2018/12/7.
 **/
public class OkHttpApi {
    private Impl mImpl;

    public OkHttpApi(okhttp3.OkHttpClient mClient) {
        this.mImpl = new Impl(mClient);
    }

    public Impl getImpl() {
        return mImpl;
    }

    public Callable get(String url, Params params) {
        return get(url + "?" + params.getRequestParamsString());
    }

    public Callable get(final String url) {
        final Call call = getImpl().getImpl(url);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = call.execute();
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(call, observable);
    }

    public Callable post(final String url) {
        final Call call = getImpl().postImpl(url, null);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = call.execute();
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(call, observable);
    }

    public Callable post(final String url, final Params params) {
        final Call call = getImpl().postImpl(url, params);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = call.execute();
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(call, observable);
    }

    public Callable postForm(final String url, final Map<String, Object> forms) {
        final FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : forms.entrySet()) {
            builder.add(entry.getKey(), String.valueOf(entry.getValue()));
        }
        final Call call = getImpl().postBodyImpl(url, builder.build());
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = call.execute();
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(call, observable);
    }

    public Callable postBody(final String url, final RequestBody requestBody) {
        final Call call = getImpl().postBodyImpl(url, requestBody);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = call.execute();
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(call, observable);
    }

    public Callable put(final String url, final Params params) {
        final Call call = getImpl().putImpl(url, params);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = call.execute();
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(call, observable);
    }

    public Callable head(final String url, final Params params) {
        final Call call = getImpl().headImpl(url, params);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = call.execute();
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(call, observable);
    }

    public Callable delete(final String url, final Params params) {
        final Call call = getImpl().deleteImpl(url, params);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = call.execute();
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(call, observable);
    }

    public Callable options(final String url, final Params params) {
        final Call call = getImpl().optionsImpl(url, params);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = call.execute();
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(call, observable);
    }

    public Callable patch(final String url, final Params params) {
        final Call call = getImpl().patchImpl(url, params);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = call.execute();
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(call, observable);
    }

    public Callable download(final String url, final Params params) {
        return download(url + "?" + params.getRequestParamsString());
    }

    public Callable download(final String url) {
        final Call call = getImpl().downloadImpl(url);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = call.execute();
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(call, observable);
    }

    public Callable upload(final String url,
                           final List<MultipartBody.Part> multipartBodyParts) {
        final Call call = getImpl().uploadImpl(url, multipartBodyParts);
        final Observable<ResponseBody> observable = Observable.create(new Task<ResponseBody>() {
            @Override
            public ResponseBody run() throws Exception {
                try {
                    Response response = call.execute();
                    int code = response.code();
                    return response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        return new Callable(call, observable);
    }

    private void checkSuccessful(@NonNull Response response) throws Exception {
        if (!response.isSuccessful()) {
            throw new NetworkErrorException(!TextUtils.isEmpty(response.message()) ?
                    "Request is not successful for " + response.message()
                    : "Request is not successful.");
        }
    }

    public static final class Callable {
        public final Call call;
        public final Observable<ResponseBody> observable;

        public Callable(Call call, Observable<ResponseBody> observable) {
            this.call = call;
            this.observable = observable;
        }
    }

    static class Impl {
        private okhttp3.OkHttpClient mClient;

        public Impl(OkHttpClient client) {
            this.mClient = client;
        }


        public void get(String url, Params params, SimpleCallback<Response> callback) {
            get(url + "?" + params.getRequestParamsString(), callback);
        }

        public void get(String url, final SimpleCallback<Response> callback) {
            enqueue(getImpl(url), callback);
        }

        private Call getImpl(String url) {
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            return mClient.newCall(request);
        }

        public Response post(String url) throws IOException {
            return postImpl(url, null).execute();
        }

        public void post(String url, final SimpleCallback<Response> callback) {
            enqueue(postImpl(url, null), callback);
        }

        public Response post(String url, Params params) throws IOException {
            return postImpl(url, params).execute();
        }

        public void post(String url, Params params, final SimpleCallback<Response> callback) {
            enqueue(postImpl(url, params), callback);
        }

        private Call postImpl(String url, Params params) {
            FormBody.Builder builder = new FormBody.Builder();
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.add(key, params.get(key));
                }
            }
            final Request request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
            return mClient.newCall(request);
        }

        public Response postBody(String url, RequestBody requestBody) throws IOException {
            return postBodyImpl(url, requestBody).execute();
        }

        public void postBody(String url, RequestBody requestBody,
                             final SimpleCallback<Response> callback) {
            enqueue(postBodyImpl(url, requestBody), callback);
        }

        private Call postBodyImpl(String url, RequestBody requestBody) {
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            return mClient.newCall(request);
        }

        private Call putImpl(String url, Params params) {
            final FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            final Request request = new Request.Builder()
                    .url(url)
                    .put(builder.build())
                    .build();
            return mClient.newCall(request);
        }

        public Response head(String url, Params params) throws IOException {
            return headImpl(url, params).execute();
        }

        private Call headImpl(String url, Params params) {
            FormBody.Builder builder = new FormBody.Builder();
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.add(key, params.get(key));
                }
            }
            final Request request = new Request.Builder()
                    .url(url)
                    .method("HEAD", builder.build())
                    .build();
            return mClient.newCall(request);
        }

        public Response delete(String url, Params params) throws IOException {
            return deleteImpl(url, params).execute();
        }

        private Call deleteImpl(String url, Params params) {
            FormBody.Builder builder = new FormBody.Builder();
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.add(key, params.get(key));
                }
            }
            final Request request = new Request.Builder()
                    .url(url)
                    .delete(builder.build())
                    .build();
            return mClient.newCall(request);
        }

        public Response options(String url, Params params) throws IOException {
            return optionsImpl(url, params).execute();
        }

        private Call optionsImpl(String url, Params params) {
            FormBody.Builder builder = new FormBody.Builder();
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.add(key, params.get(key));
                }
            }
            final Request request = new Request.Builder()
                    .url(url)
                    .method("OPTIONS", builder.build())
                    .build();
            return mClient.newCall(request);
        }

        private Call patchImpl(String url, Params params) {
            final FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            final Request request = new Request.Builder()
                    .url(url)
                    .patch(builder.build())
                    .build();
            return mClient.newCall(request);
        }

        private Call downloadImpl(String url) {
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            return mClient.newCall(request);
        }

        private Call uploadImpl(String url, List<MultipartBody.Part> multipartBodyParts) {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            for (MultipartBody.Part part : multipartBodyParts) {
                builder.addPart(part);
            }
            final Request request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
            return mClient.newCall(request);
        }

        private void enqueue(@NonNull Call call, final SimpleCallback<Response> callback) {
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callback != null) {
                        callback.onError(e);
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (callback != null) {
                        callback.onSuccess(response);
                    }
                }
            });
        }
    }
}
