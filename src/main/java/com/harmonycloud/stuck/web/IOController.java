package com.harmonycloud.stuck.web;

import java.io.*;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import com.harmonycloud.stuck.bean.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "IO阻塞", tags = {"IO"})
@RestController
@RequestMapping("/io")
public class IOController {
    private static Logger LOG = LogManager.getLogger(IOController.class);
    
    private Scanner scanner = new Scanner(System.in);
    
    @ApiOperation(value = "等待输入")
    @RequestMapping(value = "/block", method = RequestMethod.GET)
    public Result block() throws Exception {
        LOG.info("Start Block");
        String line = scanner.nextLine();
        LOG.info("Finsh Block");    
        
        return Result.success("Got Input " + line);
    }

    @ApiOperation(value = "同步写入nfs延迟")
    @RequestMapping(value = "/writeNfs", method = RequestMethod.GET)
    public Result writeNfs(@RequestParam("path") String path) {
        LOG.info("Start write nfs");
        long startTimestamp = System.currentTimeMillis();
        File f = new File(path);
        try (OutputStream outputStream = new FileOutputStream(f)) {
            byte[] bWrite = {'H', 'e', 'l', 'l', 'o', ',', 'w', 'o', 'r', 'l', 'd'};
            for (byte b : bWrite) {
                outputStream.write(b); // writes the bytes
            }
        } catch (IOException e) {
            LOG.error(e);
        }

        try (InputStream is = new FileInputStream(f)) {
            int size = is.available();
            for (int i = 0; i < size; i++) {
                System.out.print((char) is.read() + "  ");
            }
        } catch (IOException e) {
            LOG.error(e);
        }
        long endTimestamp = System.currentTimeMillis();
        LOG.info("Finish write nfs");
        return Result.success("finish write nfs at" + path + " in" + (endTimestamp - startTimestamp) + "ms");
    }

    @ApiOperation(value = "接受POST大报文")
    @RequestMapping(value = "/bigBody", method = RequestMethod.POST)
    public Result receiveBigBody(@RequestParam("sleep") long sleep, @RequestBody String body) throws InterruptedException {
        Thread.sleep(sleep);
        LOG.info("Receive a request with body size {}, body is {}", body.length(), body);
        Thread.sleep(sleep);
        return Result.success("sleep=" + sleep + ", body size is " + body.length());
    }
}
