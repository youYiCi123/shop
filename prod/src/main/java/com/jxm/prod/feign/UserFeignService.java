package com.jxm.prod.feign;

import com.jxm.common.api.CommonResult;
import com.jxm.prod.dao.SeriesModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service",path = "/user",fallback = UserFeignServiceFallBack.class)
public interface UserFeignService {

    @PostMapping("/create")
    CommonResult create(SeriesModel seriesModel);

}
