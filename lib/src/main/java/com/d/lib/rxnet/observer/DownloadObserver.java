package com.d.lib.rxnet.observer;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.d.lib.rxnet.exception.ApiException;
import com.d.lib.rxnet.listener.DownloadCallBack;
import com.d.lib.rxnet.util.RxLog;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;

/**
 * Created by D on 2017/10/26.
 */
public class DownloadObserver<T extends ResponseBody> extends AbsObserver<T> {
    private static final int TASK_TIME = 200;

    private T data;
    private DownloadCallBack callBack;
    private String filePath;
    private String fileName;
    private Handler handler;
    private ProgressTask progressTask;
    private boolean progressTaskRunning;
    private long fileDownload, fileTotal;

    private static class ProgressTask implements Runnable {
        private final WeakReference<DownloadObserver> reference;

        ProgressTask(DownloadObserver observer) {
            this.reference = new WeakReference<>(observer);
        }

        @Override
        public void run() {
            DownloadObserver observer = reference.get();
            if (!observer.progressTaskRunning) {
                return;
            }
            observer.callBack.onProgresss(observer.fileDownload, observer.fileTotal);
        }
    }

    private void reStartTask() {
        stopTask();
        progressTaskRunning = true;
        handler.postDelayed(progressTask, TASK_TIME);
    }

    private void stopTask() {
        progressTaskRunning = false;
        handler.removeCallbacks(progressTask);
    }

    public DownloadObserver(String path, String name, DownloadCallBack callBack) {
        if (callBack == null) {
            throw new NullPointerException("this callback is null!");
        }
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("this path can not be empty!");
        }
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("this name can not be empty!");
        }
        this.filePath = path;
        this.fileName = name;
        this.callBack = callBack;
        this.handler = new Handler(Looper.getMainLooper());
        this.progressTask = new ProgressTask(this);
    }

    @Override
    public void onNext(T t) {
        RxLog.d("dsiner_th_ downloadOnNext: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
        this.data = t;
        writeResponseBodyToDisk(filePath, fileName, t);
    }

    private void writeResponseBodyToDisk(String path, String name, okhttp3.ResponseBody body) {

    }

    @Override
    public void onError(ApiException e) {
        callBack.onError(e);
    }

    @Override
    public void onComplete() {
    }

    public T getData() {
        return data;
    }

    private void start() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onStart(fileTotal);
            }
        });
        reStartTask();
    }

    private void complete() {
        stopTask();
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onComplete();
            }
        });
    }

    private void error(final IOException e) {
        stopTask();
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(new ApiException(e, -1));
            }
        });
    }
}
