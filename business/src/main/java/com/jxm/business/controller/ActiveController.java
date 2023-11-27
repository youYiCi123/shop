package com.jxm.business.controller;

import com.github.pagehelper.PageHelper;
import com.jxm.business.dto.ActiveSubmitDto;
import com.jxm.business.model.ActiveUserParam;
import com.jxm.business.model.SurveyUserParam;
import com.jxm.business.service.ActiveService;
import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/active")
public class ActiveController {

    @Autowired
    private ActiveService activeService;

    @RequestMapping(value = "/getActiveBySearch", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getActiveBySearch(@RequestParam(defaultValue = "") String[] date,
                                          @RequestParam(value = "keyword", required = false) String keyword,
                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {
        String startDate = null;
        String endDate = null;
        if (date.length == 2) {
            startDate = date[0];
            endDate = date[1];
        }
        PageHelper.startPage(pageNum, pageSize);
        List<ActiveUserParam> activeBySearch=activeService.getActiveBySearch(startDate, endDate,keyword);
        return CommonResult.success(CommonPage.restPage(activeBySearch),"请求成功");
    }

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

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        int count=activeService.delete(id);
        if(count<0)
            return CommonResult.failed("删除培训信息错误");
        return CommonResult.success();
    }
}
