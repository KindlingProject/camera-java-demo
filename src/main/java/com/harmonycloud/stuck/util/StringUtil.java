package com.harmonycloud.stuck.util;

public class StringUtil {
	public static String getData(int size) {
		byte[] data = new byte[size];
		int kb = size / 1024;
		int b = size % 1024;
		for (int i = 0; i < kb; i++) {
			System.arraycopy(Mb.KBYTES, 0, data, i * 1024, 1024);
		}
		if (b > 0) {
			System.arraycopy(Mb.KBYTES, 0, data, kb * 1024, b);
		}
		return new String(data);
	}
}
