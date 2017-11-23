package com.d.lib.rxnet.observer;

import com.d.lib.rxnet.exception.ApiException;
import com.d.lib.rxnet.util.RxLog;

import io.reactivex.observers.DisposableObserver;

/**
 * API统一订阅者
 */
abstract class AbsObserver<T> extends DisposableObserver<T> {

    AbsObserver() {
    }

    @Override
    public void onError(Throwable e) {
        RxLog.e(e.getMessage());
        if (e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            onError(new ApiException(-1, e));
        }
    }

    protected abstract void onError(ApiException e);
}
