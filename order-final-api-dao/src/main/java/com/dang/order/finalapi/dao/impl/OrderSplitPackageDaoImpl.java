package com.dang.order.finalapi.dao.impl;

import com.dang.order.finalapi.dao.OrderSplitPackageDao;
import com.dang.order.finalapi.dao.criteria.OrderSplitPackageCriteria;
import com.dang.order.persistence.ds.SessionContext;
import com.dang.order.persistence.ds.SessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class OrderSplitPackageDaoImpl implements OrderSplitPackageDao {
	private String namespace = OrderSplitPackageDao.class.getName();
	@Resource
	private SessionTemplate sessionTemplate;

	@Override
	public int update(SessionContext sct, OrderSplitPackageCriteria orderSplitPackageCriteria) {
		return sessionTemplate.update(sct, namespace + ".update", orderSplitPackageCriteria);
	}
}
