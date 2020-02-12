package com.d.lib.aster.integration.volley.sink;

import android.support.annotation.NonNull;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * ForwardingSink
 * Created by D on 2020/2/12.
 */
public abstract class ForwardingSink extends BufferedSink {
    private BufferedSink delegate;

    public ForwardingSink(BufferedSink sink) {
        this.delegate = sink;
    }

    @Override
    public DataOutputStream getDataOutputStream() {
        return delegate.getDataOutputStream();
    }

    @Override
    public void write(byte[] b) throws IOException {
        delegate.write(b);
        write(getDataOutputStream(), b.length);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        delegate.write(b, off, len);
        write(getDataOutputStream(), len);
    }

    @Override
    public void writeUtf8(String string) throws IOException {
        delegate.writeUtf8(string);
    }

    public void write(@NonNull DataOutputStream source, long byteCount) throws IOException {

    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public void flush() throws IOException {
        delegate.flush();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}