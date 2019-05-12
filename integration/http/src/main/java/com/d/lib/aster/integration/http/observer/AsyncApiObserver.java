package com.d.lib.aster.integration.http.observer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.integration.http.RequestManagerImpl;

import java.net.HttpURLConnection;

/**
 * Observer with Async Callback
 */
public class AsyncApiObserver<T, R> extends AbsObserver<R> {
    private R mData;
    private final AsyncCallback<T, R> mCallback;
    private final Object mTag; // Request tag

    public AsyncApiObserver(@Nullable final HttpURLConnection conn,
                            @NonNull AsyncCallback<T, R> callback,
                            @Nullable Object tag) {
        if (callback == null) {
            throw new NullPointerException("This callback must not be null!");
        }
        this.mConn = conn;
        this.mTag = tag;
        this.mCallback = callback;
    }

    @Override
    public void onNext(R r) {
        RequestManagerImpl.getIns().remove(mTag);
        if (isDisposed()) {
            return;
        }
        this.mData = r;
        mCallback.onSuccess(r);
    }

    @Override
    public void onError(Throwable e) {
        RequestManagerImpl.getIns().remove(mTag);
        if (isDisposed()) {
            return;
        }
        super.onError(e);
        mCallback.onError(e);
    }

    public R getData() {
        return mData;
    }
}
