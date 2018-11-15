package com.dang.order.finalapi.dto.db;

public class OrderItem {
	private Long orderId;

	private Integer productId;

	private Integer medium;

	private Integer productType;

	private Integer relationType;

	private Integer allotQuantity;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getMedium() {
		return medium;
	}

	public void setMedium(Integer medium) {
		this.medium = medium;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public Integer getRelationType() {
		return relationType;
	}

	public void setRelationType(Integer relationType) {
		this.relationType = relationType;
	}

	public Integer getAllotQuantity() {
		return allotQuantity;
	}

	public void setAllotQuantity(Integer allotQuantity) {
		this.allotQuantity = allotQuantity;
	}
}
