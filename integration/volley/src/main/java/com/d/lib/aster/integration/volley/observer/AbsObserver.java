package com.d.lib.aster.integration.volley.observer;

import android.support.annotation.NonNull;

import com.d.lib.aster.scheduler.callback.DisposableObserver;
import com.d.lib.aster.utils.ULog;

/**
 * Abstract Observer
 */
abstract class AbsObserver<T> extends DisposableObserver<T> {

    AbsObserver() {
    }

    @Override
    public void onError(@NonNull Throwable e) {
        // Print error log
        ULog.e(e.getMessage());
    }
}
