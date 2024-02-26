package com.jxm.business.mapper;

import com.jxm.business.dto.ProcessDto;
import com.jxm.business.model.ProcessBriefDto;
import com.jxm.business.model.ProcessParam;

import java.util.List;

public interface ProcessMapper {

    ProcessBriefDto getById(Long id);

    int add(ProcessDto processDto);

    int update(ProcessDto processDto);

    List<ProcessParam> getProcessByKeyword(String keyword);

    int delete(Long processId);
}
