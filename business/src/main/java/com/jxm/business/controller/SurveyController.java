package com.jxm.business.controller;

import com.jxm.business.dto.SurveySubmitDto;
import com.jxm.business.dto.TempValueSubmitSingerDto;
import com.jxm.business.service.SurveyService;
import com.jxm.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    /**
     *用户提交填写后的调查问卷
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult submitContent(@RequestBody SurveySubmitDto surveySubmitDto) throws ParseException {
        int count = surveyService.submitContent(surveySubmitDto);
        if(count>0){
            return CommonResult.success();
        }else if(count==-2){
            return CommonResult.failed("以提交，请不要重复提交");
        }else{
            return CommonResult.failed("提交失败");
        }
    }

}
