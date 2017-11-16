package com.d.lib.rxnet.listener;

import com.d.lib.rxnet.exception.ApiException;

/**
 * Created by D on 2017/10/24.
 */
public interface DownloadCallBack {
    void onProgress(long currentLength, long totalLength);

    void onError(ApiException e);

    void onComplete();
}
