package com.dang.order.finalapi.dao;

import com.dang.order.finalapi.dto.db.OrderItem;

import java.util.List;

public interface OrderItemDao {
	public List<OrderItem> getByOrderId(Long orderId,String dataSource);
}
