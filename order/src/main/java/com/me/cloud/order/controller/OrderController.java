package com.me.cloud.order.controller;

import com.me.cloud.entity.Order;
import com.me.cloud.entity.User;
import com.me.cloud.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 创建订单
     *
     * @param userId
     * @param commodityId
     */
    @GetMapping("/createOrder")
    public Order createOrder(@RequestParam("userId") Integer userId,
                             @RequestParam("commodityId") Integer commodityId) {
        return orderService.createOrder(userId, commodityId);
    }

}
