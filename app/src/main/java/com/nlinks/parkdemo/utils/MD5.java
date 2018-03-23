package com.nlinks.parkdemo.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public static String encode(String string) {

		byte[] hash;
		StringBuilder hex = null;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
			hex = new StringBuilder(hash.length * 2);
			for (byte b : hash) {
				if ((b & 0xFF) < 0x10) {
					hex.append("0");
				}
				hex.append(Integer.toHexString(b & 0xFF));
			}
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}
		return hex.toString().toUpperCase();
	}
}
