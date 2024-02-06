package com.jxm.business.service.Impl;

import com.jxm.business.dto.ProcessDto;
import com.jxm.business.mapper.ProcessMapper;
import com.jxm.business.model.ProcessParam;
import com.jxm.business.service.ProcessService;
import com.jxm.common.generator.UniqueIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private ProcessMapper processMapper;

    @Override
    public int add(ProcessDto processDto) {
        UniqueIdGenerator idGenerator = new UniqueIdGenerator(1,1);
        long id = idGenerator.nextId();
        processDto.setId(id);
        processDto.setHandTime(new Date());
        return processMapper.add(processDto);
    }

    @Override
    public int update(Long id, ProcessDto processDto) {
        processDto.setId(id);
        processDto.setHandTime(new Date());
        return processMapper.update(processDto);
    }

    @Override
    public List<ProcessParam> getProcessByKeyword(String keyword) {
        return processMapper.getProcessByKeyword(keyword);
    }

    @Override
    public int delete(Long processId) {
        return processMapper.delete(processId);
//        if (count > 0) {
//            return newsContentMapper.delete(trainId);
//        }
//        return -1;
    }
}
