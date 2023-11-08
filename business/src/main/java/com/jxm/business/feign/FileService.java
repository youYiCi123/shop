package com.jxm.business.feign;

import com.jxm.business.config.FeignRequestInterceptor;
import com.jxm.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value ="file-service",configuration = FeignRequestInterceptor.class)
public interface FileService {

    @GetMapping(value = "/file/required/getTheNumberOfFileTypes")
    CommonResult getTheNumberOfFileTypes();
}
