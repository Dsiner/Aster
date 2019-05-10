package com.d.lib.aster.integration.retrofit.observer;

import android.support.annotation.Nullable;

import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.integration.okhttp3.body.UploadProgressRequestBody;
import com.d.lib.aster.integration.retrofit.RequestManagerImpl;
import com.d.lib.aster.utils.ULog;
import com.d.lib.aster.utils.Util;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Observer with Upload Callback
 * Created by D on 2017/10/26.
 */
public class UploadObserver<R> extends AbsObserver<R> {
    private final List<MultipartBody.Part> mMultipartBodyParts;
    private final Object mTag;
    private final SimpleCallback<R> mCallback;

    public UploadObserver(@Nullable Object tag,
                          @Nullable List<MultipartBody.Part> multipartBodyParts,
                          @Nullable SimpleCallback<R> callback) {
        this.mMultipartBodyParts = multipartBodyParts;
        this.mTag = tag;
        this.mCallback = callback;
    }

    public void cancel() {
        ULog.e("Request cancelled.");
        disposeProgress();
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

    @Override
    public void onComplete() {

    }
}
