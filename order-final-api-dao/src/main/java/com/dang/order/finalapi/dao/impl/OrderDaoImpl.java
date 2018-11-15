package com.dang.order.finalapi.dao.impl;

import com.dang.order.finalapi.dao.OrderDao;
import com.dang.order.finalapi.dao.criteria.OrderCriteria;
import com.dang.order.finalapi.dto.db.Order;
import com.dang.order.persistence.ds.SessionContext;
import com.dang.order.persistence.ds.SessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderDaoImpl implements OrderDao {
	private String namespace = OrderDao.class.getName();
	@Resource
	private SessionTemplate sessionTemplate;

	@Override
	public Order getByOrderId(Long orderId,String dataSource) {
		Map<String,Object> params = new HashMap<>();
		params.put("orderId",orderId);
		return sessionTemplate.selectOne(new SessionContext(dataSource),
				namespace + ".selectByOrderId", params);
	}

	@Override
	public int update(SessionContext sct, OrderCriteria orderCriteria) {
		return sessionTemplate.update(sct, namespace + ".update", orderCriteria);
	}
}
