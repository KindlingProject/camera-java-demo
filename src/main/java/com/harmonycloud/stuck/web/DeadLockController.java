package com.harmonycloud.stuck.web;

import java.util.concurrent.ThreadFactory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harmonycloud.stuck.bean.Result;
import com.harmonycloud.stuck.util.DeadLockUtil;
import com.harmonycloud.stuck.util.DefaultThreadFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Dead Lock Rest API", tags = {"Dead Lock"})
@RestController()
@RequestMapping("/dead")
public class DeadLockController {
    private final DeadLockUtil deadLock = new DeadLockUtil();
    private final ThreadFactory threadFactory = new DefaultThreadFactory("Lock", false);
    private Thread lockThread = null;

    @ApiOperation(value="准备死锁场景")
    @RequestMapping(value = "/prepare", method= RequestMethod.GET)
    public Result prepare() throws Exception {
        if (lockThread == null) {
            lockThread = threadFactory.newThread(new Runnable() {
                public void run() {
                    deadLock.lock2();
                }
            });
            lockThread.start();
        }
        return Result.success("Prepare Lock");
    }
    
    @ApiOperation(value="模拟死锁场景")
    @RequestMapping(value = "/lock", method= RequestMethod.GET)
    public Result lock() throws Exception {
        deadLock.lock1();
        return Result.success("Finish DeadLock");
    }
}
