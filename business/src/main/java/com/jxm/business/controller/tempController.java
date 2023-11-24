package com.jxm.business.controller;

import com.jxm.business.dto.TempIdToName;
import com.jxm.business.dto.TempParam;
import com.jxm.business.model.NewsParam;
import com.jxm.business.service.NewsService;
import com.jxm.business.service.TempService;
import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/temp")
public class tempController {

    @Autowired
    private TempService tempService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<TempParam>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "type", required = false) Integer type,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<TempParam> tempParamList = tempService.list(keyword,type, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(tempParamList));
    }

    @ApiOperation("获取所有模板idName关系")
    @RequestMapping(value = "/getTempIdToName", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<TempIdToName>> getTempIdToName(){
        return  CommonResult.success(tempService.getTempIdToName());
    }

    /**
     * 添加模板内容
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addContent(@RequestBody TempParam tempParam){
        int count = tempService.addContent(tempParam);
        if(count!=0){
            return CommonResult.success();
        }else {
            return CommonResult.failed();
        }
    }

    /**
     * 修改模板内容
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@RequestBody TempParam tempParam){
        int count=tempService.update(tempParam);
        if(count!=0)
            return CommonResult.success();
        else
            return CommonResult.failed();
    }

    /**
     * 删除模板内容
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        int count=tempService.delete(id);
        if(count<0)
            return CommonResult.failed("删除培训信息错误");
        return CommonResult.success();
    }


}
