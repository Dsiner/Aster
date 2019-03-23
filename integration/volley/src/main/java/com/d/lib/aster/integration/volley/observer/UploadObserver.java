package com.d.lib.aster.integration.volley.observer;

import android.support.annotation.Nullable;

import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.volley.RequestManagerImpl;
import com.d.lib.aster.integration.volley.client.ResponseBody;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.utils.Util;

/**
 * Observer with Upload Callback
 * Created by D on 2017/10/26.
 */
public class UploadObserver extends AbsObserver<ResponseBody> {
    private final Object mTag;
    private final SimpleCallback<ResponseBody> mCallback;

    public UploadObserver(Object tag, @Nullable SimpleCallback<ResponseBody> callback) {
        this.mTag = tag;
        this.mCallback = callback;
    }

    public void cancel() {
        dispose();
        if (mCallback == null) {
            return;
        }
        Observable.executeMain(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(new Exception("Request cancelled."));
            }
        });
    }

    @Override
    public void onNext(ResponseBody o) {
        RequestManagerImpl.getIns().remove(mTag);
        Util.printThread("Aster_thread uploadOnNext");
        if (mCallback == null) {
            return;
        }
        mCallback.onSuccess(o);
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
