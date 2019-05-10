package com.d.lib.aster.integration.http.observer;

import android.support.annotation.Nullable;

import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.http.RequestManagerImpl;
import com.d.lib.aster.utils.ULog;
import com.d.lib.aster.utils.Util;

import java.net.HttpURLConnection;

/**
 * Observer with Upload Callback
 * Created by D on 2017/10/26.
 */
public class UploadObserver<R> extends AbsObserver<R> {
    private final Object mTag;
    private final SimpleCallback<R> mCallback;

    public UploadObserver(Object tag,
                          @Nullable final HttpURLConnection conn,
                          @Nullable SimpleCallback<R> callback) {
        this.mTag = tag;
        this.mConn = conn;
        this.mCallback = callback;
    }

    public void cancel() {
        ULog.e("Request cancelled.");
        dispose();
    }

    @Override
    public void onNext(R r) {
        RequestManagerImpl.getIns().remove(mTag);
        Util.printThread("Aster_thread uploadOnNext");
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
