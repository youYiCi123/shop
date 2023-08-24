package com.jxm.prod.service;


import com.jxm.prod.model.PmsProductAttributeCategory;

import java.util.List;

public interface PmsProductAttributeCategoryService {

    /**
     * 创建属性分类
     */
    int create(String name);

    /**
     * 修改属性分类
     */
    int update(Long id, String name);

    /**
     * 删除属性分类
     */
    int delete(Long id);
    /**
     * 分页查询属性分类
     */
    List<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum);
}
