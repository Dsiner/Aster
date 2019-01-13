package com.d.lib.aster.base;

/**
 * Request management to facilitate mid-way cancellation of requests
 */
public interface IRequestManager<Observer> {
    void add(Object tag, Observer disposable);

    void remove(Object tag);

    void removeAll();

    boolean canceled(Object tag);

    void cancel(Object tag);

    void cancelAll();
}
