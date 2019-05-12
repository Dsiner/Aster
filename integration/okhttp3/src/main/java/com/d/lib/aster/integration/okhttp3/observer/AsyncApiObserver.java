package com.d.lib.aster.integration.okhttp3.observer;

import android.support.annotation.Nullable;

import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.integration.okhttp3.RequestManagerImpl;

import okhttp3.Call;

/**
 * Observer with Async Callback
 */
public class AsyncApiObserver<T, R> extends AbsObserver<R> {
    private R mData;
    private final AsyncCallback<T, R> mCallback;
    private final Object mTag; // Request tag

    public AsyncApiObserver(@Nullable final Call call,
                            @Nullable AsyncCallback<T, R> callback,
                            Object tag) {
        if (callback == null) {
            throw new NullPointerException("This callback must not be null!");
        }
        this.mCall = call;
        this.mCallback = callback;
        this.mTag = tag;
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
