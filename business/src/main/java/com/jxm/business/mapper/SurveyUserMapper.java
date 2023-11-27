package com.jxm.business.mapper;

import com.jxm.business.model.SurveyUserParam;

import java.util.List;


public interface SurveyUserMapper {

    int setTempNameAndType(Long tempId,String tempName,Integer tempType);

    List<SurveyUserParam> getSurveyBySearch(String startDate, String endDate, String keyword, Long tempId);

    int add(SurveyUserParam tempUserParam);

    SurveyUserParam selectByTempAndUser(Long tempId, Long userId);
}
