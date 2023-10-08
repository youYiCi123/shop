package com.jxm.upstage.feign;

import com.jxm.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 获取用户File初始化数据
 */
@FeignClient("file-service")
public interface FileService {

    @GetMapping(value = "file/getUserTopFileInfo")
    CommonResult getUserTopFileInfo(@RequestParam Long depId);

    @PostMapping(value = "file/createDepRootFolder")
    CommonResult createFolder(@RequestParam("parentId") Long parentId,@RequestParam("folderName")  String folderName,
                              @RequestParam("userId")  Long userId,@RequestParam("depId") Long depId);
}
