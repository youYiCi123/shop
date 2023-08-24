package com.jxm.upstage.feign;

import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import com.jxm.upstage.dto.PmsProductAttributeParam;
import com.jxm.upstage.model.PmsProductAttribute;
import com.jxm.upstage.model.PmsProductAttributeCategory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@FeignClient(name = "prod-service",url = "/productAttribute")
@FeignClient("prod-service")
public interface PmsProdAttributeService {

    /**
     * 根据分类查询属性列表或参数列表
     */
    @GetMapping("/productAttribute/list/{cid}")
    CommonResult<CommonPage<PmsProductAttribute>> getList(@PathVariable Long cid,
                                                          @RequestParam(value = "type") Integer type,
                                                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum);

    /**
     * 添加商品属性信息
     */
    @PostMapping("/productAttribute/create")
    CommonResult create(@RequestBody PmsProductAttributeParam productAttributeParam);

    /**
     * 修改商品属性信息
     */
    @PostMapping("/productAttribute/update/{id}")
    CommonResult update(@PathVariable Long id, @RequestBody PmsProductAttributeParam productAttributeParam);

    /**
     * 查询单个商品属性
     */
    @GetMapping("/productAttribute/{id}")
    CommonResult<PmsProductAttribute> getItem(@PathVariable Long id);

    /**
     * 批量删除商品属性
     */
    @PostMapping("/productAttribute/delete")
    CommonResult delete(@RequestParam("ids") List<Long> ids);

    /**
     * 分页获取所有商品属性分类
     */
    @GetMapping("/productAttribute/category/list")
    CommonResult<CommonPage<PmsProductAttributeCategory>> getList(@RequestParam(defaultValue = "5") Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum);

    /**
     * 添加商品属性分类
     */
    @PostMapping("/productAttribute/category/create")
    CommonResult create(@RequestParam String name);

    /**
     *修改商品属性分类
     */
    @PostMapping("/productAttribute/category/update/{id}")
    CommonResult update(@PathVariable Long id, @RequestParam String name);

    /**
     * 删除单个商品属性分类
     */
    @GetMapping("/productAttribute/category/delete/{id}")
    CommonResult delete(@PathVariable Long id);
}
