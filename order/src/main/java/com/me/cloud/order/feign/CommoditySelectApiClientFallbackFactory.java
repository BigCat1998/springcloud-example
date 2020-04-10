package com.me.cloud.order.feign;

import com.me.cloud.entity.Commodity;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
public class CommoditySelectApiClientFallbackFactory implements FallbackFactory<CommoditySelectApiClient> {

    @Override
    public CommoditySelectApiClient create(Throwable cause) {
        return new CommoditySelectApiClient() {
            @Override
            public Double selectCommodityPrice(Commodity commodity) {
                return 0D;
            }
        };
    }

}
