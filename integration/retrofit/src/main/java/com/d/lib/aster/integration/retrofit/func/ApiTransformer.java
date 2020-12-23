package com.d.lib.aster.integration.retrofit.func;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.callback.UploadCallback;
import com.d.lib.aster.integration.retrofit.RequestManagerImpl;
import com.d.lib.aster.integration.retrofit.observer.ApiObserver;
import com.d.lib.aster.integration.retrofit.observer.AsyncApiObserver;
import com.d.lib.aster.integration.retrofit.observer.DownloadObserver;
import com.d.lib.aster.integration.retrofit.observer.UploadObserver;
import com.d.lib.aster.util.Utils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

/**
 * Transformer
 */
public class ApiTransformer {

    public static <T> void request(final Observable<ResponseBody> observable,
                                   final Config config,
                                   final SimpleCallback<T> callback,
                                   final Object tag) {
        DisposableObserver<T> disposableObserver = new ApiObserver<T>(callback, tag);
        if (tag != null) {
            RequestManagerImpl.getIns().add(tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(Utils.getFirstCls(callback)))
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis))
                .subscribe(disposableObserver);
    }

    public static <T, R> void requestAsync(final Observable<ResponseBody> observable,
                                           final Config config,
                                           final AsyncCallback<T, R> callback,
                                           final Object tag) {
        DisposableObserver<R> disposableObserver = new AsyncApiObserver<T, R>(callback, tag);
        if (tag != null) {
            RequestManagerImpl.getIns().add(tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(Utils.getFirstCls(callback)))
                .map(new MapFunc<T, R>(callback))
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis))
                .subscribe(disposableObserver);
    }

    public static void requestDownload(final Observable<ResponseBody> observable,
                                       final Config config,
                                       final String path, final String name,
                                       final ProgressCallback callback,
                                       final Object tag) {
        if (callback != null) {
            com.d.lib.aster.scheduler.Observable.executeMain(new Runnable() {
                @Override
                public void run() {
                    callback.onStart();
                }
            });
        }
        DisposableObserver<ResponseBody> disposableObserver = new DownloadObserver(path, name,
                callback, tag);
        if (tag != null) {
            RequestManagerImpl.getIns().add(tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis))
                .subscribe(disposableObserver);
    }

    public static <T> void requestUpload(final Observable<ResponseBody> observable,
                                         final Config config,
                                         final List<MultipartBody.Part> multipartBodyParts,
                                         final UploadCallback<T> callback,
                                         final Object tag) {
        DisposableObserver<T> disposableObserver = new UploadObserver<T>(multipartBodyParts, callback, tag);
        if (tag != null) {
            RequestManagerImpl.getIns().add(tag, disposableObserver);
        }
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new ApiFunc<T>(Utils.getFirstCls(callback)))
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis))
                .subscribe(disposableObserver);
    }

    public static <T> ObservableTransformer<T, T> norTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> apiResultObservable) {
                return apiResultObservable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retryWhen(new ApiRetryFunc(Config.getDefault().retryCount,
                                Config.getDefault().retryDelayMillis));
            }
        };
    }

    /**
     * e.g observable.compose(this.<T>norTransformer(callback))
     */
    protected <OTF> ObservableTransformer<ResponseBody, OTF> norTransformer(final Class<OTF> clazz,
                                                                            final Config config) {
        return new ObservableTransformer<ResponseBody, OTF>() {
            @Override
            public ObservableSource<OTF> apply(Observable<ResponseBody> apiResultObservable) {
                return apiResultObservable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .map(new ApiFunc<OTF>(clazz))
                        .observeOn(AndroidSchedulers.mainThread())
                        .retryWhen(new ApiRetryFunc(config.retryCount, config.retryDelayMillis));
            }
        };
    }

    public static <T> ObservableTransformer<T, T> norTransformer(final int retryCount, final int retryDelayMillis) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> apiResultObservable) {
                return apiResultObservable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retryWhen(new ApiRetryFunc(retryCount, retryDelayMillis));
            }
        };
    }
}
