package com.d.lib.aster.integration.volley.sink;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.Flushable;
import java.io.IOException;

/**
 * Sink
 * Created by D on 2020/2/12.
 */
public interface Sink extends Closeable, Flushable {
    DataOutputStream getDataOutputStream();

    /**
     * Pushes all buffered bytes to their final destination.
     */
    @Override
    void flush() throws IOException;

    /**
     * Pushes all buffered bytes to their final destination and releases the
     * resources held by this sink. It is an error to write a closed sink. It is
     * safe to close a sink more than once.
     */
    @Override
    void close() throws IOException;
}
