package com.jxm.prod.service;

import com.jxm.prod.dto.PmsProductAttributeParam;
import com.jxm.prod.dto.ProductAttrInfo;
import com.jxm.prod.model.PmsProductAttribute;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品属性Service
 * Created by macro on 2018/4/26.
 */
public interface PmsProductAttributeService {

    /**
     * 添加商品属性
     */
    @Transactional
    int create(PmsProductAttributeParam pmsProductAttributeParam);

    /**
     * 修改商品属性
     */
    int update(Long id, PmsProductAttributeParam productAttributeParam);

    /**
     * 获取单个商品属性信息
     */
    PmsProductAttribute getItem(Long id);

    @Transactional
    int delete(List<Long> ids);

    /**
     * 根据分类分页获取商品属性
     * @param cid 分类id
     * @param type 0->属性；2->参数
     */
    List<PmsProductAttribute> getList(Long cid, Integer type, Integer pageSize, Integer pageNum);

    List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId);
}
