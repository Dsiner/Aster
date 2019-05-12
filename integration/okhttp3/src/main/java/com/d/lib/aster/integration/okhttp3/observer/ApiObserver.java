package com.d.lib.aster.integration.okhttp3.observer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.okhttp3.RequestManagerImpl;

import okhttp3.Call;

/**
 * Observer with Sync Callback
 */
public class ApiObserver<R> extends AbsObserver<R> {
    private R mData;
    @NonNull
    private final SimpleCallback<R> mCallback;
    private final Object mTag; // Request tag

    public ApiObserver(@Nullable final Call call,
                       @Nullable SimpleCallback<R> callback,
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
        mData = r;
        mCallback.onSuccess(r);
    }

    @Override
    public void onError(@NonNull Throwable e) {
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
