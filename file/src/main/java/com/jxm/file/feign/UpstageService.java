package com.jxm.file.feign;

import com.jxm.common.api.CommonResult;
import com.jxm.file.config.FeignRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.ParseException;
import java.util.Map;

/**
 * 认证服务远程调用
 */
@FeignClient(value ="upstage-service",configuration = FeignRequestInterceptor.class)
public interface UpstageService {

    @GetMapping(value = "/admin/getCurrentAdmin")
    CommonResult getCurrentAdmin() throws ParseException;

}
