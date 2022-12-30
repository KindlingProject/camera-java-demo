package com.harmonycloud.stuck.util.userCase;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogTestThreadTask implements Runnable {

    private static Logger log = LogManager.getLogger(LogTestThreadTask.class);

    @SneakyThrows
    @Override
    public void run() {

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            log.info("线程" + Thread.currentThread().getName() + "开始执行工作");
            Pattern pattern = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");
            Matcher m = pattern.matcher("430923199803084155");
            Thread.sleep(5);
            log.info("线程" + Thread.currentThread().getName() + "本次执行任务耗时 = " + (System.currentTimeMillis() - startTime));
        }


    }
}
