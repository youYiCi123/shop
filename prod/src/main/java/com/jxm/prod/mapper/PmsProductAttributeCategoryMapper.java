package com.jxm.prod.mapper;

import com.jxm.prod.model.PmsProductAttributeCategory;

import java.util.List;

public interface PmsProductAttributeCategoryMapper {

    int insert(PmsProductAttributeCategory record);

    int updateByPrimaryKey(PmsProductAttributeCategory record);

    PmsProductAttributeCategory selectByPrimaryKey(Long id);

    int update(PmsProductAttributeCategory record);

    int deleteById(Long id);

    List<PmsProductAttributeCategory> selectAll();

}