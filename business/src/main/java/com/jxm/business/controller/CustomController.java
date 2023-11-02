package com.jxm.business.controller;

import com.github.pagehelper.PageHelper;
import com.jxm.business.dto.CustomSalesParam;
import com.jxm.business.model.CustomParam;
import com.jxm.business.model.CustomPostParam;
import com.jxm.business.model.NewsParam;
import com.jxm.business.model.NewsPostParam;
import com.jxm.business.service.CustomService;
import com.jxm.business.service.NewsService;
import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/custom")
public class CustomController {

    @Autowired
    private CustomService customService;

    @ApiOperation(value = "后台显示客户列表")
    @RequestMapping(value = "/getCustomBySearch", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getCustomBySearch(@RequestParam(value = "keyword", required = false) String keyword,
                                          @RequestParam(value = "salesPersonId", required = false) Long salesPersonId,
                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {
        List<CustomParam> customParamList = customService.getCustomBySearch(pageNum, pageSize, keyword,salesPersonId);
        return CommonResult.success(CommonPage.restPage(customParamList), "请求成功");
    }


    @ApiOperation(value = "后台查询客户（用于编辑）")
    @RequestMapping(value = "/queryContent/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CustomParam> queryCustomContent(@PathVariable Long id){
        return CommonResult.success(customService.queryCustomContent(id));
    }


    @ApiOperation(value = "添加客户")
    @RequestMapping(value = "/addCustom", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addCustom(@RequestBody CustomSalesParam customSalesParam) {
        int count= customService.addCustom(customSalesParam);
        if (count<0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "修改客户内容")
    @RequestMapping(value = "/updateCustom", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateCustom(@RequestBody CustomSalesParam customSalesParam){
        int count=customService.updateCustom(customSalesParam);
        if(count!=0)
            return CommonResult.success();
        else
            return CommonResult.failed();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        //删除客户信息
        int count=customService.delete(id);
        if(count<0)
            return CommonResult.failed("删除客户信息错误");
        return CommonResult.success();
    }

    @RequestMapping(value = "/handleBatchDelete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteBatch(@RequestBody Long[] multipleSelectionId) {
        List<Long> idList= Arrays.stream(multipleSelectionId).collect(Collectors.toList());
        //删除多个客户信息
        int count=customService.deleteBatchCustom(idList);
        if(count<0)
            return CommonResult.failed("删除客户信息错误");
        return CommonResult.success();
    }

}
