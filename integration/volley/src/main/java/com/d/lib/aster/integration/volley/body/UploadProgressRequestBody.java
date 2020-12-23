package com.d.lib.aster.integration.volley.body;

import android.support.annotation.NonNull;

import com.d.lib.aster.base.MediaType;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.integration.volley.sink.BufferedSink;
import com.d.lib.aster.integration.volley.sink.ForwardingSink;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.util.ULog;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Upload progress request entity class
 */
public class UploadProgressRequestBody extends RequestBody {
    // The two progress update intervals cannot be less than 1000ms
    private static final int MIN_DELAY_TIME = 1000;

    private RequestBody mRequestBody;
    private ProgressCallback mCallback;
    private long mLastTime;

    public UploadProgressRequestBody(@NonNull RequestBody requestBody,
                                     @NonNull ProgressCallback callback) {
        this.mRequestBody = requestBody;
        this.mCallback = callback;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return mRequestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        onStartImpl();
        try {
            mRequestBody.writeTo(new CountingSink(sink));
            onSuccessImpl();
        } catch (final Throwable e) {
            e.printStackTrace();
            onErrorImpl(e);
            throw e;
        }
    }

    private void onStartImpl() {
        if (mCallback == null) {
            return;
        }
        Observable.executeMain(new Runnable() {
            @Override
            public void run() {
                mCallback.onStart();
            }
        });
    }

    private void onErrorImpl(final Throwable e) {
        if (mCallback == null) {
            return;
        }
        Observable.executeMain(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(e);
            }
        });
    }

    private void onSuccessImpl() {
        if (mCallback == null) {
            return;
        }
        Observable.executeMain(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess();
            }
        });
    }

    private final class CountingSink extends ForwardingSink {
        // Current byte length
        private long currentLength = 0L;
        // Total byte length, avoid calling the contentLength() method multiple times
        private long totalLength = 0L;

        CountingSink(BufferedSink sink) {
            super(sink);
        }

        @Override
        public void write(@NonNull DataOutputStream source, long byteCount) throws IOException {
            // Increase the number of bytes currently written
            currentLength += byteCount;
            // Get the value of contentLength, no longer call later
            if (totalLength == 0) {
                totalLength = contentLength();
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastTime >= MIN_DELAY_TIME || mLastTime == 0 || currentLength == totalLength) {
                mLastTime = currentTime;
                Observable.executeMain(new Runnable() {
                    @Override
                    public void run() {
                        ULog.d("Upload progress currentLength: " + currentLength
                                + " totalLength: " + totalLength);
                        mCallback.onProgress(currentLength, totalLength);
                    }
                });
            }
        }
    }
}
