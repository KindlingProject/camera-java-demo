package com.harmonycloud.stuck.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harmonycloud.stuck.bean.Result;
import com.harmonycloud.stuck.util.OomHolder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "触发OOM", tags = {"Oom"})
@RestController
@RequestMapping("/oom")
public class OomController {
    /**
     * 用于模拟OOM
     */
    @ApiOperation(value="增加堆内存，直到触发OOM")
    @ApiImplicitParam(name = "size", value = "堆大小(MB)", required = true, dataType = "int", paramType = "path", defaultValue = "200")
    @RequestMapping(value = "/heap/{size}", method = RequestMethod.GET)
    public Result addHeap(@PathVariable("size") int size) {
        OomHolder.add(size);
        return Result.success("Add Heap " + size + "MB");
    }
}
