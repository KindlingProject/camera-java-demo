package com.harmonycloud.stuck.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.harmonycloud.stuck.bean.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "CPU Rest API", tags = {"CPU"})
@RestController
@RequestMapping("/cpu")
public class CpuController {
    private static final Logger LOG = LogManager.getLogger(CpuController.class);
    private volatile boolean running = true;
    
    @ApiOperation(value="CPU使用率单核100%")
    @RequestMapping(value = "/hot", method= RequestMethod.GET)
    public Result hotCpu(@RequestParam(value = "period", defaultValue = "1e8") long period) throws Exception {
        running = true;
        long i = 0;
        while (true) {
            if (!running) {
                LOG.info("Break the while loop when i = {}", i);
                break;
            }
            i++;
            if (i >= period) {
                LOG.info("{} times in while loop", i);
                i = 0;
            }
        }
        return Result.success("Hot Cpu");
    }
    
    @ApiOperation(value="冷却CPU使用率")
    @RequestMapping(value = "/cool", method= RequestMethod.GET)
    public Result coolCpu() {
        running = false;
        return Result.success("Cool Cpu");
    }
    @ApiOperation(value="多函数计算型任务")
    @RequestMapping(value = "/flameGraph", method= RequestMethod.GET)
    public String flameGraph(@RequestParam(value = "maxNumber", defaultValue = "1e8") long maxNumber) {
        long startTime = System.nanoTime();
        long a = 2, b = 1;
        while (a < maxNumber) {
            a++;
            b++;
            a = divide(a, b);
            a = add(a, b);
        }
        long endTime = System.nanoTime();
        return "flameGraph, totalTime = " + (endTime - startTime)/1000 + "us";
    }
    public long divide(long a, long b) {
        if (b == 0) {
            return a;
        }
        return a/b;
    }
    public long add(long a, long b) {
        return a+b;
    }
    @ApiOperation(value="forLoop N次")
    @ApiImplicitParam(name = "times", value = "次数(W)", required = true, dataType = "int", paramType = "path", defaultValue = "10")
    @RequestMapping(value = "/loop/{times}", method= RequestMethod.GET)
    public Result loop(@PathVariable("times") int times) throws Exception {
        if (times >= 20000) {
            return Result.success("TooLarge Times " + times);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(new char[1_000_000]);
        
        int totalTimes = times * 10000;
        for (int i = 0; i < totalTimes; i++) {
            sb.append(12345);
            sb.delete(0, 5);
        }
        return Result.success("ForLoop " + totalTimes);
    }

    @ApiOperation(value="计算斐波那契数列")
    @RequestMapping(value = "/fibonacci/{number}/{count}", method= RequestMethod.GET)
    public Result fibonacci(@PathVariable("number") long number,
                            @PathVariable("count") long count) {
        if (number >= 800000000) {
            return Result.success("Number larger than 800000000 is not allowed, current number = " + number);
        }
        long startTime = System.nanoTime();
        long ret = 0;
        for (int i=0; i<count; i++) {
             ret = fabIteration(number);
        }
        long endTime = System.nanoTime();
        return Result.success("Result = " + ret + ", totalTime = " + (endTime - startTime)/1000 + "us");
    }

    public long calculateFibonacci(long number) {
        if ((number == 0) || (number == 1))
            return number;
        else
            return calculateFibonacci(number - 1) + calculateFibonacci(number - 2);
    }

    public long fabIteration(long index) {
        if (index == 1 || index == 2) {
            return 1;
        } else {
            long f1 = 1L;
            long f2 = 1L;
            long f3 = 0;
            for(int i = 0; i < index-2; i++) {
                f3 = f1 + f2; //利用变量的原值推算出变量的一个新值
                f1 = f2;
                f2 = f3;
            }
            return f3;
        }
    }
}
