package com.jxm.business.controller;

import com.jxm.business.model.CertificateParam;
import com.jxm.business.model.RemindParam;
import com.jxm.business.service.CertificateService;
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
@RequestMapping("/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @ApiOperation(value = "后台显示证书列表")
        @RequestMapping(value = "/getListBySearch", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getNewsBySearch(@RequestParam(defaultValue = "") String[] lastDeclareTime,
                                        @RequestParam(defaultValue = "") String[] firstRegisterTime,
                                        @RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(value = "category", defaultValue = "0") Long category,
                                        @RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {

        List<CertificateParam> NewsBrieflyList=certificateService.getListBySearch(lastDeclareTime, firstRegisterTime,keyword,category,pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(NewsBrieflyList),"请求成功");
    }

    @ApiOperation(value = "后台查询证书（用于编辑）")
    @RequestMapping(value = "/queryContent/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CertificateParam> queryContent(@PathVariable Long id){
        return CommonResult.success(certificateService.queryContent(id));
    }

    @ApiOperation(value = "获取证书提醒人内容")
    @RequestMapping(value = "/getRemind", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getRemind() {
        RemindParam remind = certificateService.getRemind();
            return CommonResult.success(remind);
    }

    @ApiOperation(value = "设置证书提醒人内容")
    @RequestMapping(value = "/setRemind", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult setRemind(@RequestBody RemindParam remindParam) {
        int count= certificateService.setRemind(remindParam);
        if (count<0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "添加证书")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult add(@RequestBody CertificateParam certificateParam) {
        int count= certificateService.add(certificateParam);
        if (count<0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "修改证书内容")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@RequestBody CertificateParam certificateParam){
        int count=certificateService.update(certificateParam);
        if(count!=0)
            return CommonResult.success();
        else
            return CommonResult.failed();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        //删除客户信息
        int count=certificateService.delete(id);
        if(count<0)
            return CommonResult.failed("删除证书信息错误");
        return CommonResult.success();
    }

    @RequestMapping(value = "/handleBatchDelete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteBatch(@RequestBody Long[] multipleSelectionId) {
        List<Long> idList= Arrays.stream(multipleSelectionId).collect(Collectors.toList());
        //删除多个证书信息
        int count=certificateService.deleteBatch(idList);
        if(count<0)
            return CommonResult.failed("删除证书信息错误");
        return CommonResult.success();
    }

}
