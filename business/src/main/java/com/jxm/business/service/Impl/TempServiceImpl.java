package com.jxm.business.service.Impl;

import com.github.pagehelper.PageHelper;
import com.jxm.business.dto.TempIdToName;
import com.jxm.business.dto.TempParam;
import com.jxm.business.mapper.TempMapper;
import com.jxm.business.mapper.SurveyUserMapper;
import com.jxm.business.service.TempService;
import com.jxm.common.generator.UniqueIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TempServiceImpl implements TempService {

    @Autowired
    private TempMapper tempMapper;

    @Autowired
    private SurveyUserMapper surveyUserMapper;

    @Override
    public List<TempParam> list(String keyword, Integer type, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<TempParam> tempParams = tempMapper.selectByQuery(keyword,type);
        return tempParams;
    }

    @Override
    public List<TempParam> getAllActive(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<TempParam> tempParams = tempMapper.getAllActive(keyword);
        return tempParams;
    }

    @Override
    public List<TempIdToName> getSurveyIdToName() {
        return tempMapper.getSurveyIdToName();
    }

    @Override
    public List<TempIdToName> getActiveIdToName() {
        return tempMapper.getActiveIdToName();
    }

    @Override
    public List<TempIdToName> getTempIdToName() {
        return tempMapper.getTempIdToName();
    }

    @Override
    public int addContent(TempParam tempParam) {
        UniqueIdGenerator idGenerator = new UniqueIdGenerator(1,1);
        long id = idGenerator.nextId();
        tempParam.setId(id);
        tempParam.setCreateTime(new Date());
        return tempMapper.add(tempParam);
    }

    @Override
    public int delete(Long tempId) {
        return tempMapper.delete(tempId);
    }

    @Override
    public int update(TempParam tempParam) {
        //更改tempUser表信息
        surveyUserMapper.setTempNameAndType(tempParam.getId(),tempParam.getTitle(),tempParam.getTitleType());
        return tempMapper.update(tempParam);
    }
}
