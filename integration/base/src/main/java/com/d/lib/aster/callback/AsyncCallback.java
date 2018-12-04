package com.d.lib.aster.callback;

import io.reactivex.annotations.NonNull;

/**
 * AsyncCallback
 * Created by D on 2017/10/24.
 */
public interface AsyncCallback<T, R> extends SimpleCallback<R> {
    R apply(@NonNull T t) throws Exception;
}
