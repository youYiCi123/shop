/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.jxm.business.controller;

import com.jxm.business.dto.EmailVo;
import com.jxm.business.model.EmailConfig;
import com.jxm.business.service.EmailService;
import com.jxm.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 发送邮件
 * @author 郑杰
 * @date 2018/09/28 6:55:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
@Api(tags = "工具：邮件管理")
public class EmailController {

    private final EmailService emailService;

    @GetMapping
    public CommonResult<EmailConfig> queryEmailConfig(){
        return CommonResult.success(emailService.find());
    }

    @PutMapping
    @ApiOperation("配置邮件")
    public CommonResult<Object> updateEmailConfig(@Validated @RequestBody EmailConfig emailConfig) throws Exception {
        emailService.config(emailConfig,emailService.find());
        return CommonResult.success();
    }

    @PostMapping
    @ApiOperation("发送邮件")
    public CommonResult<Object> sendEmail(@Validated @RequestBody EmailVo emailVo){
        emailService.send(emailVo,emailService.find());
        return CommonResult.success();
    }
}
