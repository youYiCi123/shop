package com.jxm.prod.mapper;

import com.jxm.prod.model.UmsMemberLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsMemberLevelMapper {

    List<UmsMemberLevel> selectByDefaultStatus(Integer defaultStatus);

}