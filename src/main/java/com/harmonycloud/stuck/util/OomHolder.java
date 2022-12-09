package com.harmonycloud.stuck.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OomHolder {
    private static final int MB = 1 << 20;
    public static List<Mb> datas = new CopyOnWriteArrayList<Mb>();
    
    public static void add(int size) {
        long beforeFreeSize = Runtime.getRuntime().freeMemory();
        datas.add(new Mb(size));
        long afterFreeSize = Runtime.getRuntime().freeMemory();
        System.out.println("Current Heap FreeSize: " + afterFreeSize / MB + ", allocated: " + (beforeFreeSize - afterFreeSize) / MB);
    }
}

class Mb {
    static final byte[] KBYTES = ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!"
        + "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!").getBytes();

    private final byte[] datas;
    
    public Mb(int size) {
        datas = new byte[size * 1024 * 1024];
        for (int i = 0; i < size * 1024; i++) {
            System.arraycopy(KBYTES, 0, datas, i * 1024, 1024);
        }
    }
}
