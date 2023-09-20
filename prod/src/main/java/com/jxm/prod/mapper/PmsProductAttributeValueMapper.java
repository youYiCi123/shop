package com.jxm.prod.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsProductAttributeValueMapper {

    int deleteByProductId(Long prodId);
}