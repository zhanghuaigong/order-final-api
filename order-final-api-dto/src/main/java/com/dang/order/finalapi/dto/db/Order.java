package com.dang.order.finalapi.dto.db;

public class Order {
	private Long orderId;

	private Integer orderStatus;

	private Integer isSplitPackage;

	//是否是历史库订单
	private boolean isHistoryOrder;

	public boolean isHistoryOrder() {
		return isHistoryOrder;
	}

	public void setHistoryOrder(boolean historyOrder) {
		isHistoryOrder = historyOrder;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getIsSplitPackage() {
		return isSplitPackage;
	}

	public void setIsSplitPackage(Integer isSplitPackage) {
		this.isSplitPackage = isSplitPackage;
	}
}
