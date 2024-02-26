package com.jxm.business.mapper;

import com.jxm.business.dto.ProcessNodeUser;

import java.util.List;

public interface ProcessNodeUserMapper {

    //查找子节点
    List<ProcessNodeUser> getListById(Long id);
}
