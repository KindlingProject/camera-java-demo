//package com.harmonycloud.stuck.util.userCase;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//
//public class FileIOUtil {
//
//    private static Logger log = LogManager.getLogger(FileIOUtil.class);
//
//
//    public static void fileIoTest(String filePath) throws Exception {
//
//        long startTime = System.currentTimeMillis();
//        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
//        byte[] bytes = new byte[1024];
//        int len = 0;
//        while ((len = inputStream.read(bytes)) != -1) {// 如果有数据就一直读写,否则就退出循环体,关闭流资源。
//            new String(bytes, 0, len, "utf-8");
//        }
//        inputStream.close();
//        log.info("加buffer，文件读取时间=" + (System.currentTimeMillis() - startTime));
//
//
//        long startTime2 = System.currentTimeMillis();
//        FileInputStream inputStream2 = new FileInputStream(new File(filePath));
//        byte[] bytes2 = new byte[1024];
//        int len2 = 0;
//        while ((len2 = inputStream2.read(bytes2)) != -1) {
//            new String(bytes2, 0, len2, "utf-8");
//        }
//        inputStream2.close();
//        log.info("未加buffer，文件读取时间=" + (System.currentTimeMillis() - startTime2));
//
//
//
//
//
//    }
//}
