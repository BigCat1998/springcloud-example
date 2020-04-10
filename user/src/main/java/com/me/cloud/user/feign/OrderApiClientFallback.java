package com.me.cloud.user.feign;

import com.me.cloud.entity.Order;
import com.me.cloud.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderApiClientFallback implements OrderApiClient {

    @Override
    public List<Order> selectUserOrders(User user) {
        return null;
    }

}
