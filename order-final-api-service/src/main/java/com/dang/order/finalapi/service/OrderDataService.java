package com.dang.order.finalapi.service;

import com.dang.order.finalapi.dao.criteria.OrderDataCriteria;
import com.dang.order.finalapi.dto.db.Order;
import com.dang.order.finalapi.dto.db.OrderItem;

import java.util.List;

public interface OrderDataService {
	public Order getOrderByOrderId(Long orderId);

	public boolean updateOrderData(OrderDataCriteria orderDataCriteria,boolean isHistoryOrder);

	public List<OrderItem> getOrderItemsByOrderId(Long orderId,boolean isHis);
}
