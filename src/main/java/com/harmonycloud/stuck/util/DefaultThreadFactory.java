package com.harmonycloud.stuck.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {
    private final String name;
    private final boolean daemon;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    public DefaultThreadFactory(String name, boolean daemon) {
        this.name = name;
        this.daemon = daemon;
    }

    public Thread newThread(Runnable runnable) {
        int num = threadNumber.getAndIncrement();
        String threadName = name;
        if (num != 1) {
            threadName += "-" + num;
        }
        Thread thread = new Thread(runnable, threadName);
        if (daemon) {
            thread.setDaemon(true);
        }
        return thread;
    }
}
