package com.d.lib.aster.integration.okhttp3.func;

import android.support.annotation.NonNull;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.callback.UploadCallback;
import com.d.lib.aster.integration.okhttp3.RequestManagerImpl;
import com.d.lib.aster.integration.okhttp3.observer.ApiObserver;
import com.d.lib.aster.integration.okhttp3.observer.AsyncApiObserver;
import com.d.lib.aster.integration.okhttp3.observer.DownloadObserver;
import com.d.lib.aster.integration.okhttp3.observer.UploadObserver;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.DisposableObserver;
import com.d.lib.aster.scheduler.schedule.Schedulers;
import com.d.lib.aster.util.Utils;

import okhttp3.Call;
import okhttp3.Response;

public class ApiTransformer {

    public static <T> void request(final Call call,
                                   final Observable<Response> observable,
                                   final Config config,
                                   final SimpleCallback<T> callback,
                                   final Object tag) {
        DisposableObserver<T> disposableObserver = new ApiObserver<T>(call, callback, tag);
        if (tag != null) {
            RequestManagerImpl.getIns().add(tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(Utils.getFirstCls(callback)))
                .observeOn(Schedulers.mainThread())
                .subscribe(new ApiRetryFunc<T>(disposableObserver,
                        config.retryCount,
                        config.retryDelayMillis,
                        new ApiRetryFunc.OnRetry<T>() {
                            @NonNull
                            @Override
                            public Observable.Observe<T> observe() {
                                return observable.subscribeOn(Schedulers.io())
                                        .map(new ApiFunc<T>(Utils.getFirstCls(callback)))
                                        .observeOn(Schedulers.mainThread());
                            }
                        }));
    }

    public static <T, R> void requestAsync(final Call call,
                                           final Observable<Response> observable,
                                           final Config config,
                                           final AsyncCallback<T, R> callback,
                                           final Object tag) {
        DisposableObserver<R> disposableObserver = new AsyncApiObserver<T, R>(call, callback, tag);
        if (tag != null) {
            RequestManagerImpl.getIns().add(tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(Utils.getFirstCls(callback)))
                .map(new MapFunc<T, R>(callback))
                .observeOn(Schedulers.mainThread())
                .subscribe(new ApiRetryFunc<R>(disposableObserver,
                        config.retryCount, config.retryDelayMillis,
                        new ApiRetryFunc.OnRetry<R>() {
                            @NonNull
                            @Override
                            public Observable.Observe<R> observe() {
                                return observable.subscribeOn(Schedulers.io())
                                        .map(new ApiFunc<T>(Utils.getFirstCls(callback)))
                                        .map(new MapFunc<T, R>(callback))
                                        .observeOn(Schedulers.mainThread());
                            }
                        }));
    }

    public static void requestDownload(final Call call,
                                       final Observable<Response> observable,
                                       final Config config,
                                       final String path, final String name,
                                       final ProgressCallback callback,
                                       final Object tag) {
        if (callback != null) {
            Observable.executeMain(new Runnable() {
                @Override
                public void run() {
                    callback.onStart();
                }
            });
        }
        DisposableObserver<Response> disposableObserver = new DownloadObserver(call,
                path, name, callback, tag);
        if (tag != null) {
            RequestManagerImpl.getIns().add(tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.defaultThread())
                .subscribe(new ApiRetryFunc<Response>(disposableObserver,
                        config.retryCount, config.retryDelayMillis,
                        new ApiRetryFunc.OnRetry<Response>() {
                            @NonNull
                            @Override
                            public Observable.Observe<Response> observe() {
                                return observable.subscribeOn(Schedulers.io())
                                        .observeOn(Schedulers.io());
                            }
                        }));
    }

    public static <T> void requestUpload(final Call call,
                                         final Observable<Response> observable,
                                         final Config config,
                                         final UploadCallback<T> callback,
                                         final Object tag) {
        DisposableObserver<T> disposableObserver = new UploadObserver<T>(call, callback, tag);
        if (tag != null) {
            RequestManagerImpl.getIns().add(tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(Utils.getFirstCls(callback)))
                .observeOn(Schedulers.mainThread())
                .subscribe(new ApiRetryFunc<T>(disposableObserver,
                        config.retryCount,
                        config.retryDelayMillis,
                        new ApiRetryFunc.OnRetry<T>() {
                            @NonNull
                            @Override
                            public Observable.Observe<T> observe() {
                                return observable.subscribeOn(Schedulers.io())
                                        .map(new ApiFunc<T>(Utils.getFirstCls(callback)))
                                        .observeOn(Schedulers.mainThread());
                            }
                        }));
    }
}
