package com.jxm.business.mapper;

import com.jxm.business.dto.ProcessConditionDetail;

import java.util.List;

public interface ProcessConditionDetailMapper {

    List<ProcessConditionDetail> getAllList(Long processId);

}
