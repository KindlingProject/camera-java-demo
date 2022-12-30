package com.harmonycloud.stuck.util.userCase;


import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class ServiceD {

    @Resource
    private ServiceE serviceE;

    @Trace
    public void doSomethingD() {

      log.info("Service D, TraceId = " + TraceContext.traceId());
      serviceE.doSomethingE();

    }
}
