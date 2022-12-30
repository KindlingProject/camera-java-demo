package com.harmonycloud.stuck.web;

import com.harmonycloud.stuck.bean.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

@Api(value = "请求长等待", tags = {"Wait"})
@RestController
public class WaitController {
    private static Logger LOG = LogManager.getLogger(WaitController.class);

    private final Object lockObject = new Object();
    private final ReentrantLock lock = new ReentrantLock();
    
    @ApiOperation(value="等待N秒再返回结果")
    @ApiImplicitParam(name = "second", value = "等待时间(s)", required = true, dataType = "int", paramType = "path", defaultValue = "10")
    @RequestMapping(value = "/sleep/{second}", method = RequestMethod.GET)
    public Result sleep(@PathVariable("second") int second) throws Exception {
        LOG.info("Start Sleep...");
        Thread.sleep(second * 1000L);
        LOG.info("Finsh Sleep");
        return Result.success("Sleep " + second + "s");
    }

    @ApiOperation(value="SynchonizedN秒再返回结果")
    @ApiImplicitParam(name = "second", value = "等待时间(s)", required = true, dataType = "int", paramType = "path", defaultValue = "10")
    @RequestMapping(value = "/sync-lock/{second}", method = RequestMethod.GET)
    public Result syncLock(@PathVariable("second") int second) throws Exception {
        LOG.info("Start SyncLock...");

        long startTime = System.currentTimeMillis();
        synchronized (lockObject) {
            LOG.info("Sleeping...");
            Thread.sleep(second * 1000L);
        }
        LOG.info("Finish SyncLock");
        return Result.success("Sleep " + (System.currentTimeMillis() - startTime) / 1000 + "s");
    }
    
    @ApiOperation(value="Reetrant Lock N秒再返回结果")
    @ApiImplicitParam(name = "second", value = "等待时间(s)", required = true, dataType = "int", paramType = "path", defaultValue = "10")
    @RequestMapping(value = "/reetrant-lock/{second}", method = RequestMethod.GET)
    public Result ReetanLock(@PathVariable("second") int second) throws Exception {
        LOG.info("Start ReetanLock..." + Thread.currentThread().getId());

        long startTime = System.currentTimeMillis();
        lock.lock();
        try {
            LOG.info("Sleeping...");
            Thread.sleep(second * 1000L);
        } finally {
            lock.unlock();
        }
        LOG.info("Finish ReetanLock");
        return Result.success("Sleep " + (System.currentTimeMillis() - startTime) / 1000 + "s");
    }
    @ApiOperation(value="请求另一个服务，返回结果")
    @RequestMapping(value = "/callOthers", method = RequestMethod.GET)
    public Result RequestOthers(@RequestParam("url") String url,
                                @RequestParam(value = "param", defaultValue = "") String param) throws IOException, InterruptedException {
        LOG.info("Start to call another service:" + url + "?" + param);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            if (!param.isEmpty()) {
                url += "?"+param;
            }
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            if(code == HttpStatus.SC_OK){
                LOG.info("The result is：{}", EntityUtils.toString(response.getEntity()));
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        LOG.info("End to call another service:" + url + "?" + param);
        Thread.sleep(550);
        return Result.success("");
    }
}
