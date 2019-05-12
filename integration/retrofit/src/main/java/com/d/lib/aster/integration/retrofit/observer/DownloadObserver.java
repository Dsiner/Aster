package com.d.lib.aster.integration.retrofit.observer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.integration.retrofit.RequestManagerImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.ResponseBody;

/**
 * Observer with Download Callback
 * Created by D on 2017/10/26.
 */
public class DownloadObserver extends AbsObserver<ResponseBody> {
    // The two progress update intervals cannot be less than 1000ms
    private static final int MIN_DELAY_TIME = 1000;

    private final String mPath;
    private final String mName;
    private final DownloadModel mDownModel = new DownloadModel();
    private final ProgressCallback mCallback;
    private final Object mTag;

    public DownloadObserver(final String path, final String name,
                            @Nullable ProgressCallback callback,
                            @Nullable final Object tag) {
        this.mPath = path;
        this.mName = name;
        this.mCallback = callback;
        this.mTag = tag;
    }

    @NonNull
    private File createFile() {
        File dir = new File(mPath);
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
        return new File(dir.getPath() + File.separator + mName);
    }

    public void cancel() {
        dispose();
        if (mCallback == null) {
            return;
        }
        com.d.lib.aster.scheduler.Observable.executeMain(new Runnable() {
            @Override
            public void run() {
                mCallback.onCancel();
            }
        });
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        saveFile(responseBody, createFile());
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        onErrorImpl(e);
    }

    @Override
    public void onComplete() {

    }

    private void saveFile(@NonNull ResponseBody resp, @NonNull File file) {
        InputStream inputStream = null;
        RandomAccessFile outputStream = null;
        try {
            int readLen;
            int currentLength = 0;
            byte[] buffer = new byte[4096];
            long lastTime = 0;

            inputStream = resp.byteStream();
            outputStream = new RandomAccessFile(file, "rw");
            outputStream.seek(0);

            mDownModel.totalLength = resp.contentLength();

            onProgressImpl(mDownModel.currentLength, mDownModel.totalLength);

            while ((readLen = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readLen);
                currentLength += readLen;
                mDownModel.currentLength = currentLength;

                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTime >= MIN_DELAY_TIME || lastTime == 0) {
                    lastTime = currentTime;
                    onProgressImpl(mDownModel.currentLength, mDownModel.totalLength);
                }
            }
            onSuccessImpl();
        } catch (IOException e) {
            e.printStackTrace();
            RequestManagerImpl.getIns().remove(mTag);
            if (!isDisposed()) {
                onErrorImpl(e);
            }
        } finally {
            okhttp3.internal.Util.closeQuietly(inputStream);
            okhttp3.internal.Util.closeQuietly(outputStream);
            okhttp3.internal.Util.closeQuietly(resp);
        }
    }

    private void onProgressImpl(final long currentLength, final long totalLength) {
        if (mCallback == null) {
            return;
        }
        com.d.lib.aster.scheduler.Observable.executeMain(new Runnable() {
            @Override
            public void run() {
                mCallback.onProgress(currentLength, totalLength);
            }
        });
    }

    private void onErrorImpl(final Throwable e) {
        RequestManagerImpl.getIns().remove(mTag);
        if (mCallback == null) {
            return;
        }
        com.d.lib.aster.scheduler.Observable.executeMain(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(e);
            }
        });
    }

    private void onSuccessImpl() {
        RequestManagerImpl.getIns().remove(mTag);
        if (mCallback == null) {
            return;
        }
        com.d.lib.aster.scheduler.Observable.executeMain(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess();
            }
        });
    }

    public static class DownloadModel {
        public long currentLength;
        public long totalLength;
    }
}
