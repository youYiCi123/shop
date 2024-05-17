package com.jxm.business.controller;

import com.jxm.business.model.RemindParam;
import com.jxm.business.model.SupplierParam;
import com.jxm.business.service.SupplierService;
import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;
    
    @ApiOperation(value = "后台显示供应商列表")
    @RequestMapping(value = "/getSupplierBySearch", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getSupplierBySearch(@RequestParam(defaultValue = "") String[] businessAuthTime,
                                            @RequestParam(value = "keyword", required = false) String keyword,
                                            @RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        List<SupplierParam> SupplierParamList = supplierService.getSupplierBySearch(pageNum, pageSize, keyword,businessAuthTime);
        return CommonResult.success(CommonPage.restPage(SupplierParamList), "请求成功");
    }


    @ApiOperation(value = "后台查询供应商（用于编辑）")
    @RequestMapping(value = "/querySupplierContent/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SupplierParam> querySupplierContent(@PathVariable Long id){
        return CommonResult.success(supplierService.querySupplierContent(id));
    }


    @ApiOperation(value = "添加供应商")
    @RequestMapping(value = "/addSupplier", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addSupplier(@RequestBody SupplierParam supplierParam) {
        int count= supplierService.addSupplier(supplierParam);
        if (count<0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "修改供应商内容")
    @RequestMapping(value = "/updateSupplier", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateSupplier(@RequestBody SupplierParam supplierParam){
        int count=supplierService.updateSupplier(supplierParam);
        if(count!=0)
            return CommonResult.success();
        else
            return CommonResult.failed();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        //删除供应商信息
        int count=supplierService.delete(id);
        if(count<0)
            return CommonResult.failed("删除供应商信息错误");
        return CommonResult.success();
    }

    @RequestMapping(value = "/handleBatchDelete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteBatch(@RequestBody Long[] multipleSelectionId) {
        List<Long> idList= Arrays.stream(multipleSelectionId).collect(Collectors.toList());
        //删除多个供应商信息
        int count=supplierService.deleteBatchSupplier(idList);
        if(count<0)
            return CommonResult.failed("删除供应商信息错误");
        return CommonResult.success();
    }

    @ApiOperation(value = "获取供应商提醒人内容")
    @RequestMapping(value = "/getRemind", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getRemind() {
        RemindParam remind = supplierService.getRemind();
        return CommonResult.success(remind);
    }

    @ApiOperation(value = "设置供应商提醒人内容")
    @RequestMapping(value = "/setRemind", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult setRemind(@RequestBody RemindParam remindParam) {
        int count= supplierService.setRemind(remindParam);
        if (count<0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

}
