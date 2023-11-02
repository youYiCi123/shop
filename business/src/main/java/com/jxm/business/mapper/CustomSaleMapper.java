package com.jxm.business.mapper;

import com.jxm.business.model.CustomSaleParam;

import java.util.List;

public interface CustomSaleMapper {

    int addCustomSales(CustomSaleParam customSaleParam);

    int updateCustomSale(CustomSaleParam customSaleParam);

    int delete(Long customId);

    int deleteBatchCustom(List<Long> idList);
}
