package com.d.lib.aster.integration.retrofit.func;

import com.d.lib.aster.base.Config;
import com.d.lib.aster.retry.RetryException;
import com.d.lib.aster.util.ULog;
import com.d.lib.aster.util.Utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Retry Func
 */
public class ApiRetryFunc implements Function<Observable<? extends Throwable>, Observable<?>> {
    private final int mMaxRetries;
    private final long mRetryDelayMillis;
    private int mRetryCount;

    public ApiRetryFunc(int maxRetries, long retryDelayMillis) {
        this.mMaxRetries = maxRetries != -1 ? maxRetries : Config.getDefault().retryCount;
        this.mRetryDelayMillis = retryDelayMillis != -1 ? retryDelayMillis : Config.getDefault().retryDelayMillis;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        Utils.printThread("Aster_thread retryInit");
        return observable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                Utils.printThread("Aster_thread retryApply");
                if (mRetryCount < mMaxRetries && throwable instanceof RetryException) {
                    mRetryCount++;
                    ULog.d("Get response data error, it will try after " + mRetryDelayMillis
                            + " millisecond, retry count " + mRetryCount + "/" + mMaxRetries);
                    return Observable.timer(mRetryDelayMillis, TimeUnit.MILLISECONDS);
                }
                return Observable.error(throwable);
            }
        });
    }
}
