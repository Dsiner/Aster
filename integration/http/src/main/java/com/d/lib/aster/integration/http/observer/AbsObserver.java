package com.d.lib.aster.integration.http.observer;

import com.d.lib.aster.scheduler.callback.DisposableObserver;
import com.d.lib.aster.utils.ULog;

import java.net.HttpURLConnection;

/**
 * Abstract Observer
 */
abstract class AbsObserver<T> extends DisposableObserver<T> {
    HttpURLConnection mConn;

    AbsObserver() {
    }

    @Override
    public void onError(Throwable e) {
        // Print error log
        ULog.e(e.getMessage());
    }

    @Override
    public void dispose() {
        super.dispose();
        if (mConn != null) {
            mConn.disconnect();
        }
    }
}
