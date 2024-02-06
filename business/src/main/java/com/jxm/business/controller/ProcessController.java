package com.jxm.business.controller;

import com.github.pagehelper.PageHelper;
import com.jxm.business.dto.ProcessDto;
import com.jxm.business.model.ProcessParam;
import com.jxm.business.service.ProcessService;
import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @ApiOperation(value = "添加流程")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult add(@RequestBody ProcessDto processDto) {
        int count = processService.add(processDto);
        if (count<=0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @ApiOperation("修改指定流程信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody ProcessDto processDto) {
        int count = processService.update(id, processDto);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("后台显示流程列表")
    @RequestMapping(value = "/getProcessBySearch", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getNewsBySearch(@RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProcessParam> processLists=processService.getProcessByKeyword(keyword);
        return CommonResult.success(CommonPage.restPage(processLists),"请求成功");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        //删除流程信息
        int count=processService.delete(id);
        if(count<0)
            return CommonResult.failed("删除流程信息错误");
        return CommonResult.success();
    }

}
