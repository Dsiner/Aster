package com.d.lib.aster.integration.http.client;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.http.body.FormBody;
import com.d.lib.aster.integration.http.body.RequestBody;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.Task;
import com.d.lib.aster.util.Utils;

import java.io.IOException;
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

    public Callable get(final String url) {
        return get(url, null);
    }

    public Callable get(final String url, @Nullable final Params params) {
        final String realUrl = params != null ? url + "?" + params.getRequestParamsString() : url;
        final Call call = getImpl().getImpl(realUrl);
        final Observable<Response> observable = getObservable(call);
        return new Callable(call, observable);
    }

    public Callable post(final String url) {
        return post(url, null);
    }

    public Callable post(final String url, @Nullable final Params params) {
        final Call call = getImpl().postImpl(url, params);
        final Observable<Response> observable = getObservable(call);
        return new Callable(call, observable);
    }

    public Callable postBody(final String url, final RequestBody requestBody) {
        final Call call = getImpl().postBodyImpl(url, requestBody);
        final Observable<Response> observable = getObservable(call);
        return new Callable(call, observable);
    }

    public Callable put(final String url) {
        return put(url, null);
    }

    public Callable put(final String url, @Nullable final Params params) {
        final Call call = getImpl().putImpl(url, params);
        final Observable<Response> observable = getObservable(call);
        return new Callable(call, observable);
    }

    public Callable putBody(final String url, final RequestBody requestBody) {
        final Call call = getImpl().putBodyImpl(url, requestBody);
        final Observable<Response> observable = getObservable(call);
        return new Callable(call, observable);
    }

    public Callable head(final String url) {
        final Call call = getImpl().headImpl(url);
        final Observable<Response> observable = getObservable(call);
        return new Callable(call, observable);
    }

    public Callable delete(final String url) {
        return delete(url, null);
    }

    public Callable delete(final String url, final Params params) {
        final Call call = getImpl().deleteImpl(url, params);
        final Observable<Response> observable = getObservable(call);
        return new Callable(call, observable);
    }

    public Callable deleteBody(final String url, final RequestBody requestBody) {
        final Call call = getImpl().deleteBodyIml(url, requestBody);
        final Observable<Response> observable = getObservable(call);
        return new Callable(call, observable);
    }

    public Callable options(final String url) {
        return options(url, null);
    }

    public Callable options(final String url, final Params params) {
        final Call call = getImpl().optionsImpl(url, params);
        final Observable<Response> observable = getObservable(call);
        return new Callable(call, observable);
    }

    public Callable optionsBody(final String url, final RequestBody requestBody) {
        final Call call = getImpl().optionsBodyImpl(url, requestBody);
        final Observable<Response> observable = getObservable(call);
        return new Callable(call, observable);
    }

    public Callable patch(final String url) {
        return patch(url, null);
    }

    public Callable patch(final String url, final Params params) {
        final Call call = getImpl().patchImpl(url, params);
        final Observable<Response> observable = getObservable(call);
        return new Callable(call, observable);
    }

    public Callable patchBody(final String url, final RequestBody requestBody) {
        final Call call = getImpl().patchBodyImpl(url, requestBody);
        final Observable<Response> observable = getObservable(call);
        return new Callable(call, observable);
    }

    public Callable download(final String url) {
        return download(url, null);
    }

    public Callable download(final String url, @Nullable final Params params) {
        final String realUrl = params != null ? url + "?" + params.getRequestParamsString() : url;
        final Call call = getImpl().downloadImpl(realUrl);
        final Observable<Response> observable = getObservable(call);
        return new Callable(call, observable);
    }

    public Callable uploadBody(final String url, final RequestBody requestBody) {
        final Call call = getImpl().uploadBodyImpl(url, requestBody);
        final Observable<Response> observable = getObservable(call);
        return new Callable(call, observable);
    }

    @NonNull
    private Observable<Response> getObservable(final Call call) {
        return Observable.create(new Task<Response>() {
            @Override
            public Response run() throws Exception {
                try {
                    return call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
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
        public final Observable<Response> observable;

        public Callable(Call call, Observable<Response> observable) {
            this.call = call;
            this.observable = observable;
        }
    }

    static class Impl {
        private HttpURLClient mClient;

        public Impl(HttpURLClient client) {
            this.mClient = client;
        }

        private Call getImpl(final String url) {
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            return mClient.newCall(request);
        }

        private Call postImpl(final String url, @Nullable final Params params) {
            final RequestBody requestBody = getRequestBody(params);
            return postBodyImpl(url, requestBody);
        }

        private Call postBodyImpl(final String url, @NonNull final RequestBody requestBody) {
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            return mClient.newCall(request);
        }

        private Call putImpl(final String url, @Nullable final Params params) {
            final RequestBody requestBody = getRequestBody(params);
            return putBodyImpl(url, requestBody);
        }

        private Call putBodyImpl(final String url, @NonNull final RequestBody requestBody) {
            final Request request = new Request.Builder()
                    .url(url)
                    .put(requestBody)
                    .build();
            return mClient.newCall(request);
        }

        private Call headImpl(final String url) {
            final Request request = new Request.Builder()
                    .url(url)
                    .head()
                    .build();
            return mClient.newCall(request);
        }

        private Call deleteImpl(final String url, @Nullable final Params params) {
            final RequestBody requestBody = getRequestBody(params);
            return deleteBodyIml(url, requestBody);
        }

        private Call deleteBodyIml(final String url, @Nullable final RequestBody requestBody) {
            final Request request = new Request.Builder()
                    .url(url)
                    .delete(requestBody)
                    .build();
            return mClient.newCall(request);
        }

        private Call optionsImpl(final String url, @Nullable final Params params) {
            final RequestBody requestBody = getRequestBody(params);
            return optionsBodyImpl(url, requestBody);
        }

        private Call optionsBodyImpl(final String url, @Nullable final RequestBody requestBody) {
            final Request request = new Request.Builder()
                    .url(url)
                    .method("OPTIONS", requestBody)
                    .build();
            return mClient.newCall(request);
        }

        private Call patchImpl(final String url, @Nullable final Params params) {
            final RequestBody requestBody = getRequestBody(params);
            return patchBodyImpl(url, requestBody);
        }

        private Call patchBodyImpl(final String url, @NonNull final RequestBody requestBody) {
            final Request request = new Request.Builder()
                    .url(url)
                    .patch(requestBody)
                    .build();
            return mClient.newCall(request);
        }

        private Call downloadImpl(final String url) {
            final Request request = new Request.Builder()
                    .url(url)
                    .transfer()
                    .build();
            return mClient.newCall(request);
        }

        private Call uploadBodyImpl(final String url, @NonNull final RequestBody requestBody) {
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .transfer()
                    .build();
            return mClient.newCall(request);
        }

        @NonNull
        private RequestBody getRequestBody(@Nullable Params params) {
            if (params == null) {
                return RequestBody.create(null, Utils.EMPTY_BYTE_ARRAY);
            }
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            return builder.build();
        }

        @Deprecated
        private void enqueue(@NonNull final Call call,
                             @Nullable final SimpleCallback<Response> callback) {
            call.enqueue(new SimpleCallback<Response>() {
                @Override
                public void onError(Throwable e) {
                    if (callback != null) {
                        callback.onError(e);
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (callback != null) {
                        callback.onSuccess(response);
                    }
                }
            });
        }
    }
}
