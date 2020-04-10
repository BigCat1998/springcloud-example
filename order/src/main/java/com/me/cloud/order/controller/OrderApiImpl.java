package com.me.cloud.order.controller;

import com.me.cloud.api.OrderApi;
import com.me.cloud.entity.Order;
import com.me.cloud.entity.User;
import com.me.cloud.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class OrderApiImpl implements OrderApi {

    private final OrderService orderService;

    public OrderApiImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public List<Order> selectUserOrders(User user) {
        return orderService.selectUserOrders(user.getUserId());
    }

}
