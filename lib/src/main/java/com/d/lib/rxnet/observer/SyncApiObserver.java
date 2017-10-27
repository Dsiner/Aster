package com.d.lib.rxnet.observer;

import com.d.lib.rxnet.exception.ApiException;
import com.d.lib.rxnet.listener.AsyncCallBack;

/**
 * 包含回调的订阅者，如果订阅这个，上层在不使用订阅者的情况下可获得回调
 */
public class SyncApiObserver<T, R> extends AbsObserver<R> {
    private R data;
    private AsyncCallBack<T, R> callBack;

    public SyncApiObserver(AsyncCallBack<T, R> callBack) {
        if (callBack == null) {
            throw new NullPointerException("this callback is null!");
        }
        this.callBack = callBack;
    }

    @Override
    public void onNext(R t) {
        this.data = t;
        callBack.onSuccess(t);
    }

    @Override
    public void onError(ApiException e) {
        callBack.onError(e);
    }

    @Override
    public void onComplete() {
    }

    public R getData() {
        return data;
    }
}
