package com.d.aster.util;

import com.d.lib.aster.util.ULog;

public class Logger {

    public static void d(String msg) {
        d(msg, true);
    }

    public static void d(String msg, boolean withThread) {
        String currentThread = "thread--> id: " + Thread.currentThread().getId()
                + " name: " + Thread.currentThread().getName();
        ULog.d(withThread ? msg + " <" + currentThread + ">" : msg);
    }
}
