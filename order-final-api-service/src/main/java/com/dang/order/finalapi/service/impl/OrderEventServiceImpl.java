package com.dang.order.finalapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.dang.order.OrderBase_messagequeue_api.api.OrderEventMessageQueueService;
import com.dang.order.OrderBase_messagequeue_api.dto.OrderEventMessageQueue;
import com.dang.order.OrderEventQueue.OrderEventQueue;
import com.dang.order.event.api.OrderEventQueueService;
import com.dang.order.finalapi.service.OrderEventService;
import com.dang.order.finalapi.service.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.ws.rs.HEAD;
import java.util.Date;

@Service
public class OrderEventServiceImpl implements OrderEventService {
	private static final Logger logger = LoggerFactory.getLogger(OrderEventServiceImpl.class);

	@Resource
	private OrderEventMessageQueueService orderEventMessageQueueService;
	@Resource
	private OrderEventQueueService orderEventQueueService;

	@Override
	public boolean sendOrderEventMq(Long orderId, boolean isSuccess) {
		try {
			logger.info("第一次修改");
			logger.info("第二次修改,我再加点日志,合并了");


			OrderEventMessageQueue mQueue = new OrderEventMessageQueue();
			mQueue.setOrderId(orderId);
			if(isSuccess){
				mQueue.setEventType(Constants.ORDER_MSG_EVENT_TYPE_SUCCESS);
				mQueue.setEventArg(Constants.ORDER_MSG_EVENT_ARG_SUCCESS);
			}else{
				mQueue.setEventType(Constants.ORDER_MSG_EVENT_TYPE_FAIL);
				mQueue.setEventArg(Constants.ORDER_MSG_EVENT_ARG_FAIL);
			}
			mQueue.setEventDate(new Date());

			String message = JSON.toJSONString(mQueue);
			logger.info("发送order-event消息,orderId:{},message:{}",orderId,message);
			int sendMessageSuccess = orderEventMessageQueueService.sendMessage(message);
			if(sendMessageSuccess == 0){
				logger.error("发送order-eventMQ消息失败,orderId:{}",orderId);
				return false;
			}
			logger.info("发送order-eventMQ消息成功,orderId:{},queue:{}",orderId,message);
			return true;
		}catch (Exception e){
			logger.error("发送order-eventMQ消息异常,orderId:" + orderId,e);
			return false;
		}
	}

	@Override
	public boolean insertOrderEventQueue(Long orderId, boolean isSuccess) {
		try{
			OrderEventQueue queue = new OrderEventQueue();
			queue.setSourceId(orderId);
			queue.setSourceTypeId(0);
			if(isSuccess){
				queue.setEventTypeId(Constants.ORDER_EVENT_QUEUE_TYPE_SUCCESS);
			}else{
				queue.setEventTypeId(Constants.ORDER_EVENT_QUEUE_TYPE_FAIL);
			}
			queue.setCreationDate(new Date());
			queue.setEventDate(new Date());
			queue.setEventArg(0L);
			int count = orderEventQueueService.insert(queue);
			if(count < 1){
				logger.error("写order_event_queue失败,orderId:{}",orderId);
				return false;
			}
			logger.info("写order_event_queue成功,orderId:{}",orderId);
			return true;
		}catch (Exception e){
			logger.error("写order_event_queue异常,orderId:" + orderId,e);
			return false;
		}
	}
}
