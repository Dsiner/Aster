package com.d.lib.rxnet.listener;

/**
 * SimpleCallback
 * Created by D on 2017/10/24.
 */
public interface SimpleCallack<R> {
    void onSuccess(R response);

    void onError(Throwable e);
}
