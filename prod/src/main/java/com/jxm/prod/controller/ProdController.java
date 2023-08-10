package com.jxm.prod.controller;

import com.jxm.common.api.CommonResult;
import com.jxm.common.generator.UniqueIdGenerator;
import com.jxm.prod.dao.SeriesModel;
import com.jxm.prod.feign.UserFeignService;
import com.jxm.prod.service.ProdService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prod")
public class ProdController {



    @Autowired
    private ProdService prodService;

    @ApiOperation("创建所有类型")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody SeriesModel seriesModel) {

        int count = prodService.create(seriesModel);

        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
