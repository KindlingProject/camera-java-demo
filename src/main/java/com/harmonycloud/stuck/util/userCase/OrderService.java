package com.harmonycloud.stuck.util.userCase;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderService {

    @Resource
    private ProductService productService;

    public void doSomeThing(boolean throwFlag) {
        try {
            productService.doSomething(throwFlag);
        } catch (Exception e) {
            throw e;
        }

    }
}
