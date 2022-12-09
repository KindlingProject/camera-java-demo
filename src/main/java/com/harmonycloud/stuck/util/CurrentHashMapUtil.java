package com.harmonycloud.stuck.util;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CurrentHashMapUtil {

    private static Logger LOG = LogManager.getLogger(CurrentHashMapUtil.class);

    private Map<String, String> userMap = new ConcurrentHashMap<>();

    public String registerUser(String userName) {
        String result = "";
        if (userMap.containsKey(userName)) {
            result = "注册失败，用户已存在";
        } else {
            userMap.put(userName, userName);
            result = "注册成功";
        }

        LOG.info("现有注册用户{}", JSON.toJSON(userMap));
        return result;
    }

}
