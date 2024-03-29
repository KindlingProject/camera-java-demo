package com.harmonycloud.stuck.web;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.harmonycloud.stuck.bean.BigResult;
import com.harmonycloud.stuck.bean.Result;
import com.harmonycloud.stuck.util.userCase.MyThreadTask;
import com.harmonycloud.stuck.util.userCase.UserRightService;
import com.harmonycloud.stuck.util.userCase.UserWrongService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(value = "userCase", tags = {"userCase-new"})
@RestController
@RequestMapping("/UserCaseNew")
@Slf4j
public class UserCaseNewController {

    @Autowired
    private UserWrongService userService;

    @Autowired
    private UserRightService userService2;

    public static Pattern pattern = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");
    public static Matcher m;


    @Trace
    @ApiOperation(value = "多次正则校验")
    @RequestMapping(value = "/pattern/test", method = RequestMethod.GET)
    public Result patternTest(@RequestParam Integer count) {

        try {


            for (int k = 0; k < count; k++) {
                m = pattern.matcher("430923199803084155");
            }

        } catch (Exception e) {
            log.error("计算失败", e);

        }
        return Result.success("success");
    }

    @Trace
    @ApiOperation(value = "监测cpu-on事件能力：fastjson(jsonType=1)、Jackson(jsonType=2)、Gson(jsonType=3), net.sf.json(jsonType=4)三种序列化工具性能对比")
    @RequestMapping(value = "/queryBigResult", method = RequestMethod.POST)
    public Result queryBigResult(@RequestParam Integer count,
                                 @RequestParam Integer jsonType, HttpServletResponse response) throws IOException {

        try {

            long startTime = System.currentTimeMillis();
            List<BigResult> list = new ArrayList<>();
            log.info("开始构造大的序列化对象");
            for (int i = 0; i < count; i++) {
                list.add(this.handleResult());
            }
            if (1 == jsonType) {
                log.info("开始执行fastJson序列化");
                Object r = JSONObject.toJSON(list);
                log.info("序列化结束");
//                return Result.success(r);
            } else if (2 == jsonType) {
                log.info("开始执行Jackson序列化");
                ObjectMapper objectMapper = new ObjectMapper();
                String r2 = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
                log.info("序列化结束,耗时{}=", System.currentTimeMillis() - startTime);
                return Result.success(r2);
            } else if (3 == jsonType) {
                log.info("开始执行Gson序列化");
                Gson gson = new Gson();
                Object r3 = gson.toJson(list);
                log.info("序列化结束");
//                return Result.success(r3);
            } else {
                log.info("开始执行net.sf.json序列化");
                net.sf.json.JSONArray r4 = net.sf.json.JSONArray.fromObject(list);
                log.info("序列化结束");
//                return Result.success(r4);
            }
            return Result.success("success");
        } catch (Exception e) {
            log.error("序列化测试异常，jsonType = " + jsonType, e);
            return Result.fail("error");
        }
    }

