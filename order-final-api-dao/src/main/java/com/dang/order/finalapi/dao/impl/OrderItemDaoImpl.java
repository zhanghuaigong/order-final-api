package com.dang.order.finalapi.dao.impl;

import com.dang.order.finalapi.dao.OrderItemDao;
import com.dang.order.finalapi.dto.db.OrderItem;
import com.dang.order.persistence.ds.SessionContext;
import com.dang.order.persistence.ds.SessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderItemDaoImpl implements OrderItemDao {
	private String namespace = OrderItemDao.class.getName();
	@Resource
	private SessionTemplate sessionTemplate;

	@Override
	public List<OrderItem> getByOrderId(Long orderId,String dataSource) {
		Map<String,Object> params = new HashMap<>();
		params.put("orderId",orderId);
		return sessionTemplate.selectList(new SessionContext(dataSource),
				namespace + ".selectByOrderId", params);
	}
}
