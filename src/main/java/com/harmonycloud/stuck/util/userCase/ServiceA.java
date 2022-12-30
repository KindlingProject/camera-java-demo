package com.harmonycloud.stuck.util.userCase;


import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class ServiceA {

    @Resource
    private ServiceB serviceB;

    @Trace
    public void doSomethingA() {

      log.info("Service A, TraceId = " + TraceContext.traceId());
      serviceB.doSomethingB();

    }
}
