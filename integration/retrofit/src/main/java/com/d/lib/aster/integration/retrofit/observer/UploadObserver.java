package com.d.lib.aster.integration.retrofit.observer;

import android.support.annotation.Nullable;

import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.okhttp3.body.UploadProgressRequestBody;
import com.d.lib.aster.integration.retrofit.RequestManagerImpl;
import com.d.lib.aster.utils.Util;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Observer with Upload Callback
 * Created by D on 2017/10/26.
 */
public class UploadObserver extends AbsObserver<ResponseBody> {
    private final List<MultipartBody.Part> mMultipartBodyParts;
    private final Object mTag;
    private final SimpleCallback<ResponseBody> mCallback;

    public UploadObserver(@Nullable Object tag,
                          @Nullable List<MultipartBody.Part> multipartBodyParts,
                          @Nullable SimpleCallback<ResponseBody> callback) {
        this.mMultipartBodyParts = multipartBodyParts;
        this.mTag = tag;
        this.mCallback = callback;
    }

    public void cancel() {
        disposeProgress();
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

    private void disposeProgress() {
        if (mMultipartBodyParts != null) {
            for (MultipartBody.Part part : mMultipartBodyParts) {
                RequestBody body = part.body();
                if (body == null) {
                    continue;
                }
                if (body instanceof UploadProgressRequestBody) {
                    ((UploadProgressRequestBody) body).dispose();
                }
            }
        }
        dispose();
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

    @Override
    public void onComplete() {

    }
}
