package com.jxm.business.service.Impl;

import cn.hutool.json.JSONUtil;
import com.jxm.business.dto.DashboardFileTypeParam;
import com.jxm.business.dto.TempValueSubmitSingerDto;
import com.jxm.business.dto.UserDepDto;
import com.jxm.business.feign.UpstageService;
import com.jxm.business.mapper.SurveyMapper;
import com.jxm.business.model.SurveyParam;
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
    private SurveyMapper surveyMapper;

    @Override
    public int submitContent(TempValueSubmitSingerDto[] tempValueSubmitDtos) throws ParseException {
        UniqueIdGenerator idGenerator = new UniqueIdGenerator(1, 1);

        String jsonStr = JSONUtil.toJsonStr(upstageService.getCurrentAdmin().getData());
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);

        //如果提交过了则不提交
        SurveyParam inspectSurvey = surveyMapper.inspectSurvey(tempValueSubmitDtos[0].getTempId(),userDepDto.getUserId());
        if(inspectSurvey!=null){
            return -2;
        }
        List<SurveyParam> surveyParamList = new ArrayList<>();
        for (TempValueSubmitSingerDto tempValueSubmitDto : tempValueSubmitDtos) {
            SurveyParam surveyParam = new SurveyParam();
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
            surveyParam.setUserName(userDepDto.getNickName());
            surveyParam.setCreateTime(new Date());
            surveyParamList.add(surveyParam);
        }
        return surveyMapper.saveBatch(surveyParamList);
    }
}
