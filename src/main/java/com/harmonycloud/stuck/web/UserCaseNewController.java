package com.harmonycloud.stuck.web;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.harmonycloud.stuck.bean.BigResult;
import com.harmonycloud.stuck.bean.Result;
import com.harmonycloud.stuck.util.ForkJoinTask;
import com.harmonycloud.stuck.util.userCase.MyThreadTask;
import com.harmonycloud.stuck.util.userCase.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

@Api(value = "userCase", tags = {"userCase-new"})
@RestController
@RequestMapping("/UserCaseNew")
@Slf4j
public class UserCaseNewController {


    @Resource
    private OrderService orderService;

    @ApiOperation(value = "文件读写加不加buffer")
    @RequestMapping(value = "/buffer", method = RequestMethod.GET)
    public Result buffer(@RequestParam String filePath) {

        try {


            long startTime = System.currentTimeMillis();
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {// 如果有数据就一直读写,否则就退出循环体,关闭流资源。
                new String(bytes, 0, len, "utf-8");
            }
            inputStream.close();
            log.info("加buffer，文件读取时间=" + (System.currentTimeMillis() - startTime));

            long startTime2 = System.currentTimeMillis();
            FileInputStream inputStream2 = new FileInputStream(new File(filePath));
            byte[] bytes2 = new byte[1024];
            int len2 = 0;
            while ((len2 = inputStream2.read(bytes2)) != -1) {
                new String(bytes2, 0, len2, "utf-8");
            }
            inputStream2.close();
            log.info("未加buffer，文件读取时间=" + (System.currentTimeMillis() - startTime2));



        } catch (Exception e) {
            log.error("文件异常", e);
            return Result.fail("error");
        }
        return Result.success("success");
    }


    @ApiOperation(value = "用不用线程池")
    @RequestMapping(value = "/threadPoolTest", method = RequestMethod.GET)
    public Result threadPoolTest(@RequestParam Boolean usePool,
                                 @RequestParam Integer taskCount,
                                 @RequestParam Integer poolSize) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        if (usePool) {
            ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
            for (int i = 0; i < taskCount; i++) {
                executorService.execute(new MyThreadTask(startTime, 5));
            }
            executorService.shutdown();
        } else {
            for (int k = 0; k < taskCount; k++) {
                new Thread(new MyThreadTask(startTime, 5)).start();
            }
        }
        Thread.sleep(510);
        return Result.success("success");
    }

    @ApiOperation(value = "线程start和run方法对比")
    @RequestMapping(value = "/threadInvokeTest", method = RequestMethod.GET)
    public Result threadInvokeTest() throws InterruptedException {

        log.info("Thread.start启动");
        new Thread(new MyThreadTask(System.currentTimeMillis(), 50)).start();

        log.info("Thread.run启动");
        new Thread(new MyThreadTask(System.currentTimeMillis(), 50)).run();

        log.info("请求执行结束");
        Thread.sleep(510);
        return Result.success("success");
    }


    @ApiOperation(value = "异常性能测试-测试结果不符-废弃")
    @RequestMapping(value = "/exceptionTest", method = RequestMethod.GET)
    public Result exceptionTest(@RequestParam Boolean throwFlag) {

        long startTime = System.currentTimeMillis();
        try {
            orderService.doSomeThing(throwFlag);
        } catch (Exception e) {

        }
        log.info("请求耗时" + (System.currentTimeMillis() - startTime));
        return Result.success("success");
    }


    @ApiOperation(value = "list循环性能比对for & Iterator")
    @RequestMapping(value = "/ListLoop/{count}", method = RequestMethod.GET)
    public Result ListLoop(@PathVariable("count") Integer count) throws InterruptedException {

        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add("我是第几个：" + i);
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            String rr = list.get(i);
        }
        log.info("for循环消耗时长 = " + (System.currentTimeMillis() - startTime));

        long startTime2 = System.currentTimeMillis();
        Iterator<String> iter = list.iterator();
        while (iter.hasNext()) {
            String s = iter.next();
        }
        log.info("Iterator循环消耗时长 = " + (System.currentTimeMillis() - startTime2));

        Thread.sleep(510);
        return Result.success("success");

    }

    @ApiOperation(value = "多线程下，finnaly的执行")
    @RequestMapping(value = "/forkJoin/{endCount}", method = RequestMethod.GET)
    public Result forkJoin(@PathVariable("endCount") int endCount) {

        try {
            log.info("开始执行请求, start time={}", System.currentTimeMillis());
            ForkJoinPool forkjoinPool = new ForkJoinPool();
            ForkJoinTask task = new ForkJoinTask(1, endCount);
            forkjoinPool.submit(task);
            log.info("请求执行结束, end time={}", System.currentTimeMillis());

            forkjoinPool.shutdown();
            return Result.success("Success, result= ");
        } catch (Exception e) {
            return Result.fail("计算失败" + e);
        } finally {
            log.info("我是finally的内容");
        }

    }

    @ApiOperation(value = "Jackson、fastjson、Gson三种序列化工具性能对比")
    @RequestMapping(value = "/queryBigResult/{count}", method = RequestMethod.POST)
    public Result queryBigResult(@PathVariable("count") Integer count) {

        try {
            long startTime = System.currentTimeMillis();

            List<BigResult> list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                list.add(this.handleResult());
            }
            long startTime2 = System.currentTimeMillis();
            Object r = JSONObject.toJSON(list);
            log.info("fastJson列化时间, 耗时={}", System.currentTimeMillis() - startTime2);

            long startTime3 = System.currentTimeMillis();
            ObjectMapper objectMapper = new ObjectMapper();
            String r2 = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
            log.info("Jackson序列化时间, 耗时={}", System.currentTimeMillis() - startTime3);


            long startTime4 = System.currentTimeMillis();
            Gson gson = new Gson();
            Object r3 = gson.toJson(list);
            log.info("Gson序列化时间, 耗时={}", System.currentTimeMillis() - startTime4);

            long startTime5 = System.currentTimeMillis();
            net.sf.json.JSONArray r4 = net.sf.json.JSONArray.fromObject(list);
            log.info("net.sf.json序列化时间, 耗时={}", System.currentTimeMillis() - startTime5);

            log.info("请求执行结束, 耗时={}", System.currentTimeMillis() - startTime);
            Thread.sleep(500);
            return Result.success(list);
        } catch (Exception e) {
            return Result.fail("error" + e);
        }

    }


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
