package com.me.cloud.order.dao;

import com.me.cloud.entity.Order;

import java.util.List;

public interface OrderRepository{
	
	Integer save(Order order);
	
	Order selectOrderByOrderId(Integer orderId);

	List<Order> selectOrderByUserId(Integer userId);

}
