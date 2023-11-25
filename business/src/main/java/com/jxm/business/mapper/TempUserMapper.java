package com.jxm.business.mapper;

import com.jxm.business.model.TempUserParam;

import java.util.List;


public interface TempUserMapper {

    int setTempNameAndType(Long tempId,String tempName,Integer tempType);

    List<TempUserParam> getSurveyBySearch(String startDate, String endDate, String keyword, Long tempId);

    int add(TempUserParam tempUserParam);

    TempUserParam selectByTempAndUser(Long tempId,Long userId);
}
