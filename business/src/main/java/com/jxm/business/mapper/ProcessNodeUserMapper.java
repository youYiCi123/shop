package com.jxm.business.mapper;

import com.jxm.business.dto.ProcessNodeUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessNodeUserMapper {

    //批量插入
    int batchInsert(@Param("nodeId")Long nodeId,@Param("nodeUserList") List<ProcessNodeUser> nodeUserList);
    //查找子节点
    List<ProcessNodeUser> getListById(Long id);
}
