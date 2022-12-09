package com.harmonycloud.stuck.web;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.harmonycloud.stuck.bean.BigResult;
import com.harmonycloud.stuck.bean.Result;
import com.harmonycloud.stuck.util.userCase.MyThreadTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Api(value = "userCase", tags = {"userCase-new"})
@RestController
@RequestMapping("/UserCaseNew")
@Slf4j
public class UserCaseNewController {


    @ApiOperation(value = "监测cpu-on事件能力：Jackson(jsonType=1)、fastjson(jsonType=2)、Gson(jsonType=3), net.sf.json(jsonType=4)三种序列化工具性能对比")
    @RequestMapping(value = "/queryBigResult", method = RequestMethod.POST)
    public Result queryBigResult(@RequestParam Integer count,
                                 @RequestParam Integer jsonType) {

        try {
            List<BigResult> list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                list.add(this.handleResult());
            }
            if (1 == jsonType) {
                log.info("开始执行fastJson序列化");
                Object r = JSONObject.toJSON(list);
                log.info("序列化结束");
            } else if (2 == jsonType) {
                log.info("开始执行fastJson序列化");
                ObjectMapper objectMapper = new ObjectMapper();
                String r2 = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
                log.info("序列化结束");
            } else if (3 == jsonType) {
                log.info("开始执行fastJson序列化");
                Gson gson = new Gson();
                Object r3 = gson.toJson(list);
                log.info("序列化结束");
            } else {
                log.info("开始执行fastJson序列化");
                net.sf.json.JSONArray r4 = net.sf.json.JSONArray.fromObject(list);
                log.info("序列化结束");
            }
            Thread.sleep(500);
            return Result.success(list);
        } catch (Exception e) {
            log.error("序列化测试异常，jsonType = " + jsonType, e);
            return Result.fail("error");
        }
    }

    @ApiOperation(value = "文件IO场景: 读文件加不加buffer")
    @RequestMapping(value = "/buffer", method = RequestMethod.GET)
    public Result buffer(@RequestParam String filePath,
                         @RequestParam Boolean useBuffer) {
        try {
            if (useBuffer) {
                log.info("加buffer读取文件");
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(bytes)) != -1) {// 如果有数据就一直读写,否则就退出循环体,关闭流资源。
                    new String(bytes, 0, len, "utf-8");
                }
                inputStream.close();
            } else {
                log.info("不加buffer读取文件");
                FileInputStream inputStream2 = new FileInputStream(new File(filePath));
                byte[] bytes2 = new byte[1024];
                int len2 = 0;
                while ((len2 = inputStream2.read(bytes2)) != -1) {
                    new String(bytes2, 0, len2, "utf-8");
                }
                inputStream2.close();
            }
            log.info("读取文件结束");

        } catch (Exception e) {
            log.error("文件异常", e);
            return Result.fail("error");
        }
        return Result.success("success");
    }


    @ApiOperation(value = "线程并发场景：不同并发工作量(taskCount)下，是否用线程池两种场景性能比对")
    @RequestMapping(value = "/threadPoolTest", method = RequestMethod.GET)
    public Result threadPoolTest(@RequestParam Integer taskCount,
                                 @RequestParam Integer poolSize) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        log.info("启动线程池执行任务");
        for (int i = 0; i < taskCount; i++) {
            executorService.execute(new MyThreadTask(startTime, 5));
        }
        executorService.shutdown();

        log.info("不用线程池，创建单个线程执行任务");
        for (int k = 0; k < taskCount; k++) {
            new Thread(new MyThreadTask(startTime, 5)).start();
        }
        Thread.sleep(500);
        return Result.success("success");
    }

//    @ApiOperation(value = "线程start和run方法对比")
//    @RequestMapping(value = "/threadInvokeTest", method = RequestMethod.GET)
//    public Result threadInvokeTest() throws InterruptedException {
//
//        log.info("Thread.start启动");
//        new Thread(new MyThreadTask(System.currentTimeMillis(), 50)).start();
//
//        log.info("Thread.run启动");
//        new Thread(new MyThreadTask(System.currentTimeMillis(), 50)).run();
//
//        log.info("请求执行结束");
//        Thread.sleep(510);
//        return Result.success("success");
//    }

//    @ApiOperation(value = "多线程下，finnaly的执行")
//    @RequestMapping(value = "/forkJoin/{endCount}", method = RequestMethod.GET)
//    public Result forkJoin(@PathVariable("endCount") int endCount) {
//
//        try {
//            log.info("开始执行请求, start time={}", System.currentTimeMillis());
//            ForkJoinPool forkjoinPool = new ForkJoinPool();
//            ForkJoinTask task = new ForkJoinTask(1, endCount);
//            forkjoinPool.submit(task);
//            log.info("请求执行结束, end time={}", System.currentTimeMillis());
//
//            forkjoinPool.shutdown();
//            return Result.success("Success, result= ");
//        } catch (Exception e) {
//            return Result.fail("计算失败" + e);
//        } finally {
//            log.info("我是finally的内容");
//        }
//
//    }


    private BigResult handleResult() {

        BigResult result = new BigResult();

        List<HashMap<String, BigResult.InterResult>> list = new ArrayList<>();

        HashMap<String, BigResult.InterResult> map1 = new HashMap<>();
        map1.put("hhh", this.create());
        list.add(map1);
        result.setA("I am A");
        result.setB(4);
        result.setList(list);

        HashMap<String, List<BigResult.InterResult>> map = new HashMap<>();
        List<BigResult.InterResult> list1 = new ArrayList<>();
        list1.add(this.create());
        map.put("hhhhh", list1);
        result.setMap(map);
        return result;
    }


    private BigResult.InterResult create() {
        BigResult.InterResult interResult = new BigResult.InterResult();
        interResult.setC("I am C in interResult");
        interResult.setD(1);
        List<HashMap<String, Object>> interList = new ArrayList<>();
        for (int k = 0; k < 5; k++) {
            HashMap<String, Object> interMap1 = new HashMap<>();
            for (int i = 0; i < 10; i++) {
                interMap1.put("string" + i, "thing" + i);
                interMap1.put("some" + i, i);
            }
            interList.add(interMap1);
        }
        interResult.setList(interList);

        HashMap<String, List<String>> map = new HashMap<>();
        for (int y = 0; y < 10; y++) {
            List<String> list = new ArrayList<>();
            list.add("list" + y);
            map.put("map" + y, list);
        }
        interResult.setMap(map);
        return interResult;
    }


}
