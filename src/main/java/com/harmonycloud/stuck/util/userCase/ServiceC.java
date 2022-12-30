package com.harmonycloud.stuck.util.userCase;


import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class ServiceC {

    @Resource
    private ServiceD serviceD;

    @Trace
    public void doSomethingC() {

      log.info("Service C, TraceId = " + TraceContext.traceId());
        serviceD.doSomethingD();

    }
}
