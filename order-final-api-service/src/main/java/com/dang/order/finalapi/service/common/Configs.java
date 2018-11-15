package com.dang.order.finalapi.service.common;

import com.dang.rocket.core.util.ConvertUtil;

public class Configs {
	public static final String API_TOKEN = ConvertUtil.toString(AppConfig.getProperty("api.token"),"");

	public static final String REVERSE_API_URL = ConvertUtil.toString(AppConfig.getProperty("reverse.api.url"));
	public static final int REVERSE_API_TIMEOUT = ConvertUtil.toInt(AppConfig.getProperty("reverse.api.timeout"));
	public static final String REVERSE_API_ENCODE = ConvertUtil.toString(AppConfig.getProperty("reverse.api.encode"));

	public static final String REDIS_KEY_TRANSLOG_FINAL = ConvertUtil.toString(AppConfig.getProperty("redis.key.orderTransLog.final"),"");
	public static final int REDIS_TIMEOUT_TRANSLOG_FINAL = ConvertUtil.toInt(AppConfig.getProperty("redis.timeout.orderTransLog.final"));

}
