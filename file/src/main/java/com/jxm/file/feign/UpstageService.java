package com.jxm.file.feign;

import com.jxm.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.ParseException;
import java.util.Map;

/**
 * 认证服务远程调用
 */
@FeignClient("upstage-service")
public interface UpstageService {

    @GetMapping(value = "/admin/getCurrentAdmin")
    CommonResult getCurrentAdmin() throws ParseException;

}
