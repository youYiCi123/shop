package com.jxm.business.service;

import com.jxm.business.dto.ProcessConditionDetail;

import java.util.List;

public interface ProcessConditionService {
    List<ProcessConditionDetail> getAllList(Long processId);
}
