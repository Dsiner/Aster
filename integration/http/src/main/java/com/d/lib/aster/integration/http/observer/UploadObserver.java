package com.d.lib.aster.integration.http.observer;

import android.support.annotation.Nullable;

import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.http.RequestManagerImpl;
import com.d.lib.aster.integration.http.client.Call;
import com.d.lib.aster.util.ULog;
import com.d.lib.aster.util.Utils;

/**
 * Observer with Upload Callback
 * Created by D on 2017/10/26.
 */
public class UploadObserver<R> extends AbsObserver<R> {
    private final SimpleCallback<R> mCallback;
    private final Object mTag;

    public UploadObserver(@Nullable final Call call,
                          @Nullable SimpleCallback<R> callback,
                          Object tag) {
        this.mCall = call;
        this.mCallback = callback;
        this.mTag = tag;
    }

    public void cancel() {
        ULog.e("Request cancelled.");
        dispose();
    }

    @Override
    public void onNext(R r) {
        RequestManagerImpl.getIns().remove(mTag);
        Utils.printThread("Aster_thread uploadOnNext");
        if (mCallback == null) {
            return;
        }
        mCallback.onSuccess(r);
    }

    @Override
    public void onError(Throwable e) {
        RequestManagerImpl.getIns().remove(mTag);
        if (isDisposed()) {
            return;
        }
        super.onError(e);
        if (mCallback == null) {
            return;
        }
        mCallback.onError(e);
    }
}
