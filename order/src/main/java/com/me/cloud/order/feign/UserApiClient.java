package com.me.cloud.order.feign;

import com.me.cloud.api.UserApi;
import com.me.cloud.common.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user", configuration = FeignConfiguration.class)
public interface UserApiClient extends UserApi {
}
