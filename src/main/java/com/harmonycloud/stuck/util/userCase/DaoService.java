package com.harmonycloud.stuck.util.userCase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DaoService {

    public void queryData(boolean throwFlag) {
        try {
            if (throwFlag) {
                log.info("抛出异常");
                throw new RuntimeException("我是dao层抛出的异常");
            } else {
                log.info("正常执行");
            }
        } catch (Exception e) {
//            log.error("dao层出现异常", e);
            throw e;
        }
    }
}
