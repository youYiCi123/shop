package com.jxm.business.feign;

import com.jxm.business.config.FeignRequestInterceptor;
import com.jxm.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.ParseException;

/**
 * 认证服务远程调用
 */
@FeignClient(value ="upstage-service",configuration = FeignRequestInterceptor.class)
public interface UpstageService {

    @GetMapping(value = "/admin/getCurrentAdmin")
    CommonResult getCurrentAdmin() throws ParseException;

}
