package com.d.lib.rxnet.observer;

import android.support.annotation.Nullable;

import com.d.lib.rxnet.base.RequestManager;
import com.d.lib.rxnet.callback.SimpleCallback;
import com.d.lib.rxnet.utils.Util;

import okhttp3.ResponseBody;

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
        Util.executeMain(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(new Exception("Request cancelled."));
            }
        });
    }

    @Override
    public void onNext(ResponseBody o) {
        RequestManager.getIns().remove(mTag);
        Util.printThread("RxNet_theard uploadOnNext");
        if (mCallback == null) {
            return;
        }
        mCallback.onSuccess(o);
    }

    @Override
    public void onError(Throwable e) {
        RequestManager.getIns().remove(mTag);
        super.onError(e);
        if (mCallback == null) {
            return;
        }
        mCallback.onError(e);
    }

    @Override
    public void onComplete() {

    }
}
