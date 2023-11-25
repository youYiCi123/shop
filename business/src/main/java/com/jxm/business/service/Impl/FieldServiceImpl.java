package com.jxm.business.service.Impl;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.jxm.business.dto.*;
import com.jxm.business.feign.UpstageService;
import com.jxm.business.mapper.QuMapper;
import com.jxm.business.mapper.QuOptionMapper;
import com.jxm.business.mapper.TempMapper;
import com.jxm.business.mapper.TempQuMapper;
import com.jxm.business.model.TempQuParam;
import com.jxm.business.service.FieldService;
import com.jxm.common.generator.UniqueIdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldServiceImpl implements FieldService {

    @Autowired
    private UpstageService upstageService;

    @Autowired
    private QuMapper quMapper;

    @Autowired
    private QuOptionMapper quOptionMapper;

    @Autowired
    private TempQuMapper tempQuMapper;

    @Autowired
    private TempMapper tempMapper;


    @Override
    public List<QuDto> list(String keyword, Integer quType, Long tempId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<QuDto> quDtos = quMapper.selectByQuery(keyword,quType,tempId);
        return quDtos;
    }

    @Override
    public FieldDetailDto queryContent(Long quId) {
        FieldDetailDto fieldDetailDto=new FieldDetailDto();
        //根据quId获取问题和类型
        QuDto infoById = quMapper.getInfoById(quId);
        fieldDetailDto.setId(quId);
        fieldDetailDto.setQuName(infoById.getQuName());
        fieldDetailDto.setQuType(infoById.getQuType());
        //根据quId获取属性值
        List<OptionDto> optionDtos=quOptionMapper.getOptionById(quId);
        fieldDetailDto.setOptions(optionDtos);
        List<Long> tempIds=tempQuMapper.getTempIdsByQuId(quId);
        // 使用Stream API将List<Long>转换为List<String>
        List<String> stringList = tempIds.stream()
                .map(Object::toString) // 将Long类型映射为String类型
                .collect(Collectors.toList()); // 将Stream转换为List
        fieldDetailDto.setTemplates(stringList);
        return fieldDetailDto;
    }

    @Override
    public int delete(Long id) {
        quMapper.deleteById(id);
        quOptionMapper.deleteByQu(id);
        return tempQuMapper.deleteByQuId(id);
    }

    @Override
    public TempQuReturnDto getTempDetailResult(Long id) {
        TempQuReturnDto tempQuDto = new TempQuReturnDto();
        TempParam tempParam=tempMapper.getTempById(id);
        tempQuDto.setTempName(tempParam.getTitle());
        tempQuDto.setTempType(tempParam.getTitleType());
        List<TempQuDetailDto> tempQuDetailDtos = tempQuMapper.listByTemp(id);
        List<TempQuDetailReturnDto> tempQuDetailReturnDtos=new ArrayList<>();
        tempQuDetailDtos.forEach(t->{
            TempQuDetailReturnDto tempQuDetailReturnDto = new TempQuDetailReturnDto();
            BeanUtils.copyProperties(t,tempQuDetailReturnDto);
            if(t.getCheckValue()!=null){
                String[] split = t.getCheckValue().split(",");
                tempQuDetailReturnDto.setCheckValue(Arrays.asList(split));
            }else{
                tempQuDetailReturnDto.setCheckValue(Arrays.asList());
            }
            tempQuDetailReturnDtos.add(tempQuDetailReturnDto);
        });
        tempQuDto.setQuList(tempQuDetailReturnDtos);
        return tempQuDto;
    }

    @Override
    public TempQuReturnDto getTempUserDetailResult(Long tempId,Long userId){
        TempQuReturnDto tempQuDto = new TempQuReturnDto();
        TempParam tempParam=tempMapper.getTempById(tempId);
        tempQuDto.setTempName(tempParam.getTitle());
        tempQuDto.setTempType(tempParam.getTitleType());
        List<TempQuDetailReturnDto> tempQuDetailReturnDtos=new ArrayList<>();
        List<TempQuDetailDto> tempQuDetailDtos = tempQuMapper.listByTempAndUser(tempId,userId);
        tempQuDetailDtos.forEach(t->{
            TempQuDetailReturnDto tempQuDetailReturnDto = new TempQuDetailReturnDto();
            BeanUtils.copyProperties(t,tempQuDetailReturnDto);
            if(t.getCheckValue()!=null){
                String[] split = t.getCheckValue().split(",");
                tempQuDetailReturnDto.setCheckValue(Arrays.asList(split));
            }else{
                tempQuDetailReturnDto.setCheckValue(Arrays.asList());
            }
            tempQuDetailReturnDtos.add(tempQuDetailReturnDto);
        });

        tempQuDto.setQuList(tempQuDetailReturnDtos);
        return tempQuDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addContent(FieldDetailDto fieldDetailDto) {
        UniqueIdGenerator idGenerator = new UniqueIdGenerator(1,1);
        long quId = idGenerator.nextId();
        QuDto quDto=new QuDto();
        BeanUtils.copyProperties(fieldDetailDto, quDto);
        quDto.setId(quId);
        quDto.setCreateTime(new Date());

        quMapper.add(quDto);

        //字段和选项绑定
        if(!CollectionUtils.isEmpty(fieldDetailDto.getOptions())){
            //最终要保存的列表
            List<OptionDto> saveList = new ArrayList<>();
            for (OptionDto option:fieldDetailDto.getOptions()) {
                option.setId(idGenerator.nextId());
                option.setQuId(quId);
                saveList.add(option);
            }
            quOptionMapper.addBatch(saveList);
        }else{
            quOptionMapper.deleteByQu(quId);
        }
        List<TempQuParam> tempQuParams = new ArrayList<>();
        //字段和模板绑定
        for (String tempId:fieldDetailDto.getTemplates()) {
            TempQuParam tempQuParam = new TempQuParam();
            tempQuParam.setId(idGenerator.nextId());
            tempQuParam.setQuId(quId);
            tempQuParam.setTempId(Long.valueOf(tempId));
            tempQuParam.setQuType(fieldDetailDto.getQuType());
            tempQuParams.add(tempQuParam);
        }
        return tempQuMapper.addBatch(tempQuParams);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateContent(FieldDetailDto fieldDetailDto) {
        UniqueIdGenerator idGenerator = new UniqueIdGenerator(1,1);
        QuDto quDto=new QuDto();
        BeanUtils.copyProperties(fieldDetailDto, quDto);
        quDto.setId(fieldDetailDto.getId());
        quDto.setUpdateTime(new Date());
        quDto.setQuName(fieldDetailDto.getQuName());
        quDto.setQuType(fieldDetailDto.getQuType());
        quMapper.updateByQuId(quDto);
        //先删除后添加
        quOptionMapper.deleteByQu(fieldDetailDto.getId());
        if(!CollectionUtils.isEmpty(fieldDetailDto.getOptions())) {
            //最终要保存的列表
            List<OptionDto> saveList = new ArrayList<>();
            for (OptionDto option : fieldDetailDto.getOptions()) {
                option.setId(idGenerator.nextId());
                option.setQuId(fieldDetailDto.getId());
                saveList.add(option);
            }
            quOptionMapper.addBatch(saveList);
        }
        tempQuMapper.deleteByQuId(fieldDetailDto.getId());
        List<TempQuParam> tempQuParams = new ArrayList<>();
        //字段和模板绑定
        for (String tempId:fieldDetailDto.getTemplates()) {
            TempQuParam tempQuParam = new TempQuParam();
            tempQuParam.setId(idGenerator.nextId());
            tempQuParam.setQuId(fieldDetailDto.getId());
            tempQuParam.setTempId(Long.valueOf(tempId));
            tempQuParam.setQuType(fieldDetailDto.getQuType());
            tempQuParams.add(tempQuParam);
        }
        return tempQuMapper.addBatch(tempQuParams);
        }
}
