package com.jxm.business.service;

import com.jxm.business.dto.TempValueSubmitSingerDto;

import java.text.ParseException;

public interface SurveyService {

    int submitContent(TempValueSubmitSingerDto[] tempValueSubmitDto) throws ParseException;
}
