package com.d.lib.aster.integration.retrofit.observer;

import android.support.annotation.NonNull;

import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.integration.retrofit.RequestManagerImpl;

/**
 * Observer with Async Callback
 */
public class AsyncApiObserver<T, R> extends AbsObserver<R> {
    private R mData;
    private AsyncCallback<T, R> mCallback;
    private Object mTag; // Request tag

    public AsyncApiObserver(@NonNull AsyncCallback<T, R> callback, Object tag) {
        if (callback == null) {
            throw new NullPointerException("This callback must not be null!");
        }
        this.mCallback = callback;
        this.mTag = tag;
    }

    @Override
    public void onNext(R r) {
        RequestManagerImpl.getIns().remove(mTag);
        this.mData = r;
        mCallback.onSuccess(r);
    }

    @Override
    public void onError(Throwable e) {
        RequestManagerImpl.getIns().remove(mTag);
        super.onError(e);
        mCallback.onError(e);
    }

    @Override
    public void onComplete() {
    }

    public R getData() {
        return mData;
    }
}
