package com.harmonycloud.stuck.util.userCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyThreadTask implements Runnable {

    private static Logger log = LogManager.getLogger(MyThreadTask.class);

    private long startTime;

    private Integer sleepTime;


    public MyThreadTask(long startTime, Integer sleepTime) {
        this.startTime = startTime;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        try {
            log.info("线程" + Thread.currentThread().getName() + "开始执行工作");
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("线程" + Thread.currentThread().getName() + "执行任务已耗时 = " + (System.currentTimeMillis() - startTime));
    }
}
