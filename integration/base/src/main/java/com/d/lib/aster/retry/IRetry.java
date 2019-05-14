package com.d.lib.aster.retry;

import android.support.annotation.NonNull;

public interface IRetry {
    void retry();

    void onError(@NonNull Throwable e);
}
