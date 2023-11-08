package com.jxm.business.controller;

import com.jxm.business.dto.EmailVo;
import com.jxm.business.service.SmsService;
import com.jxm.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Object> sendSms(@RequestParam(value = "phone") String phone,
                                        @RequestParam(value = "customName", required = false) String customName,
                                        @RequestParam(value = "licenseTime", required = false) String licenseTime){
        Boolean sendBoolean = smsService.send(phone, customName, licenseTime);
        if(sendBoolean){
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}
