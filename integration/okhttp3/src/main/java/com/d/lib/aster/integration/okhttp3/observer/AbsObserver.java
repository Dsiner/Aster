package com.d.lib.aster.integration.okhttp3.observer;

import android.support.annotation.NonNull;

import com.d.lib.aster.scheduler.callback.DisposableObserver;
import com.d.lib.aster.util.ULog;

import okhttp3.Call;

/**
 * Abstract Observer
 */
abstract class AbsObserver<T> extends DisposableObserver<T> {
    Call mCall;

    AbsObserver() {
    }

    @Override
    public void onError(@NonNull Throwable e) {
        // Print error log
        ULog.e(e.getMessage());
    }

    @Override
    public void dispose() {
        super.dispose();
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
    }
}
