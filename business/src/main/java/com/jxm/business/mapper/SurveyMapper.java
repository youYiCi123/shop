package com.jxm.business.mapper;

import com.jxm.business.model.SurveyParam;

import java.util.List;

public interface SurveyMapper {

    int saveBatch(List<SurveyParam> list);

}
