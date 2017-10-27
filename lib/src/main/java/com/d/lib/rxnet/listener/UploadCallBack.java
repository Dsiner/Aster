package com.d.lib.rxnet.listener;

import com.d.lib.rxnet.exception.ApiException;

/**
 * Created by D on 2017/10/24.
 */
public interface UploadCallBack<T> {
    void onProgresss(int count, int countDone, int position, long progress, long length, boolean done);

    void onError(ApiException e);
}
