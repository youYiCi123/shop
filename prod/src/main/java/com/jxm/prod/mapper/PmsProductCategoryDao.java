package com.jxm.prod.mapper;

import com.jxm.prod.dto.PmsProductCategoryWithChildrenItem;

import java.util.List;

public interface PmsProductCategoryDao {

    /**
     * 获取商品分类及其子分类
     */
    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}
