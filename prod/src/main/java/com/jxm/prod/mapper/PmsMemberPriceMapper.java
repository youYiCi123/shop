package com.jxm.prod.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsMemberPriceMapper {

    int deleteByProductId(Long id);
}