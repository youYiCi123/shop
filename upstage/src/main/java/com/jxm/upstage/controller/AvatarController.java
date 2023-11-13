package com.jxm.upstage.controller;

import com.jxm.common.api.CommonResult;
import com.jxm.upstage.config.ServerConfig;
import com.jxm.upstage.config.TianYiConfig;
import com.jxm.upstage.feign.FileService;
import com.jxm.upstage.utils.FileUploadUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("avatar")
public class AvatarController {

    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public CommonResult uploadFile(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = TianYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = "http://localhost:8079/file-service/previewByFilePath"+ fileName;
            Map<String, Object> info = new HashMap<>();
            //把用户ID设置到JWT中
            info.put("fileName", file.getOriginalFilename());
            info.put("url", url);
            return CommonResult.success(info);
        }
        catch (Exception e)
        {
            return CommonResult.failed(e.getMessage());
        }
    }
}
