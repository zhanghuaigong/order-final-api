package com.dang.order.finalapi.dao;

import com.dang.order.finalapi.dao.criteria.OrderSplitPackageCriteria;
import com.dang.order.persistence.ds.SessionContext;

public interface OrderSplitPackageDao {
	public int update(SessionContext sct, OrderSplitPackageCriteria orderSplitPackageCriteria);
}
