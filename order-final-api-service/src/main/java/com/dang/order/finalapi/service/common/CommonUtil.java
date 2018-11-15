package com.dang.order.finalapi.service.common;

import com.dang.order.common.api.ApiRequest;
import com.dang.order.common.api.ApiResponse;
import com.dang.order.finalapi.api.param.OrderParam;
import com.dang.order.finalapi.dto.db.OrderItem;
import com.dang.order.finalapi.dto.enums.RespStatusCode;
import com.dang.rocket.core.util.ConvertUtil;

import java.util.ArrayList;
import java.util.List;

public class CommonUtil {
	public static List<OrderItem> filterOrderItems(List<OrderItem> orderItems){
		List<OrderItem> remainOrderItemList = new ArrayList<>();
		for(OrderItem orderItem : orderItems){
			if(!CommonUtil.isVirtualProduct(orderItem)
					&& !CommonUtil.isEbookProduct(orderItem)){
				remainOrderItemList.add(orderItem);
			}
		}
		return remainOrderItemList;
	}

	/**
	 * 判断是否虚拟母品
	 */
	public static boolean isVirtualProduct(OrderItem orderItem){
		int productType = ConvertUtil.toInt(orderItem.getProductType());
		int relationType = ConvertUtil.toInt(orderItem.getRelationType());
		if(productType == 9 && relationType > 0 && (relationType % 3 == 0)){
			return true;
		}
		return false;
	}

	/**
	 * 判断是否电子书商品
	 */
	public static boolean isEbookProduct(OrderItem orderItem){
		int medium = ConvertUtil.toInt(orderItem.getMedium());
		if(medium == 22 || medium == 96){
			return true;
		}
		return false;
	}

	public static ApiResponse validRequest(ApiRequest<OrderParam> request){
		RespStatusCode respCode = RespStatusCode.REQUEST_PARAM_INVALID;
		if(request == null){
			return BeanConvertUtil.convertRespResult(respCode,"request param is null");
		}

		if(!validToken(request.getTokenId(), Configs.API_TOKEN)){
			return BeanConvertUtil.convertRespResult(respCode,"tokenId is error");
		}

		OrderParam requestBody = request.getRequestBody();
		if(requestBody == null){
			return BeanConvertUtil.convertRespResult(respCode,"requestBody is null");
		}

		if(ConvertUtil.toLong(requestBody.getOrderId()) <= 0){
			return BeanConvertUtil.convertRespResult(respCode,"orderId is error");
		}

		return null;
	}

	public static boolean validToken(String clientToken,String systemToken){
		if(systemToken == null || "".equals(systemToken.trim())){
			return true;
		}

		if(clientToken == null || "".equals(clientToken.trim())){
			return false;
		}

		String[] tokenArray = systemToken.split(",");
		for(int i = 0; i < tokenArray.length;i++){
			if(clientToken.trim().equals(tokenArray[i].trim())){
				return true;
			}
		}
		return false;
	}
}
