package com.d.lib.aster.integration.retrofit;

import android.support.annotation.Nullable;

import com.d.lib.aster.base.IRequestManager;
import com.d.lib.aster.integration.retrofit.observer.DownloadObserver;
import com.d.lib.aster.integration.retrofit.observer.UploadObserver;

import java.util.HashMap;
import java.util.Set;

import io.reactivex.disposables.Disposable;

/**
 * Request management to facilitate mid-way cancellation of requests
 */
public class RequestManagerImpl implements IRequestManager<Disposable> {
    private HashMap<Object, Disposable> mHashMap;

    private static class Singleton {
        private static final RequestManagerImpl INSTANCE = new RequestManagerImpl();
    }

    public static RequestManagerImpl getIns() {
        return Singleton.INSTANCE;
    }

    private RequestManagerImpl() {
        mHashMap = new HashMap<>();
    }

    @Override
    public synchronized void add(Object tag, Disposable disposable) {
        if (tag == null || disposable == null) {
            return;
        }
        mHashMap.put(tag, disposable);
    }

    @Override
    public synchronized void remove(Object tag) {
        if (mHashMap.isEmpty() || tag == null) {
            return;
        }
        mHashMap.remove(tag);
    }

    @Override
    public synchronized void removeAll() {
        if (mHashMap.isEmpty()) {
            return;
        }
        mHashMap.clear();
    }

    @Override
    public synchronized boolean canceled(Object tag) {
        if (tag == null) {
            return false;
        }
        boolean canceled = mHashMap.containsKey(tag);
        cancel(tag);
        return canceled;
    }

    @Override
    public synchronized void cancel(Object tag) {
        if (tag == null) {
            return;
        }
        Disposable value = mHashMap.remove(tag);
        cancelImpl(value);
    }

    private void cancelImpl(@Nullable Disposable value) {
        if (value != null && !value.isDisposed()) {
            if (value instanceof DownloadObserver) {
                ((DownloadObserver) value).cancel();
            } else if (value instanceof UploadObserver) {
                ((UploadObserver) value).cancel();
            } else {
                value.dispose();
            }
        }
    }

    @Override
    public synchronized void cancelAll() {
        if (mHashMap.isEmpty()) {
            return;
        }
        HashMap<Object, Disposable> temp = new HashMap<>(mHashMap);
        mHashMap.clear();
        Set<Object> keys = temp.keySet();
        for (Object k : keys) {
            cancelImpl(temp.get(k));
        }
    }
}
