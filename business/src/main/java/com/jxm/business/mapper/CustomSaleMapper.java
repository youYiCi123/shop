package com.jxm.business.mapper;

import com.jxm.business.model.CustomPostParam;
import com.jxm.business.model.CustomSaleParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomSaleMapper {

    int addCustomSales(CustomSaleParam customSaleParam);

    int updateCustomSale(CustomSaleParam customSaleParam);

    int delete(Long customId);

    int deleteBatchCustom(List<Long> idList);

    int saveBatch(@Param("list") List<CustomSaleParam> list);
}
