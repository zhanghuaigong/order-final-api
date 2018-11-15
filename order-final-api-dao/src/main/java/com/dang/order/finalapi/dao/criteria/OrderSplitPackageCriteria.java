package com.dang.order.finalapi.dao.criteria;

import java.util.List;

public class OrderSplitPackageCriteria {
	private Long orderId;

	private Integer packageStatus;

	private List<Integer> oldStatusList;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getPackageStatus() {
		return packageStatus;
	}

	public void setPackageStatus(Integer packageStatus) {
		this.packageStatus = packageStatus;
	}

	public List<Integer> getOldStatusList() {
		return oldStatusList;
	}

	public void setOldStatusList(List<Integer> oldStatusList) {
		this.oldStatusList = oldStatusList;
	}
}
