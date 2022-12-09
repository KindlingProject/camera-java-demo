package com.harmonycloud.stuck.util;

import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeadLockUtil {
    private static Logger LOG = LogManager.getLogger(DeadLockUtil.class);

    private final Object lockObject1 = new Object();
    private final Object lockObject2 = new Object();
    
    private CountDownLatch latch1 = new CountDownLatch(1);
    private CountDownLatch latch2 = new CountDownLatch(1);
    
    public void lock1() {
        synchronized (lockObject1) {
            latch1.countDown();
            try {
                latch2.await();
                synchronized (lockObject2) {
                    LOG.info("Finish Lock1");
                }
            } catch (InterruptedException e) {
                LOG.error("Fail to await latch", e);
            }
        }
    }
    
    public void lock2() {
        synchronized (lockObject2) {
            latch2.countDown();
            try {
                latch1.await();
                synchronized (lockObject1) {
                    LOG.info("Finish Lock2");
                }
            } catch (InterruptedException e) {
                LOG.error("Fail to await latch", e);
            }
        }
    }
}
