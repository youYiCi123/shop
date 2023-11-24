package com.jxm.business.mapper;

import com.jxm.business.dto.TempQuDetailDto;
import com.jxm.business.model.TempQuParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TempQuMapper {

    int deleteByQuId(Long quId);

    int addBatch(@Param("list") List<TempQuParam> list);

    List<Long> getTempIdsByQuId(Long quId);

    List<TempQuDetailDto> listByTemp(@Param("tempId") Long tempId);
}
