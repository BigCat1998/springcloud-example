package com.me.cloud.user.service;

import com.me.cloud.entity.Order;

import java.util.List;

public interface UserService {

    List<Order> selectUserOrders(Integer userId);

}
