package com.jxm.business.mapper;

import com.jxm.business.dto.OptionDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuOptionMapper {

    List<OptionDto> getOptionById(Long quId);

    int addBatch(@Param("list") List<OptionDto> list);

    int deleteByQu(Long quId);
}
