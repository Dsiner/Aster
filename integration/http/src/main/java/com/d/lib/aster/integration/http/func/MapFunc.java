package com.d.lib.aster.integration.http.func;

import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.scheduler.callback.Function;
import com.d.lib.aster.util.Utils;

/**
 * Map with AsyncCallback
 */
public class MapFunc<T, R> implements Function<T, R> {
    private AsyncCallback<T, R> mCallback;

    public MapFunc(AsyncCallback<T, R> callback) {
        this.mCallback = callback;
    }

    @Override
    public R apply(T t) throws Exception {
        Utils.printThread("Aster_thread map apply");
        return mCallback.apply(t);
    }
}
