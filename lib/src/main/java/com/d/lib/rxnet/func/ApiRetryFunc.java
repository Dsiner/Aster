package com.d.lib.rxnet.func;

import com.d.lib.rxnet.base.HttpConfig;
import com.d.lib.rxnet.exception.ApiException;
import com.d.lib.rxnet.util.RxLog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 重试机制
 */
public class ApiRetryFunc implements Function<Observable<? extends Throwable>, Observable<?>> {
    private final int maxRetries;
    private final long retryDelayMillis;
    private int retryCount;

    public ApiRetryFunc(int maxRetries, long retryDelayMillis) {
        this.maxRetries = maxRetries != -1 ? maxRetries : HttpConfig.getDefaultConfig().retryCount;
        this.retryDelayMillis = retryDelayMillis != -1 ? retryDelayMillis : HttpConfig.getDefaultConfig().retryDelayMillis;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        RxLog.d("dsiner_th_ retryInit: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());

        return observable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                RxLog.d("dsiner_th_ retryApply: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                if (++retryCount <= maxRetries && (throwable instanceof SocketTimeoutException
                        || throwable instanceof ConnectException)) {
                    RxLog.d("get response data error, it will try after " + retryDelayMillis
                            + " millisecond, retry count " + retryCount);
                    return Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);
                }
                return Observable.error(ApiException.handleException(throwable));
            }
        });
    }
}
