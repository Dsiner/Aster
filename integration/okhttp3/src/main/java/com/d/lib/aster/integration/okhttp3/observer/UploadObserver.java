package com.d.lib.aster.integration.okhttp3.observer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.d.lib.aster.callback.UploadCallback;
import com.d.lib.aster.integration.okhttp3.RequestManagerImpl;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.util.ULog;
import com.d.lib.aster.util.Utils;

import okhttp3.Call;

/**
 * Observer with Upload Callback
 * Created by D on 2017/10/26.
 */
public class UploadObserver<R> extends AbsObserver<R> {
    private R mData;
    @NonNull
    private final UploadCallback<R> mCallback;
    private final Object mTag;

    public UploadObserver(@Nullable Call call,
                          @Nullable UploadCallback<R> callback,
                          Object tag) {
        if (callback == null) {
            throw new NullPointerException("This callback must not be null!");
        }
        this.mCall = call;
        this.mCallback = callback;
        this.mTag = tag;
    }

    public void cancel() {
        ULog.e("Request cancelled.");
        dispose();
        Observable.executeMain(new Runnable() {
            @Override
            public void run() {
                mCallback.onCancel();
            }
        });
    }

    @Override
    public void onNext(R r) {
        RequestManagerImpl.getIns().remove(mTag);
        Utils.printThread("Aster_thread uploadOnNext");
        mData = r;
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
