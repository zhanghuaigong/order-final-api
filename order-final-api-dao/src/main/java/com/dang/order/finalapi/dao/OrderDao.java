package com.dang.order.finalapi.dao;

import com.dang.order.finalapi.dao.criteria.OrderCriteria;
import com.dang.order.finalapi.dto.db.Order;
import com.dang.order.persistence.ds.SessionContext;

public interface OrderDao {
	public Order getByOrderId(Long orderId,String dataSource);

	public int update(SessionContext sct,OrderCriteria orderCriteria);
}
