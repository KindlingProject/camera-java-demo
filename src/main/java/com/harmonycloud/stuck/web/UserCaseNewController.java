package com.harmonycloud.stuck.web;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.harmonycloud.stuck.bean.BigResult;
import com.harmonycloud.stuck.bean.Result;
import com.harmonycloud.stuck.util.userCase.UserWrongService;
import com.harmonycloud.stuck.util.userCase.UserRightService;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    @Trace
    @ApiOperation(value = "监测cpu-on事件能力：fastjson(jsonType=1)、Jackson(jsonType=2)、Gson(jsonType=3), net.sf.json(jsonType=4)三种序列化工具性能对比")
    @RequestMapping(value = "/queryBigResult", method = RequestMethod.POST)
    public Result queryBigResult(@RequestParam Integer count,
                                 @RequestParam Integer jsonType) {
        log.info("skywalking的trace id = " + TraceContext.traceId());

        try {
            List<BigResult> list = new ArrayList<>();
            log.info("开始构造大的序列化对象");
            for (int i = 0; i < count; i++) {
                list.add(this.handleResult());
            }
            if (1 == jsonType) {
                log.info("开始执行fastJson序列化");
                Object r = JSONObject.toJSON(list);
                log.info("序列化结束");
            } else if (2 == jsonType) {
                log.info("开始执行Jackson序列化");
                ObjectMapper objectMapper = new ObjectMapper();
                String r2 = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
                log.info("序列化结束");
            } else if (3 == jsonType) {
                log.info("开始执行Gson序列化");
                Gson gson = new Gson();
                Object r3 = gson.toJson(list);
                log.info("序列化结束");
            } else {
                log.info("开始执行net.sf.json序列化");
                net.sf.json.JSONArray r4 = net.sf.json.JSONArray.fromObject(list);
                log.info("序列化结束");
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
        log.info("skywalking的trace id = " + TraceContext.traceId());
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
        log.info("skywalking的trace id = " + TraceContext.traceId());
        try {
            if (useBuffer) {
                log.info("开始用buffer进行文件读取");
                this.bufferOperationWith100Buffer(filePath);
            } else {
                log.info("开始不用buffer进行文件读取");
                this.perByteOperation(filePath);
            }
            log.info("读取文件结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.success("success");
    }

    @Trace
    @ApiOperation(value = "sql事务回滚-错误示范")
    @RequestMapping(value = "/sqlBackError", method = RequestMethod.GET)
    public Result sqlBackError(@RequestParam String name) {
        log.info("skywalking的trace id = " + TraceContext.traceId());

        userService.createUserWrong(name);

        return Result.success("success");
    }

    @Trace
    @ApiOperation(value = "sql事务回滚-正确示范")
    @RequestMapping(value = "/sqlBackRight", method = RequestMethod.GET)
    public Result sqlBackRight(@RequestParam String name) {

        log.info("skywalking的trace id = " + TraceContext.traceId());

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


}
