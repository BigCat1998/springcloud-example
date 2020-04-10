package com.me.cloud.user.service;

import com.me.cloud.user.feign.OrderApiClient;
import com.me.cloud.entity.Order;
import com.me.cloud.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final OrderApiClient orderApiClient;

    public UserServiceImpl(OrderApiClient orderApiClient) {
        this.orderApiClient = orderApiClient;
    }

    @Override
    public List<Order> selectUserOrders(Integer userId) {
        User user = new User();
        user.setUserId(userId);
        return orderApiClient.selectUserOrders(user);
    }

}
