package com.jxm.business.service;

import com.jxm.business.dto.TempIdToName;
import com.jxm.business.dto.TempParam;

import java.util.List;

public interface TempService {

    List<TempParam> list(String keyword, Integer type, Integer pageSize, Integer pageNum);

    List<TempParam> getAllActive(String keyword, Integer pageSize, Integer pageNum);

    List<TempIdToName> getSurveyIdToName();

    List<TempIdToName> getActiveIdToName();

    List<TempIdToName> getTempIdToName();

    int addContent(TempParam tempParam);

    int delete(Long tempId);

    int update(TempParam tempParam);
}
