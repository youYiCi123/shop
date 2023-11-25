package com.jxm.business.controller;

import com.jxm.business.dto.ActiveSubmitDto;
import com.jxm.business.service.ActiveService;
import com.jxm.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

@Controller
@RequestMapping("/active")
public class ActiveController {

    @Autowired
    private ActiveService activeService;

    /**
     *用户提交填写后的调查问卷
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult submitContent(@RequestBody ActiveSubmitDto activeSubmitDto) throws ParseException {
        int count = activeService.submitContent(activeSubmitDto);
        if(count>0){
            return CommonResult.success();
        }else if(count==-2){
            return CommonResult.failed("以提交，请不要重复提交");
        }else{
            return CommonResult.failed("提交失败");
        }
    }
}
