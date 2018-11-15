package com.dang.order.finalapi.dao.criteria;

import java.util.List;

public class OrderCriteria {
    private Long orderId;

    private Integer orderStatus;

    private List<Integer> oldStatusList;

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

    public List<Integer> getOldStatusList() {
        return oldStatusList;
    }

    public void setOldStatusList(List<Integer> oldStatusList) {
        this.oldStatusList = oldStatusList;
    }
}
