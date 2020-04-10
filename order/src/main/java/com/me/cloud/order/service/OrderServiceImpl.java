package com.me.cloud.order.service;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.me.cloud.order.dao.OrderRepository;
import com.me.cloud.order.feign.CommodityApiClient;
import com.me.cloud.order.feign.CommoditySelectApiClient;
import com.me.cloud.order.feign.UserApiClient;
import com.me.cloud.entity.Commodity;
import com.me.cloud.entity.Order;
import com.me.cloud.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final CommodityApiClient commodityApiClient;

    private final CommoditySelectApiClient commoditySelectApiClient;

    private final UserApiClient userApiClient;

    private final OrderRepository orderRepository;

    public OrderServiceImpl(CommodityApiClient commodityApiClient, UserApiClient userApiClient, OrderRepository orderRepository, CommoditySelectApiClient commoditySelectApiClient) {
        this.commodityApiClient = commodityApiClient;
        this.userApiClient = userApiClient;
        this.orderRepository = orderRepository;
        this.commoditySelectApiClient = commoditySelectApiClient;
    }

    @Override
    @LcnTransaction
    public Order createOrder(Integer userId, Integer commodityId) {
        Commodity commodity = new Commodity();
        commodity.setCommodityId(commodityId);
        commodity.setCommodityStock(6);

        if (commodityApiClient.updateCommodityStock(commodity) <= 0) {
            throw new RuntimeException("已售空");
        }
        // 获取价格
        Double commodityPrice = commoditySelectApiClient.selectCommodityPrice(commodity);
        if (commodityPrice == 0D){
            throw new RuntimeException("获取价格失败");
        }

        User user = new User();
        user.setUserId(userId);
        user.setUserBalance(commodityPrice * commodity.getCommodityStock());

        if (userApiClient.updateUserBalance(user) <= 0) {
            throw new RuntimeException("余额不足");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityId(commodityId);

        Integer orderId = orderRepository.save(order);

        // 通过commodityId，模拟出现异常回滚
        if (commodityId > 3) {
            throw new RuntimeException("异常回滚");
        }

        if (orderId == null) {
            throw new RuntimeException("下单失败");
        }

        return orderRepository.selectOrderByOrderId(orderId);
    }

    @Override
    public List<Order> selectUserOrders(Integer userId) {
        return orderRepository.selectOrderByUserId(userId);
    }

}
