package com.jxm.business.mapper;

import com.jxm.business.dto.TempIdToName;
import com.jxm.business.dto.TempParam;

import java.util.List;

public interface TempMapper {

    List<TempParam> selectByQuery(String keyword, Integer type);

    List<TempParam> getAllActive(String keyword);

    Long getLatestId();

    List<TempIdToName> getSurveyIdToName();

    List<TempIdToName> getActiveIdToName();

    List<TempIdToName> getTempIdToName();

    int add(TempParam tempParam);

    int delete(Long tempId);

    int update(TempParam tempParam);

    TempParam getTempById(Long tempId);
}
