package com.me.cloud.order.feign;

import com.me.cloud.api.CommoditySelectApi;
import com.me.cloud.common.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "commodity", configuration = FeignConfiguration.class, fallbackFactory = CommoditySelectApiClientFallbackFactory.class)
public interface CommoditySelectApiClient extends CommoditySelectApi {
}
