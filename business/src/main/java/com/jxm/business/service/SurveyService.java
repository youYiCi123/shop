package com.jxm.business.service;

import com.jxm.business.dto.SurveySubmitDto;
import com.jxm.business.dto.TempValueSubmitSingerDto;
import com.jxm.business.model.TempUserParam;

import java.text.ParseException;
import java.util.List;

public interface SurveyService {

    List<TempUserParam> getSurveyBySearch(String startDate,String endDate,String keyword,Long tempId);

    int submitContent(SurveySubmitDto surveySubmitDto) throws ParseException;
}
