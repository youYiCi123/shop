package com.jxm.business.mapper;

import com.jxm.business.dto.ProcessDto;
import com.jxm.business.model.ProcessParam;

import java.util.List;

public interface ProcessMapper {

    int add(ProcessDto processDto);

    int update(ProcessDto processDto);

    List<ProcessParam> getProcessByKeyword(String keyword);

    int delete(Long processId);
}
