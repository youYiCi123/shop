package com.jxm.prod.mapper;

import com.jxm.prod.dto.PmsProductQueryParam;
import com.jxm.prod.model.PmsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsProductMapper {

    List<PmsProduct> selectByQueryParam(@Param("productQueryParam") PmsProductQueryParam productQueryParam);

    int updateByPrimaryId(PmsProduct record);

    int insertProduct(PmsProduct record);

    int updateByProductCategoryId(@Param("record") PmsProduct record, @Param("id") Long id);

    int updateByBrandId(@Param("record") PmsProduct record, @Param("id") Long id);

    int updateIdIn(@Param("record") PmsProduct record, @Param("ids") List<Long> ids);
}
