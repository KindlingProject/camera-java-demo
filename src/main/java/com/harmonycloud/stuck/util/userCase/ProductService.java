package com.harmonycloud.stuck.util.userCase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class ProductService {

    @Resource
    private DaoService daoService;

    public void doSomething(boolean throwFlag) {
        try {
            log.info("productService开始执行");
            daoService.queryData(throwFlag);
        } catch (Exception e) {
            throw e;
        }

    }
}
