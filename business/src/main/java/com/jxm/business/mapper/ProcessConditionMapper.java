package com.jxm.business.mapper;


import com.jxm.business.dto.ProcessCondition;
import com.jxm.business.dto.ProcessNodeUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessConditionMapper {

    int deleteByNodeId(Long nodeId);
    //批量插入
    int batchInsert(@Param("nodeId")Long nodeId, @Param("processConditions") List<ProcessCondition> processConditions);
    //查找子节点
    List<ProcessCondition> getListById(Long id);
}
