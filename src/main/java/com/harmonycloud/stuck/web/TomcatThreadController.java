package com.harmonycloud.stuck.web;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmonycloud.stuck.bean.BigResult;
import com.harmonycloud.stuck.bean.Result;
import com.harmonycloud.stuck.util.ForkJoinStealTask;
import com.harmonycloud.stuck.util.ForkJoinTask;
import com.harmonycloud.stuck.util.ThreadTestUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;


@RestController
@RequestMapping("/")
public class TomcatThreadController {

    private static Logger LOG = LogManager.getLogger(TomcatThreadController.class);


    @RequestMapping(value = "/forkJoin/{endCount}", method = RequestMethod.GET)
    public Result forkJoin(@PathVariable("endCount") int endCount) {

        try {
            LOG.info("开始执行请求, start time={}", System.currentTimeMillis());
            ForkJoinPool forkjoinPool = new ForkJoinPool();
            ForkJoinTask task = new ForkJoinTask(1, endCount);
            forkjoinPool.submit(task);
            LOG.info("请求执行结束, end time={}", System.currentTimeMillis());
            Thread.sleep(500);
            return Result.success("Success, result= ");
        } catch (Exception e) {
            return Result.fail("计算失败" + e);
        } finally {
            LOG.info("我是finally的内容");
        }

    }

    @RequestMapping(value = "/forkJoinInvoke/{endCount}", method = RequestMethod.GET)
    public Result forkJoinInvoke(@PathVariable("endCount") int endCount) {

        try {
            LOG.info("开始执行请求, start time={}", System.currentTimeMillis());
            ForkJoinPool forkjoinPool = new ForkJoinPool();
            Integer result = 0;
            //生成一个计算任务，计算1+2+3+4+...+endCount
            ForkJoinTask task = new ForkJoinTask(1, endCount);
            //执行一个任务
            forkjoinPool.invoke(task);
            LOG.info("请求执行结束, end time={}", System.currentTimeMillis());
            Thread.sleep(500);
            return Result.success("Success, result= " + result);
        } catch (Exception e) {
            return Result.fail("计算失败" + e);
        }

    }


    @RequestMapping(value = "/forkJoinSteal/{endCount}", method = RequestMethod.GET)
    public Result forkJoinSteal(@PathVariable("endCount") int endCount) {

        try {
            LOG.info("开始执行请求, start time={}", System.currentTimeMillis());
            ForkJoinPool forkjoinPool = new ForkJoinPool();
            //生成一个计算任务，计算1+2+3+4+...+endCount
            ForkJoinStealTask task = new ForkJoinStealTask(1, endCount);
            //执行一个任务
            Future<Integer> result = forkjoinPool.submit(task);

            Thread.sleep(500);
//            forkjoinPool.shutdown();
            LOG.info("请求执行结束, end time={}", System.currentTimeMillis());
            return Result.success("Success, result= " + result.get());
        } catch (Exception e) {
            return Result.fail("计算失败" + e);
        }

    }


    @RequestMapping(value = "/tomcat/{sleepTime}", method = RequestMethod.GET)
    public Result tomcat(@PathVariable("sleepTime") int sleepTime) {

        Date startTime = new Date();
        try {
            LOG.info("开始执行请求, sleep start time={}", System.currentTimeMillis());
            Thread.sleep(sleepTime);
            LOG.info("请求执行结束, sleep end time={}", System.currentTimeMillis());
        } catch (InterruptedException e) {
            return Result.fail("Thread sleep error");
        }
        return Result.success("Success, startTime= " + startTime.toString());
    }

    @RequestMapping(value = "/thread/{corePoolSize}/{maxPoolSize}/{keepAliveTime}/{blockQueue}/{requestCount}",
            method = RequestMethod.GET)
    public Result thread(@PathVariable("corePoolSize") int corePoolSize,
                         @PathVariable("maxPoolSize") int maxPoolSize,
                         @PathVariable("keepAliveTime") long keepAliveTime,
                         @PathVariable("blockQueue") int blockQueue,
                         @PathVariable("requestCount") int requestCount) {
        try {
            ThreadTestUtil.test(corePoolSize, maxPoolSize, keepAliveTime, blockQueue, requestCount);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(JSON.toJSONString(e));
        }
        return Result.success("Success");
    }


}
