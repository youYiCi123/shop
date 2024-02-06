package com.jxm.business.service;

import com.jxm.business.dto.ProcessDto;
import com.jxm.business.model.ProcessParam;

import java.util.List;

public interface ProcessService {

    int add(ProcessDto processDto);

    int update(Long id,ProcessDto processDto);

    List<ProcessParam> getProcessByKeyword(String keyword);

    int delete(Long processId);
}
