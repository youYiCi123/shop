package com.jxm.business.service.Impl;

import com.jxm.business.dto.ProcessConditionDetail;
import com.jxm.business.mapper.ProcessConditionDetailMapper;
import com.jxm.business.service.ProcessConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessConditionServiceImpl implements ProcessConditionService {

    @Autowired
    private ProcessConditionDetailMapper processConditionDetailMapper;
    @Override
    public List<ProcessConditionDetail> getAllList(Long processId) {
        return processConditionDetailMapper.getAllList(processId);
    }
}
