package com.jxm.business.controller;

import com.github.pagehelper.PageHelper;
import com.jxm.business.dto.SurveySubmitDto;
import com.jxm.business.model.SurveyUserParam;
import com.jxm.business.service.SurveyService;
import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @RequestMapping(value = "/getSurveyBySearch", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getSurveyBySearch(@RequestParam(defaultValue = "") String[] date,
                                        @RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(value = "tempId", required = false) Long tempId,
                                        @RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        String startDate = null;
        String endDate = null;
        if (date.length == 2) {
            startDate = date[0];
            endDate = date[1];
        }
        PageHelper.startPage(pageNum, pageSize);
        List<SurveyUserParam> tempUserParams=surveyService.getSurveyBySearch(startDate, endDate,keyword,tempId);
        return CommonResult.success(CommonPage.restPage(tempUserParams),"请求成功");
    }

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
