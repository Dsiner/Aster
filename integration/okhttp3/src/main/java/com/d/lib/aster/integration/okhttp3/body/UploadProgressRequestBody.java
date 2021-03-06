package com.d.lib.aster.integration.okhttp3.body;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.util.ULog;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Upload progress request entity class
 */
public class UploadProgressRequestBody<T> extends RequestBody {
    // The two progress update intervals cannot be less than 1000ms
    private static final int MIN_DELAY_TIME = 1000;

    private RequestBody mRequestBody;
    private ProgressCallback mCallback;
    private long mLastTime;
    private AtomicBoolean disposed = new AtomicBoolean(false);

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
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        onStartImpl();
        try {
            BufferedSink bufferedSink = Okio.buffer(new CountingSink(sink));
            mRequestBody.writeTo(bufferedSink);
            bufferedSink.flush();
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
        if (isDisposed()) {
            if (mCallback != null) {
                mCallback.onCancel();
            }
            return;
        }
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

    public final boolean isDisposed() {
        return disposed.get();
    }

    public void dispose() {
        disposed.set(true);
    }

    private final class CountingSink extends ForwardingSink {
        // Current byte length
        private long currentLength = 0L;
        // Total byte length, avoid calling the contentLength() method multiple times
        private long totalLength = 0L;

        CountingSink(Sink sink) {
            super(sink);
        }

        @SuppressLint("CheckResult")
        @Override
        public void write(@NonNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            // Get the value of contentLength, no longer call later
            if (totalLength == 0) {
                totalLength = contentLength();
            }
            // Increase the number of bytes currently written
            currentLength += byteCount;
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastTime >= MIN_DELAY_TIME || mLastTime == 0 || currentLength == totalLength) {
                mLastTime = currentTime;
                Observable.executeMain(new Runnable() {
                    @Override
                    public void run() {
                        ULog.d("Upload progress currentLength: " + currentLength
                                + " totalLength: " + totalLength);
                        if (mCallback != null) {
                            mCallback.onProgress(currentLength, totalLength);
                        }
                    }
                });
            }
        }
    }
}
