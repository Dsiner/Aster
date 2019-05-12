package com.d.lib.aster.utils;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Util
 * Created by D on 2017/10/25.
 */
public class Util {
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset UTF_16_BE = Charset.forName("UTF-16BE");
    public static final Charset UTF_16_LE = Charset.forName("UTF-16LE");
    public static final Charset UTF_32_BE = Charset.forName("UTF-32BE");
    public static final Charset UTF_32_LE = Charset.forName("UTF-32LE");

    private Util() {
    }

    /**
     * Print the thread information of the current code
     */
    public static void printThread(String tag) {
        ULog.d(tag + " current thread--> id: " + Thread.currentThread().getId()
                + " name: " + Thread.currentThread().getName());
    }

    /**
     * Get the first generic type, interface only
     */
    public static <T> Class<T> getFirstCls(T t) {
        Type[] types = t.getClass().getGenericInterfaces();
        Type[] params = ((ParameterizedType) types[0]).getActualTypeArguments();
        Class<T> reponseClass = (Class) params[0];
        return reponseClass;
    }

    public static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * Delete file/folder
     */
    public static void deleteFile(File file) {
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length <= 0) {
                return;
            }
            for (File f : files) {
                deleteFile(f);
            }
            // If you want to keep the folder, just delete the file, please comment this line
            file.delete();
        } else if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Returns an immutable copy of {@code list}.
     */
    public static <T> List<T> immutableList(List<T> list) {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }

    /**
     * Closes {@code closeable}, ignoring any checked exceptions. Does nothing if {@code closeable} is
     * null.
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }
}
