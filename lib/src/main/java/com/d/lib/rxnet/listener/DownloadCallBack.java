package com.d.lib.rxnet.listener;

import com.d.lib.rxnet.exception.ApiException;

/**
 * Created by D on 2017/10/24.
 */
public interface DownloadCallBack {
    void onStart(long total);

    void onProgresss(long download, long total);

    void onComplete();

    void onError(ApiException e);
}
