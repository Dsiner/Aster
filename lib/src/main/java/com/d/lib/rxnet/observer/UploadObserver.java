package com.d.lib.rxnet.observer;

import com.d.lib.rxnet.exception.ApiException;
import com.d.lib.rxnet.listener.UploadCallBack;
import com.d.lib.rxnet.util.RxLog;

/**
 * Created by D on 2017/10/26.
 */
public class UploadObserver extends AbsObserver<Object> {
    private UploadCallBack callBack;

    public UploadObserver(UploadCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onNext(Object t) {
        RxLog.d("dsiner_th_ uploadOnNext: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
        callBack.onComplete();
    }

    @Override
    public void onError(ApiException e) {
        callBack.onError(e);
    }

    @Override
    public void onComplete() {

    }
}
