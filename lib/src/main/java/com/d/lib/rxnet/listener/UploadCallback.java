package com.d.lib.rxnet.listener;

/**
 * UploadCallback
 * Created by D on 2017/10/24.
 */
public interface UploadCallback {
    void onProgress(long currentLength, long totalLength);

    void onError(Throwable e);

    void onComplete();
}
