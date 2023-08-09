package com.jxm.prod.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service",path = "/user",fallback = UserFeignServiceFallBack.class)
public interface UserFeignService {

    @GetMapping("/getUser")
     String getUser();

    @GetMapping("/getUser2")
    String getUser2();
}
