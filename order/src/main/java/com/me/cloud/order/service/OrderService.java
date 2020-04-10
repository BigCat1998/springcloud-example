package com.me.cloud.order.service;

import com.me.cloud.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Integer userId, Integer commodityId);

    List<Order> selectUserOrders(Integer userId);
}
