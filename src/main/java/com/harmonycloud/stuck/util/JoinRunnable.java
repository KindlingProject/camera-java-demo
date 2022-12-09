package com.harmonycloud.stuck.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JoinRunnable implements Runnable {
    private static Logger LOG = LogManager.getLogger(JoinRunnable.class);

	public void run() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
		}
		new UnCaughtException().test();
	}
}

class UnCaughtException {
	static int value;
	
	static {
		value = 100 / 0;
	}
	
	public void test() {
		System.out.println(value);
	}
}