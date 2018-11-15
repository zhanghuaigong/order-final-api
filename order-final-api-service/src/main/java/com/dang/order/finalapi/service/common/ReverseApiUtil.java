package com.dang.order.finalapi.service.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dang.order.util.http.HttpGetUtil;
import com.dang.rocket.core.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReverseApiUtil {
	private static final Logger logger = LoggerFactory.getLogger(ReverseApiUtil.class);

	public static Integer getSumReturnNum(Long orderId){
		String url = String.format(Configs.REVERSE_API_URL,orderId);
		String encode = Configs.REVERSE_API_ENCODE;
		int timeout = Configs.REVERSE_API_TIMEOUT;
		try{
			logger.info("调用退换货API请求,url:{},orderId:{}", url, orderId);
			String response = HttpGetUtil.doGet(url,null,null,timeout,timeout,encode);
			logger.info("调用退换货API响应,url:{},orderId:{},response:{}",url,orderId,response);
			if (response == null || "".equals(response.trim())) {
				logger.error("调用退换货API返回为空,orderId:{},url:{},response:{}", orderId, url, response);
				return null;
			}

			JSONObject rootJsonObject = JSON.parseObject(response);
			Integer status = rootJsonObject.getInteger("status");
			if(status == null || status != 0){
				logger.error("调用退换货API错误,状态码错误,orderId:{},status:{}", orderId, status);
				return null;
			}

			JSONArray dataArray = rootJsonObject.getJSONArray("data");
			if(dataArray == null || dataArray.size() <= 0){
				return 0;
			}

			int sumReturnNum = 0;
			for(int i = 0;i < dataArray.size();i++){
				JSONObject dataObject = dataArray.getJSONObject(i);
				int returnNum = ConvertUtil.toInt(dataObject.getInteger("returnNum"));
				sumReturnNum += returnNum;
			}

			logger.info("汇总退换货api的returnNum结果,orderId:{},sumReturnNum:{}",orderId,sumReturnNum);
			return sumReturnNum;
		}catch (Exception e){
			logger.error("调用退换货API异常,orderId:" + orderId,e);
			return null;
		}
	}
}
