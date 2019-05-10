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
    private Object mTag; // Request tag
    private SimpleCallback<R> mCallback;

    public ApiObserver(Object tag,
                       @Nullable final Call call,
                       @Nullable SimpleCallback<R> callback) {
        if (callback == null) {
            throw new NullPointerException("This callback must not be null!");
        }
        this.mTag = tag;
        this.mCall = call;
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
