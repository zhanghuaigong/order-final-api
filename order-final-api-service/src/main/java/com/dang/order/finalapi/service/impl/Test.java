package com.dang.order.finalapi.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Test {
	public static void main(String[] args) {
		try {
			System.out.println(URLEncoder.encode("1,0,-1","UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
