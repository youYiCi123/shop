package com.jxm.prod.mapper;

import com.jxm.prod.model.PmsProductAttribute;
import com.jxm.prod.model.PmsProductAttributeCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsProductAttributeMapper {

    int insertSelective(PmsProductAttribute record);

    int updateByPrimaryKeySelective(PmsProductAttribute record);

    PmsProductAttribute selectByPrimaryKey(Long id);

    int deleteByIds(@Param("ids") List<Long> ids);

    List<PmsProductAttribute> selectByCidAndType(@Param("cid") Long cid,@Param("type") Integer type);

}