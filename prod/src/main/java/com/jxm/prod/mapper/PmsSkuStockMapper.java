package com.jxm.prod.mapper;


import com.jxm.prod.model.PmsSkuStock;

import java.util.List;

public interface PmsSkuStockMapper {

    List<PmsSkuStock> selectByPidAndKeyWord(Long pid, String keyword);

    int updateBySkuStock(PmsSkuStock record);

    int deleteInSkuIds(List<Long> ids);

    int deleteByProductId(Long id);

    List<PmsSkuStock> selectByProductId(Long id);

}