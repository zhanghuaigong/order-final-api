package com.dang.order.finalapi.dao.impl;

import com.dang.order.finalapi.dao.OrderTransLogDao;
import com.dang.order.finalapi.dao.criteria.OrderTranslogCriteria;
import com.dang.order.persistence.ds.SessionContext;
import com.dang.order.persistence.ds.SessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class OrderTransLogDaoImpl implements OrderTransLogDao {
	private String namespace = OrderTransLogDao.class.getName();
	@Resource
	private SessionTemplate sessionTemplate;

	@Override
	public int insert(SessionContext ctx, OrderTranslogCriteria orderTranslogCriteria) {
		return sessionTemplate.insert(ctx, namespace + ".insert", orderTranslogCriteria);
	}

}
