package com.jxm.business.service.Impl;

import cn.hutool.json.JSONUtil;
import com.jxm.business.dto.SurveySubmitDto;
import com.jxm.business.dto.TempValueSubmitSingerDto;
import com.jxm.business.dto.UserDepDto;
import com.jxm.business.feign.UpstageService;
import com.jxm.business.mapper.QuUserMapper;
import com.jxm.business.mapper.SurveyUserMapper;
import com.jxm.business.model.QuUserParam;
import com.jxm.business.model.SurveyUserParam;
import com.jxm.business.service.SurveyService;
import com.jxm.common.generator.UniqueIdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private UpstageService upstageService;

    @Autowired
    private QuUserMapper quUserMapper;

    @Autowired
    private SurveyUserMapper surveyUserMapper;

    @Override
    public List<SurveyUserParam> getSurveyBySearch(String startDate, String endDate, String keyword, Long tempId) {
        return surveyUserMapper.getSurveyBySearch(startDate,endDate,keyword,tempId);
    }

    @Override
    public int submitContent(SurveySubmitDto surveySubmitDto) throws ParseException {
        UniqueIdGenerator idGenerator = new UniqueIdGenerator(1, 1);

        String jsonStr = JSONUtil.toJsonStr(upstageService.getCurrentAdmin().getData());
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);

        //如果提交过了则不提交
        SurveyUserParam tempUserParam = surveyUserMapper.selectByTempAndUser(surveySubmitDto.getTempId(), userDepDto.getUserId());
        if(tempUserParam!=null){
            return -2;
        }
        //填写用户和问卷关联信息
        long nextId = idGenerator.nextId();
        SurveyUserParam tempUserParam1 = new SurveyUserParam();
        tempUserParam1.setId(nextId);
        tempUserParam1.setTempId(surveySubmitDto.getTempId());
        tempUserParam1.setTempName(surveySubmitDto.getTempName());
        tempUserParam1.setUserId(userDepDto.getUserId());
        tempUserParam1.setUserName(userDepDto.getNickName());
        tempUserParam1.setCreateTime(new Date());
        surveyUserMapper.add(tempUserParam1);
        //填写问卷信息
        List<QuUserParam> surveyParamList = new ArrayList<>();
        for (TempValueSubmitSingerDto tempValueSubmitDto : surveySubmitDto.getTempValueSubmitSingerDtos()) {
            QuUserParam surveyParam = new QuUserParam();
            long id = idGenerator.nextId();
            BeanUtils.copyProperties(tempValueSubmitDto, surveyParam);
            String[] checkValue = tempValueSubmitDto.getCheckValue();

            if (checkValue != null) {
                String resultString = "";
                for (int i = 0; i < checkValue.length; i++) {
                    if (i < checkValue.length - 1) {
                        resultString += checkValue[i] + ",";
                    } else {
                        resultString += checkValue[i];
                    }
                }
                surveyParam.setCheckValue(resultString);
            } else {
                surveyParam.setCheckValue(null);
            }
            surveyParam.setId(id);
            surveyParam.setUserId(userDepDto.getUserId());
            surveyParam.setRelateId(nextId);
            surveyParam.setCreateTime(new Date());
            surveyParamList.add(surveyParam);
        }
        return quUserMapper.saveBatch(surveyParamList);
    }

    @Override
    public int delete(Long id) {
        surveyUserMapper.deleteById(id);
        return quUserMapper.deleteByRelateId(id);
    }
}
