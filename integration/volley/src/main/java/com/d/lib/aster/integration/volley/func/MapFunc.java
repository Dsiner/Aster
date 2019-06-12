package com.d.lib.aster.integration.volley.func;

import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.scheduler.callback.Function;
import com.d.lib.aster.utils.Util;

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
        Util.printThread("Aster_thread map apply");
        return mCallback.apply(t);
    }
}
