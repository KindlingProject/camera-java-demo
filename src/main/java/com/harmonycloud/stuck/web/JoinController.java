package com.harmonycloud.stuck.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harmonycloud.stuck.bean.Result;
import com.harmonycloud.stuck.util.JoinRunnable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Join新建的线程，但线程因异常退出", tags = {"join"})
@RestController
@RequestMapping("/join")
public class JoinController {
    private static Logger LOG = LogManager.getLogger(JoinController.class);

    @ApiOperation(value="join创建的线程，但线程因异常退出导致请求无法结束")
    @RequestMapping(value = "/exception", method = RequestMethod.GET)
    public Result exception() throws InterruptedException {
    	LOG.info("Start Join");
    	List<Thread> threadList = new ArrayList<Thread>();
    	for (int i = 0; i < 5; i++) {
    		threadList.add(new Thread(new JoinRunnable(), "Join Thread-" + i));
    	}
    	for (Thread thread : threadList) {
    		thread.start();
    	}
    	for (Thread thread : threadList) {
    		LOG.info("Join Thread [" + thread.getName() + "]");
    		thread.join();
    	}
    	LOG.info("End Join");
        return Result.success("Join Success");
    }
}
