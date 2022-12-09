package com.harmonycloud.stuck.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.RecursiveTask;

/**
 * 展示forkJoin场景
 */
public class ForkJoinTask extends RecursiveTask<Integer> {

    private static Logger log = LogManager.getLogger(ForkJoinTask.class);

    public static final int threshold = 3;
    private int start;
    private int end;

    public ForkJoinTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;

        boolean forkFlag = (end - start) >= threshold;
        if (forkFlag) {
            // 如果任务大于阈值，就分裂成两个子任务计算
            int middle = (start + end) / 2;
            ForkJoinTask leftTask = new ForkJoinTask(start, middle);
            ForkJoinTask rightTask = new ForkJoinTask(middle + 1, end);
            // 执行子任务
            log.info("fork子任务{}~{}开始", start, end);
            log.info("leftTask start={},end={}", start, middle);
            leftTask.fork();
            log.info("rightTask start={},end={}", middle + 1, end);
            rightTask.fork();
            log.info("fork子任务{}~{}结束", start, end);
            // 等待任务执行结束合并其结果
            int leftResult = leftTask.join();
            int rightResult = rightTask.join();
            // 合并子任务
            sum = leftResult + rightResult;
            log.info("join子任务结果{}～{}，leftTaskResult={}，rightTaskResult={}，合并result={}",
                    start, end, leftResult, rightResult, sum);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                log.error("sleep error", e);
            }
        } else {
            //如果任务足够小就计算任务
            log.info("任务数足够小,开始执行子任务计算start={},end={}", start, end);
            for (int i = start; i <= end; i++) {
                sum += i;
                log.info("子任务计算累加i={}，sum={}", i, sum);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    log.error("sleep error", e);
                }
            }
        }
        return sum;
    }

}