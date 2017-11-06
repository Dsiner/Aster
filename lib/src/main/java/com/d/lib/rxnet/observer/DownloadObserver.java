package com.d.lib.rxnet.observer;

import com.d.lib.rxnet.exception.ApiException;
import com.d.lib.rxnet.listener.DownloadCallBack;
import com.d.lib.rxnet.request.DownloadRequest;
import com.d.lib.rxnet.util.RxLog;

/**
 * Created by D on 2017/10/26.
 */
public class DownloadObserver extends AbsObserver<DownloadRequest.DownloadModel> {
    private DownloadCallBack callBack;

    public DownloadObserver(DownloadCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onNext(DownloadRequest.DownloadModel t) {
        RxLog.d("dsiner_th_ downloadOnNext: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
        callBack.onProgresss(t.downloadSize, t.totalSize);
    }

    @Override
    public void onError(ApiException e) {
        callBack.onError(e);
    }

    @Override
    public void onComplete() {
        callBack.onComplete();
    }
}
