package com.me.cloud.order.feign;

import com.me.cloud.api.CommodityApi;
import com.me.cloud.common.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "commodity", configuration = FeignConfiguration.class, fallbackFactory = CommodityApiClientFallbackFactory.class)
public interface CommodityApiClient extends CommodityApi {
}
