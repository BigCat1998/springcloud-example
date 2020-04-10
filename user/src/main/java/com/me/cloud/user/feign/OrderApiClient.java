package com.me.cloud.user.feign;

import com.me.cloud.api.OrderApi;
import com.me.cloud.common.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order", configuration = FeignConfiguration.class, fallback = OrderApiClientFallback.class)
public interface OrderApiClient extends OrderApi {
}
