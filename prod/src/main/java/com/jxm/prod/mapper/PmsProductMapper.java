package com.jxm.prod.mapper;

import com.jxm.prod.model.PmsProduct;
import org.apache.ibatis.annotations.Param;

public interface PmsProductMapper {

    int updateByProductCategoryId(@Param("record") PmsProduct record, @Param("id") Long id);

    int updateByBrandId(@Param("record") PmsProduct record, @Param("id") Long id);
}