    @Trace
    @ApiOperation(value = "并发场景：多线程，日志锁竞争")
    @RequestMapping(value = "/logLock", method = RequestMethod.GET)
    public Result threadPoolSingleTest() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            log.info("线程" + Thread.currentThread().getName() + "开始执行工作");
            for (int k = 0; k < 400; k++) {
                Pattern pattern = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");
                Matcher m = pattern.matcher("430923199803084155");
            }
            log.info("线程" + Thread.currentThread().getName() + "本次执行任务耗时 = " + (System.currentTimeMillis() - startTime));
        }

        return Result.success(TraceContext.traceId());
    }





    @Trace
    @ApiOperation(value = "文件IO场景：加不加buffer性能对比")
    @RequestMapping(value = "/fileIO", method = RequestMethod.GET)
    public Result fileIO(@RequestParam Boolean useBuffer,
                         @RequestParam String filePath) {
        try {
            if (useBuffer) {
                this.bufferOperationWith100Buffer(filePath);
            } else {
                this.perByteOperation(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.success("success");
    }


    @Trace
    @ApiOperation(value = "文件IO场景：并发写同一份文件")
    @RequestMapping(value = "/fileWrite", method = RequestMethod.GET)
    public Result fileWrite(@RequestParam String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write("Hello, world!");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.success("success");
    }

//    @Trace
//    @ApiOperation(value = "文件IO场景：多线程并行加buffer读取文件")
//    @RequestMapping(value = "/thread/fileIO", method = RequestMethod.GET)
//    public Result threadFileIO(@RequestParam Integer count,
//                               @RequestParam String filePath) {
//        try {
//
//            for (int i = 0; i < count; i++) {
//                Thread thread = new Thread(new MyThreadTask(filePath));
//                thread.start();
//            }
//            Thread.sleep(200);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return Result.success("success");
//    }

    @Trace
    @ApiOperation(value = "sql事务回滚-错误示范")
    @RequestMapping(value = "/sqlBackError", method = RequestMethod.GET)
    public Result sqlBackError(@RequestParam String name) {

        userService.createUserWrong(name);

        return Result.success("success");
    }

    @Trace
    @ApiOperation(value = "sql事务回滚-正确示范")
    @RequestMapping(value = "/sqlBackRight", method = RequestMethod.GET)
    public Result sqlBackRight(@RequestParam String name) {


        userService2.createUserWrong(name);


        return Result.success("success");
    }


    @Trace
    @ApiOperation(value = "DNS解析，调用域名接口")
    @RequestMapping(value = "/dnsTest", method = RequestMethod.GET)
    public Result dnsTest(@RequestParam("url") String url,
                          @RequestParam(value = "param", defaultValue = "") String param) throws IOException {
        log.info("Start to call another service:" + url + "?" + param);
        log.info("skywalking的trace id = " + TraceContext.traceId());
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            if (!param.isEmpty()) {
                url += "?" + param;
            }
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                log.info("The result is：{}", EntityUtils.toString(response.getEntity()));
            }
        } catch (ParseException e) {
            log.error("error", e);
        }
        log.info("End to call another service:" + url + "?" + param);
        return Result.success("");
    }


    /**
     * 不用缓冲取读取文件
     *
     * @param filePath
     */
    private void perByteOperation(String filePath) {

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
        ) {
            while ((fileInputStream.read()) != -1) {
            }
        } catch (Exception e) {
            log.error("不用缓冲区读取文件异常", e);
        }

    }


    /**
     * 用缓冲区读取文件
     *
     * @param filePath
     */
    private void bufferOperationWith100Buffer(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
        ) {
            byte[] buffer = new byte[100];
            while ((fileInputStream.read(buffer)) != -1) {
//
            }
        } catch (Exception e) {
            log.error("用缓冲区读取文件异常", e);
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


    public StopCondition RunQueueThreadCondition = new StopCondition(false);
    @Trace
    @ApiOperation(value = "同时唤醒若干线程，使run queue变长")
    @RequestMapping(value = "/startRunqlatency", method = RequestMethod.GET)
    public Result startRunQueueThread(@RequestParam("threadNum") int num) throws InterruptedException {
        RunQueueThreadCondition.setStopped(false);
        Thread startThreadLoop = new Thread(new LoopThread(RunQueueThreadCondition, num),"ThreadWakeLoop");
        startThreadLoop.start();
        return Result.success("Runqlatency threads started");
    }

    @Trace
    @ApiOperation(value = "停止唤醒若干线程，使run queue恢复正常")
    @RequestMapping(value = "/stopRunqlatency", method = RequestMethod.GET)
    public Result stopRunQueueThread() throws InterruptedException {
        if (RunQueueThreadCondition.stopped) {
            return Result.success("Runqlatency threads are not started");
        }
        RunQueueThreadCondition.setStopped(true);
        return Result.success("Runqlatency threads have been stopped");
    }


    class WaitThread implements Runnable {
        Semaphore semaphore;
        WaitThread(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            while (!RunQueueThreadCondition.stopped) {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                long a = 2, b = 1;
                while (a < 50000) {
                    a++;
                    b++;
                    a = divide(a, b);
                    a = add(a, b);
                }
            }
        }

        public long divide(long a, long b) {
            if (b == 0) {
                return a;
            }
            return a / b;
        }

        public long add(long a, long b) {
            return a + b;
        }
    }

    class LoopThread implements Runnable {
        StopCondition condition;
        Semaphore semaphore;
        int num;
        LoopThread(StopCondition condition, int num) {
            this.condition = condition;
            this.num = num;
            semaphore = new Semaphore(num);
        }
        @Override
        public void run() {
            try {
                startThreads();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (!condition.stopped) {
                releaseAllSemaphore();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void startThreads() throws InterruptedException {
            semaphore.acquire(num);
            for (int i=0; i < num; i++) {
                Thread thread = new Thread(new WaitThread(semaphore), "thread-" + i);
                thread.start();
            }
        }
        public void releaseAllSemaphore() {
            semaphore.release(num);
        }
    }
}

class StopCondition {
    boolean stopped;
    StopCondition(boolean stopped) {
        this.stopped = stopped;
    }
    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}