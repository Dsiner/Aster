package com.d.lib.aster.integration.http.observer;

import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.http.RequestManagerImpl;

/**
 * Observer with Sync Callback
 */
public class ApiObserver<R> extends AbsObserver<R> {
    private R mData;
    private Object mTag; // Request tag
    private SimpleCallback<R> mCallback;

    public ApiObserver(Object tag, SimpleCallback<R> callback) {
        if (callback == null) {
            throw new NullPointerException("This callback must not be null!");
        }
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
