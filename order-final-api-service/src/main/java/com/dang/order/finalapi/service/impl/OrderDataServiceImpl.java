package com.dang.order.finalapi.service.impl;

import com.dang.order.finalapi.dao.OrderDao;
import com.dang.order.finalapi.dao.OrderItemDao;
import com.dang.order.finalapi.dao.OrderSplitPackageDao;
import com.dang.order.finalapi.dao.OrderTransLogDao;
import com.dang.order.finalapi.dao.criteria.OrderCriteria;
import com.dang.order.finalapi.dao.criteria.OrderDataCriteria;
import com.dang.order.finalapi.dao.criteria.OrderSplitPackageCriteria;
import com.dang.order.finalapi.dao.criteria.OrderTranslogCriteria;
import com.dang.order.finalapi.dto.db.Order;
import com.dang.order.finalapi.dto.db.OrderItem;
import com.dang.order.finalapi.service.OrderDataService;
import com.dang.order.finalapi.service.common.Configs;
import com.dang.order.finalapi.service.common.Constants;
import com.dang.order.persistence.ds.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderDataServiceImpl implements OrderDataService {
	private static final Logger logger = LoggerFactory.getLogger(OrderDataServiceImpl.class);

	@Resource
	private OrderDao orderDao;
	@Resource
	private OrderSplitPackageDao orderSplitPackageDao;
	@Resource
	private OrderTransLogDao orderTransLogDao;
	@Resource
	private JedisCluster jedisCluster;
	@Resource
	private OrderItemDao orderItemDao;

	@Override
	public Order getOrderByOrderId(Long orderId) {
		Order order = orderDao.getByOrderId(orderId,"order" + orderId % 2);
		if(order == null){
			order = orderDao.getByOrderId(orderId,"orderHis" + orderId % 2);
			if(order != null){
				order.setHistoryOrder(true);
			}
		}else{
			order.setHistoryOrder(false);
		}
		return order;
	}

	@Override
	public boolean updateOrderData(OrderDataCriteria orderDataCriteria,boolean isHistoryOrder) {
		Long orderId = orderDataCriteria.getOrderId();
		//开启事务
		String dataSource = getDataSource(orderId,isHistoryOrder);
		SessionContext sct = new SessionContext(dataSource, false, false);
		try {
			OrderCriteria orderCriteria = orderDataCriteria.getOrderCriteria();
			if(!updateOrder(sct,orderCriteria,orderId)){
				sct.rollback();
				return false;
			}

			OrderSplitPackageCriteria orderSplitPackageCriteria
					= orderDataCriteria.getOrderSplitPackageCriteria();
			if(orderSplitPackageCriteria != null ){
				if(!updateOrderSplitPackage(sct,orderSplitPackageCriteria,orderId)){
					sct.rollback();
					return false;
				}
			}

			OrderTranslogCriteria orderTranslogCriteria = orderDataCriteria.getOrderTranslogCriteria();
			if(!insertTransLog(sct,orderTranslogCriteria,orderId)){
				sct.rollback();
				return false;
			}

			sct.commit();
			logger.info("更新订单信息成功,orderId:{}",orderId);
			setOrderTransLogCache(orderId,Constants.TRANSLOG_OPERATOR_TYPE,Constants.TRANSLOG_OPERATOR_ID);
			return true;
		}catch(Exception e){
			logger.error("更新订单信息持久化异常,orderId:" + orderId,e);
			sct.rollback();
			return false;
		}finally{
			sct.close();
		}
	}

	@Override
	public List<OrderItem> getOrderItemsByOrderId(Long orderId,boolean isHis) {
		String ds = getDataSource(orderId,isHis);
		return orderItemDao.getByOrderId(orderId,ds);
	}

	private boolean updateOrder(SessionContext sct,OrderCriteria orderCriteria,Long orderId){
		if (orderCriteria == null) {
			logger.error("更新数据错误,没有order信息,orderId:{}", orderId);
			return false;
		}
		int updateOrderCount = orderDao.update(sct, orderCriteria);
		if (updateOrderCount < 1) {
			logger.error("更新orders表信息失败,orderId:{}", orderId);
			return false;
		}
		return true;
	}

	private boolean updateOrderSplitPackage(SessionContext sct, OrderSplitPackageCriteria splitPackageCriteria, Long orderId){
		if (splitPackageCriteria == null) {
			logger.error("更新数据错误,没有orderSplitPackage信息,orderId:{}", orderId);
			return false;
		}
		int updateOrderCount = orderSplitPackageDao.update(sct, splitPackageCriteria);
		if (updateOrderCount < 1) {
			logger.error("更新orderSplitPackage表信息失败,orderId:{}", orderId);
			return false;
		}
		return true;
	}

	private boolean insertTransLog(SessionContext sct, OrderTranslogCriteria orderTranslogCriteria, Long orderId){
		boolean isExist = getOrderTransLogCache(orderId,Constants.TRANSLOG_OPERATOR_TYPE, Constants.TRANSLOG_OPERATOR_ID);
		if(isExist){
			logger.info("命中order_trans_log缓存,不再写trans_log,orderId:{}",orderId);
			return true;
		}

		if(orderTranslogCriteria == null){
			logger.error("更新数据错误,没有ordertranslog信息,orderId:{}",orderId);
			return false;
		}
		int insertTransCount = orderTransLogDao.insert(sct,orderTranslogCriteria);
		if(insertTransCount < 1){
			logger.error("写ordertranslog表信息失败,orderId:{}",orderId);
			return false;
		}
		return true;
	}

	private void setOrderTransLogCache(Long orderId,Integer operatorType,Integer operatorId){
		try{
			String redisKey = String.format(Configs.REDIS_KEY_TRANSLOG_FINAL,orderId,operatorType,operatorId);
			jedisCluster.setex(redisKey, Configs.REDIS_TIMEOUT_TRANSLOG_FINAL,"1");
			logger.info("写order_trans_log缓存成功,orderId:{},key:{},value:{}",orderId,redisKey,"1");
		}catch (Exception e){
			logger.error("写order_trans_log缓存异常,orderId:" + orderId,e);
		}
	}

	private boolean getOrderTransLogCache(Long orderId,Integer operatorType,Integer operatorId){
		try{
			String redisKey = String.format(Configs.REDIS_KEY_TRANSLOG_FINAL,orderId,operatorType,operatorId);
			String value = jedisCluster.get(redisKey);
			logger.info("查找order_trans_log缓存,orderId:{},key:{},value:{}",orderId,redisKey,value);
			if(value != null){
				return true;
			}
			return false;
		}catch (Exception e){
			logger.error("查找order_trans_log缓存异常,orderId:" + orderId,e);
			return false;
		}
	}

	private String getDataSource(Long orderId,boolean isHis){
		String dataSource = "order" + orderId % 2;
		if(isHis){
			dataSource = "orderHis" + orderId % 2;
		}
		return dataSource;
	}
}
