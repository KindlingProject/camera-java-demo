package com.harmonycloud.stuck.util.userCase;


import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class ServiceE {

    @Resource
    private UserService2 UserService2;

    @Trace
    public void doSomethingE() {

      log.info("Service E, TraceId = " + TraceContext.traceId());
        UserService2.createUserWrong1("something");

    }
}
