package com.jxm.upstage.controller;

import com.jxm.common.api.CommonResult;
import com.jxm.upstage.dao.SeriesModel;
import com.jxm.upstage.service.ProdSeriesService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RefreshScope  //@Value无法动态感知配置文件内容修改
public class UserController {

    @Autowired
    private ProdSeriesService prodSeriesService;

    @ApiOperation("获取所有类型")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SeriesModel>> listAll() {
        List<SeriesModel> seriesList = prodSeriesService.listAll();
        return CommonResult.success(seriesList);
    }

    @ApiOperation("创建所有类型")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody SeriesModel seriesModel) {
        int count = prodSeriesService.create(seriesModel);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
