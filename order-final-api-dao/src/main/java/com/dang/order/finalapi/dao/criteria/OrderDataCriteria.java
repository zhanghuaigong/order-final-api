package com.dang.order.finalapi.dao.criteria;

public class OrderDataCriteria {
	private Long orderId;

	private OrderCriteria orderCriteria;

	private OrderSplitPackageCriteria orderSplitPackageCriteria;

	private OrderTranslogCriteria orderTranslogCriteria;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public OrderCriteria getOrderCriteria() {
		return orderCriteria;
	}

	public void setOrderCriteria(OrderCriteria orderCriteria) {
		this.orderCriteria = orderCriteria;
	}

	public OrderSplitPackageCriteria getOrderSplitPackageCriteria() {
		return orderSplitPackageCriteria;
	}

	public void setOrderSplitPackageCriteria(OrderSplitPackageCriteria orderSplitPackageCriteria) {
		this.orderSplitPackageCriteria = orderSplitPackageCriteria;
	}

	public OrderTranslogCriteria getOrderTranslogCriteria() {
		return orderTranslogCriteria;
	}

	public void setOrderTranslogCriteria(OrderTranslogCriteria orderTranslogCriteria) {
		this.orderTranslogCriteria = orderTranslogCriteria;
	}
}
