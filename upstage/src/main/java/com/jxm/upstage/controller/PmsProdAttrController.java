package com.jxm.upstage.controller;

import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import com.jxm.upstage.dto.PmsProductAttributeParam;
import com.jxm.upstage.feign.PmsProdAttributeService;
import com.jxm.upstage.model.PmsProductAttribute;
import com.jxm.upstage.model.PmsProductAttributeCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productAttribute")
public class PmsProdAttrController {

    @Autowired
    private PmsProdAttributeService pmsProdAttributeService;

    @RequestMapping(value = "/category/list", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<CommonPage<PmsProductAttributeCategory>> getList(@RequestParam(defaultValue = "5") Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum){
        return pmsProdAttributeService.getList(pageSize,pageNum);
    }

    @RequestMapping(value = "/category/create", method = RequestMethod.POST)
    @ResponseBody
    CommonResult create(@RequestParam String name){
        return pmsProdAttributeService.create(name);
    }

    @RequestMapping(value = "/category/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    CommonResult update(@PathVariable Long id, @RequestParam String name){
        return pmsProdAttributeService.update(id,name);
    }

    @RequestMapping(value = "/category/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    CommonResult delete(@PathVariable Long id){
        return pmsProdAttributeService.delete(id);
    }

    @RequestMapping(value = "/list/{cid}", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<CommonPage<PmsProductAttribute>> getList(@PathVariable Long cid,
                                                          @RequestParam(value = "type") Integer type,
                                                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
       return pmsProdAttributeService.getList(cid,type,pageSize,pageNum);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    CommonResult create(@RequestBody PmsProductAttributeParam productAttributeParam){
        return pmsProdAttributeService.create(productAttributeParam);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    CommonResult update(@PathVariable Long id, @RequestBody PmsProductAttributeParam productAttributeParam){
        return pmsProdAttributeService.update(id,productAttributeParam);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<PmsProductAttribute> getItem(@PathVariable Long id){
        return pmsProdAttributeService.getItem(id);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    CommonResult delete(@RequestParam("ids") List<Long> ids){
        return pmsProdAttributeService.delete(ids);
    }
}
