package com.jxm.business.mapper;


import com.jxm.business.dto.ProcessCondition;

import java.util.List;

public interface ProcessConditionMapper {
    //查找子节点
    List<ProcessCondition> getListById(Long id);
}
