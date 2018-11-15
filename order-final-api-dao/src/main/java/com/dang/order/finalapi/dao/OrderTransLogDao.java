package com.dang.order.finalapi.dao;

import com.dang.order.finalapi.dao.criteria.OrderTranslogCriteria;
import com.dang.order.persistence.ds.SessionContext;

public interface OrderTransLogDao {
	public int insert(SessionContext ctx, OrderTranslogCriteria orderTranslogCriteria);

}
