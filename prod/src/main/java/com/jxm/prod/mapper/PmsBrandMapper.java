package com.jxm.prod.mapper;

import com.jxm.prod.model.PmsBrand;
import com.jxm.prod.model.PmsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsBrandMapper {

    List<PmsBrand> selectAll();

    int insertBrand(PmsBrand record);

    int update(PmsBrand record);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);

    List<PmsBrand> selectByName(String keyword);

    PmsBrand selectById(Long id);

    int updateByExampleSelective(@Param("record") PmsBrand record, @Param("ids") List<Long> ids);
}
