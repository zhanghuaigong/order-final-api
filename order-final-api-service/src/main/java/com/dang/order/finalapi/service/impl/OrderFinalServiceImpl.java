package com.dang.order.finalapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.dang.order.common.api.ApiRequest;
import com.dang.order.common.api.ApiResponse;
import com.dang.order.finalapi.api.OrderFinalService;
import com.dang.order.finalapi.api.param.OrderParam;
import com.dang.order.finalapi.dao.criteria.OrderCriteria;
import com.dang.order.finalapi.dao.criteria.OrderDataCriteria;
import com.dang.order.finalapi.dao.criteria.OrderSplitPackageCriteria;
import com.dang.order.finalapi.dao.criteria.OrderTranslogCriteria;
import com.dang.order.finalapi.dto.db.Order;
import com.dang.order.finalapi.dto.db.OrderItem;
import com.dang.order.finalapi.dto.enums.RespStatusCode;
import com.dang.order.finalapi.service.OrderDataService;
import com.dang.order.finalapi.service.OrderEventService;
import com.dang.order.finalapi.service.common.BeanConvertUtil;
import com.dang.order.finalapi.service.common.CommonUtil;
import com.dang.order.finalapi.service.common.ReverseApiUtil;
import com.dang.rocket.core.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Service("orderFinalService")
@Path("orderfinal")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class OrderFinalServiceImpl implements OrderFinalService {
	private static final Logger logger = LoggerFactory.getLogger(OrderFinalServiceImpl.class);

	@Resource
	private OrderDataService orderDataService;
	@Resource
	private OrderEventService orderEventService;

	@Override
	@Path("process")
	@POST
	public ApiResponse processFinalOrder(ApiRequest<OrderParam> request) {
		try{
			logger.info("请求入参:{}", JSON.toJSONString(request));
			ApiResponse validReqResponse = CommonUtil.validRequest(request);
			if(validReqResponse != null){
				logger.error("参数校验不通过,orderId:{},subMsg:{}",
						request.getRequestBody().getOrderId(),validReqResponse.getSubMsg());
				return validReqResponse;
			}

			ApiResponse response = process(request.getRequestBody());
			logger.info("处理完成,返回结果,orderId:{},response:{}",
					request.getRequestBody().getOrderId(),JSON.toJSONString(response));
			return response;
		}catch (Exception e){
			logger.error("处理异常,orderId:" + request.getRequestBody().getOrderId(),e);
			return BeanConvertUtil.convertRespResult(RespStatusCode.UN_KNOWN_EXCEPTION,"");
		}
	}

	/**
	 * 业务处理
	 */
	private ApiResponse process(OrderParam orderParam){
		Long orderId = orderParam.getOrderId();
		Order dbOrder = orderDataService.getOrderByOrderId(orderId);
		if(dbOrder == null){
			logger.error("查询订单信息为空,orderId:{}",orderId);
			return BeanConvertUtil.convertRespResult(RespStatusCode.RECORD_NOT_EXIST,"order query null");
		}

		int orderStatus = ConvertUtil.toInt(dbOrder.getOrderStatus());
		if(orderStatus != 400 && orderStatus != 1000){
			logger.info("订单状态不允许变更为完结状态,按成功处理,orderId:{},orderStatus:{}",orderId,orderStatus);
			return BeanConvertUtil.convertRespResult(RespStatusCode.SUCCESS,orderStatus + "");
		}

		Integer sumReturnNum = ReverseApiUtil.getSumReturnNum(orderId);
		if(sumReturnNum == null || ConvertUtil.toInt(sumReturnNum) < 0){
			return BeanConvertUtil.convertRespResult(RespStatusCode.REVERSE_API_ERROR, "");
		}

		if(sumReturnNum == 0){
			if(orderStatus == 400){
				return processResult(dbOrder,true);
			}else{
				logger.info("sumReturnNum为0,orderStatus != 400,直接返回成功,orderId:{},sumReturnNum:{},orderStatus:{}",
						orderId,sumReturnNum,orderStatus);
				return BeanConvertUtil.convertRespResult(RespStatusCode.SUCCESS, "");
			}
		}

		return compareSumReturnNumAllotQuantity(dbOrder,sumReturnNum);
	}

	/**
	 * 对比销退数量和配货数量判断交易是否成功
	 */
	private ApiResponse compareSumReturnNumAllotQuantity(Order order,int sumReturnNum){
		Long orderId = order.getOrderId();
		//查询订单明细
		List<OrderItem> dbOrderItemList = orderDataService.getOrderItemsByOrderId(orderId,order.isHistoryOrder());
		if(dbOrderItemList == null || dbOrderItemList.size() <= 0){
			logger.error("查询订单明细为空,orderId:{}",orderId);
			return BeanConvertUtil.convertRespResult(RespStatusCode.RECORD_NOT_EXIST,"orderItems query null");
		}
		//去掉虚拟母品和电子书商品
		List<OrderItem> remainOrderItemList = CommonUtil.filterOrderItems(dbOrderItemList);
		int sumAllotQuantity = summuryItemAllotQuantity(remainOrderItemList);

		if(sumReturnNum >= sumAllotQuantity){
			logger.info("全部退货,orderId:{},sumReturnNum:{},sumAllotQuantity:{}",
					orderId,sumReturnNum,sumAllotQuantity);
			if(ConvertUtil.toInt(order.getOrderStatus()) == 1100){
				return BeanConvertUtil.convertRespResult(RespStatusCode.SUCCESS, "");
			}
			return processResult(order,false);
		}else{
			logger.info("部分退货,orderId:{},sumReturnNum:{},sumAllotQuantity:{}",
					orderId,sumReturnNum,sumAllotQuantity);
			if(ConvertUtil.toInt(order.getOrderStatus()) == 1000){
				return BeanConvertUtil.convertRespResult(RespStatusCode.SUCCESS, "");
			}
			return processResult(order,true);
		}
	}

	/**
	 * 汇总明细配货数量
	 */
	private int summuryItemAllotQuantity(List<OrderItem> orderItemList){
		if(orderItemList == null || orderItemList.size() <= 0){
			return 0;
		}
		int sumQuantity = 0;
		for(OrderItem orderItem : orderItemList){
			sumQuantity += ConvertUtil.toInt(orderItem.getAllotQuantity());
		}
		return sumQuantity;
	}

	/**
	 * 交易结果处理
	 * @param isSuccess(交易是否成功)
	 */
	private ApiResponse processResult(Order order,boolean isSuccess){
		Long orderId = order.getOrderId();
		boolean isUpdate = updateOrderData(order,isSuccess);
		if(!isUpdate){
			return BeanConvertUtil.convertRespResult(RespStatusCode.UPDATE_DATA_FAIL, "");
		}

		if(!orderEventService.insertOrderEventQueue(orderId,isSuccess)){
			return BeanConvertUtil.convertRespResult(RespStatusCode.INSERT_ORDER_EVENT_QUEUE_FAIL, "");
		}

		if(!orderEventService.sendOrderEventMq(orderId,isSuccess)){
			return BeanConvertUtil.convertRespResult(RespStatusCode.SEND_EVENT_QUEUE_MSG_FAIL, "");
		}

		return BeanConvertUtil.convertRespResult(RespStatusCode.SUCCESS, "");
	}

	/**
	 * 更新订单数据
	 * @param isSuccess(交易是否成功)
	 */
	private boolean updateOrderData(Order order,boolean isSuccess){
		Long orderId = order.getOrderId();
		OrderDataCriteria orderDataCriteria = new OrderDataCriteria();
		orderDataCriteria.setOrderId(orderId);

		//orders
		OrderCriteria orderCriteria = BeanConvertUtil.buildOrderCriteria(orderId,isSuccess);
		orderDataCriteria.setOrderCriteria(orderCriteria);

		//一单多件
		if(ConvertUtil.toInt(order.getIsSplitPackage()) == 1){
			OrderSplitPackageCriteria orderSplitPackageCriteria
					= BeanConvertUtil.buildOrderSplitPackageCriteria(orderId,isSuccess);
			orderDataCriteria.setOrderSplitPackageCriteria(orderSplitPackageCriteria);
		}

		//order_trans_log
		OrderTranslogCriteria orderTranslogCriteria
				= BeanConvertUtil.buildOrderTranslogCriteria(orderId,isSuccess);
		orderDataCriteria.setOrderTranslogCriteria(orderTranslogCriteria);

		boolean isUpdateSuccess = orderDataService.updateOrderData(orderDataCriteria,order.isHistoryOrder());
		if(!isUpdateSuccess){
			logger.error("更新订单数据失败,orderId:{}",orderId);
			return false;
		}
		return true;
	}

}
