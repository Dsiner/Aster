package com.d.lib.aster.retry;

public abstract class RetryException extends RuntimeException {
    public abstract void run(IRetry retry);
}
