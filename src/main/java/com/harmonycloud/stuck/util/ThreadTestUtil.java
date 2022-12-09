package com.harmonycloud.stuck.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadTestUtil {

    private static Logger log = LogManager.getLogger(ThreadTestUtil.class);

    public static void test(int corePoolSize, int maxPoolSize, long keepAliveTime,
                            int blockQueue, int requestCount) {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(blockQueue);
        ThreadFactory threadFactory = new NameTreadFactory();
        RejectedExecutionHandler handler = new MyIgnorePolicy();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
                TimeUnit.SECONDS, workQueue, threadFactory, handler);
        executor.prestartAllCoreThreads();
        for (int i = 0; i < requestCount; i++) {
            TestTask testTask = new TestTask(String.valueOf(i));
            executor.execute(testTask);
        }
        executor.shutdown();

    }

    static class NameTreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "my-thread-" + mThreadNum.getAndIncrement());
            log.info(t.getName() + " has been created");
            return t;
        }
    }

    public static class MyIgnorePolicy implements RejectedExecutionHandler {

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            doLog(r, e);
        }

        private void doLog(Runnable r, ThreadPoolExecutor e) {
            // 可做日志记录等
            log.info(r.toString() + " rejected");
//          System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
        }
    }

    static class TestTask implements Runnable {
        private String name;

        public TestTask(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                log.info(this.toString() + " is running!");
                Thread.sleep(1000); //让任务执行慢点
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "TestTask [name=" + name + "]";
        }
    }
}


