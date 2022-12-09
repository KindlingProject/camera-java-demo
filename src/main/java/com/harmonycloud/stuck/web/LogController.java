package com.harmonycloud.stuck.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harmonycloud.stuck.bean.Result;
import com.harmonycloud.stuck.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "日志输出", tags = {"Log"})
@RestController
@RequestMapping("/log")
public class LogController {
    private static Logger LOG = LogManager.getLogger(LogController.class);
    private static volatile long sleepTime;
    
    @ApiOperation(value = "设置睡眠时间")
    @ApiImplicitParam(name = "millisecond", value = "睡眠时间(毫秒)", required = true, dataType = "long", paramType = "path", defaultValue = "0")
    @RequestMapping(value = "/initsleep/{millisecond}", method = RequestMethod.GET)
    public Result initSleep(@PathVariable("millisecond") long milliSecond) throws Exception {
    	sleepTime = milliSecond;
        return Result.success("Sleep " + milliSecond + "ms");
    }
    
    @ApiOperation(value = "错误日志")
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public Result error() throws Exception {
    	LOG.error("Error", new Throwable());
    	if (sleepTime > 0) {
    		Thread.sleep(sleepTime);
    	}
        return Result.success("Error Log");
    }
    
    @ApiOperation(value = "单条日志")
    @ApiImplicitParam(name = "size", value = "大小(B)", required = true, dataType = "int", paramType = "path", defaultValue = "10")
    @RequestMapping(value = "/single/{size}", method = RequestMethod.GET)
    public Result single(@PathVariable("size") int size) throws Exception {
        LOG.info(StringUtil.getData(size));
        if (sleepTime > 0) {
    		Thread.sleep(sleepTime);
    	}
        return Result.success("Log Size: " + size);
    }
    
    @ApiOperation(value = "多条日志")
    @ApiImplicitParams({
		@ApiImplicitParam(name = "count", value = "条数", required = true, dataType = "int", paramType = "path", defaultValue = "1"),
	    @ApiImplicitParam(name = "size", value = "大小(B)", required = true, dataType = "int", paramType = "path", defaultValue = "10")
    })
    @RequestMapping(value = "/multi/{count}/{size}", method = RequestMethod.GET)
    public Result multi(@PathVariable("count") int count, @PathVariable("size") int size) throws Exception {
    	String log = StringUtil.getData(size);
    	for (int i = 0; i < count; i++) {
    		LOG.info(log);
    	}
    	if (sleepTime > 0) {
    		Thread.sleep(sleepTime);
    	}
        return Result.success("Log Count: " + count + ", Size: " + size);
    }
}
