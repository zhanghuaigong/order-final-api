package com.dang.order.finalapi.service.common;

import com.dang.order.common.api.ApiResponse;
import com.dang.order.finalapi.dao.criteria.OrderCriteria;
import com.dang.order.finalapi.dao.criteria.OrderSplitPackageCriteria;
import com.dang.order.finalapi.dao.criteria.OrderTranslogCriteria;
import com.dang.order.finalapi.dto.enums.RespStatusCode;

import java.util.ArrayList;
import java.util.List;

public class BeanConvertUtil {
	public static OrderTranslogCriteria buildOrderTranslogCriteria(Long orderId,boolean isSuccess){
		OrderTranslogCriteria orderTranslogCriteria = new OrderTranslogCriteria();
		orderTranslogCriteria.setOrderId(orderId);
		orderTranslogCriteria.setOperatorId(Constants.TRANSLOG_OPERATOR_ID);
		orderTranslogCriteria.setOperatorType(Constants.TRANSLOG_OPERATOR_TYPE);
		if(isSuccess){
			orderTranslogCriteria.setTransDesc(Constants.TRANSLOG_TRANS_DESC_SUCESS);
			orderTranslogCriteria.setOrderStatus(Constants.TRANSLOG_ORDER_STATUS_SUCESS);
		}else{
			orderTranslogCriteria.setTransDesc(Constants.TRANSLOG_TRANS_DESC_FAIL);
			orderTranslogCriteria.setOrderStatus(Constants.TRANSLOG_ORDER_STATUS_FAIL);
		}
		orderTranslogCriteria.setTransDescId(Constants.TRANSLOG_TRANS_DESC_ID);

		return orderTranslogCriteria;
	}

	public static OrderCriteria buildOrderCriteria(Long orderId,boolean isSuccess){
		OrderCriteria orderCriteria = new OrderCriteria();
		orderCriteria.setOrderId(orderId);
		List<Integer> oldStatusList = new ArrayList<>();
		if(isSuccess){
			orderCriteria.setOrderStatus(1000);
			oldStatusList.add(400);
			orderCriteria.setOldStatusList(oldStatusList);
		}else{
			orderCriteria.setOrderStatus(1100);
			oldStatusList.add(400);
			oldStatusList.add(1000);
			orderCriteria.setOldStatusList(oldStatusList);
		}
		return orderCriteria;
	}

	public static OrderSplitPackageCriteria buildOrderSplitPackageCriteria(Long orderId,boolean isSuccess){
		OrderSplitPackageCriteria orderSplitPackageCriteria = new OrderSplitPackageCriteria();
		orderSplitPackageCriteria.setOrderId(orderId);
		List<Integer> oldStatusList = new ArrayList<>();
		if(isSuccess){
			orderSplitPackageCriteria.setPackageStatus(1000);
			oldStatusList.add(400);
			orderSplitPackageCriteria.setOldStatusList(oldStatusList);
		}else{
			orderSplitPackageCriteria.setPackageStatus(1100);
			oldStatusList.add(400);
			oldStatusList.add(1000);
			orderSplitPackageCriteria.setOldStatusList(oldStatusList);
		}
		return orderSplitPackageCriteria;
	}

	public static ApiResponse convertRespResult(RespStatusCode respStatusCode, String subMsg){
		return new ApiResponse(respStatusCode.getCode(),respStatusCode.getMsg(),
				respStatusCode.getSubCode(),respStatusCode.formatSubMsg(subMsg),null);
	}

}
