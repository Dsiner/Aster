package com.d.lib.rxnet.func;

import com.d.lib.rxnet.listener.AsyncCallBack;
import com.d.lib.rxnet.util.RxLog;

import io.reactivex.functions.Function;

/**
 * T转
 */
public class MapFunc<T, R> implements Function<T, R> {
    private AsyncCallBack<T, R> callback;

    public MapFunc(AsyncCallBack<T, R> callback) {
        this.callback = callback;
    }

    @Override
    public R apply(T responseBody) throws Exception {
        RxLog.d("dsiner_th_map: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
        return callback.apply(responseBody);
    }
}
