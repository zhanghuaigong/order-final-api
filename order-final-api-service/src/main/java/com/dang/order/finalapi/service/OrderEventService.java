package com.dang.order.finalapi.service;

public interface OrderEventService {
	/**
	 * 发送order-event MQ消息
	 */
	public boolean sendOrderEventMq(Long orderId,boolean isSuccess);

	/**
	 * 写入订单事件
	 */
	public boolean insertOrderEventQueue(Long orderId,boolean isSuccess);
}
