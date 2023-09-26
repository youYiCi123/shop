package com.jxm.upstage.feign;

import com.jxm.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 获取用户File初始化数据
 */
@FeignClient("file-service")
public interface FileService {

    @GetMapping(value = "file/getUserTopFileInfo")
    CommonResult getUserTopFileInfo(Long depId);
}
