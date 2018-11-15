package com.dang.order.finalapi.api.param;

import java.io.Serializable;

public class OrderParam implements Serializable{
    private static final long serialVersionUID = -7934373965510320630L;

    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
