package com.jxm.business.mapper;

import com.jxm.business.model.ProcessNodeParam;

import java.util.List;

public interface ProcessNodeMapper {

    //查找根节点
    ProcessNodeParam getByIdAndNull(Long id);
    //查找子节点
    List<ProcessNodeParam> getByIdAndNotNull(Long id);

}
