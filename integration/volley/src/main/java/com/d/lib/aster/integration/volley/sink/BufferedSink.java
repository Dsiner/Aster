package com.d.lib.aster.integration.volley.sink;

import com.d.lib.aster.utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * BufferedSink
 * Created by D on 2020/2/12.
 */
public class BufferedSink implements Sink {
    final DataOutputStream outputStream;

    public BufferedSink() {
        this.outputStream = new DataOutputStream(new ByteArrayOutputStream());
    }

    public BufferedSink(DataOutputStream stream) {
        this.outputStream = stream;
    }

    @Override
    public DataOutputStream getDataOutputStream() {
        return outputStream;
    }

    public void write(byte b[]) throws IOException {
        getDataOutputStream().write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
        getDataOutputStream().write(b, off, len);
    }

    public void writeUtf8(String string) throws IOException {
        write(string.getBytes(Util.UTF_8));
    }

    public int size() {
        return getDataOutputStream().size();
    }

    @Override
    public void flush() throws IOException {
        getDataOutputStream().flush();
    }

    @Override
    public void close() throws IOException {
        getDataOutputStream().close();
    }
}
