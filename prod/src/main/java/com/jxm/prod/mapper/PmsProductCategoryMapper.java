package com.jxm.prod.mapper;

import com.jxm.prod.model.PmsProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsProductCategoryMapper {

    int insertSelective(PmsProductCategory record);

    List<PmsProductCategory> selectByParentId(Long parentId);

    int updateByExampleSelective(@Param("record") PmsProductCategory record,@Param("ids") List<Long> ids);

    int deleteById(Long id);

    PmsProductCategory selectById(Long id);

    int updateByPrimaryKeySelective(PmsProductCategory record);
}
