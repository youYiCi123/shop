package com.jxm.business.service;

import com.jxm.business.dto.FieldDetailDto;
import com.jxm.business.dto.QuDto;
import com.jxm.business.dto.TempQuDto;

import java.util.List;

public interface FieldService {

    List<QuDto> list(String keyword, Integer quType, Long tempId, Integer  pageSize, Integer  pageNum);

    int addContent(FieldDetailDto fieldDetailDto);

    int updateContent(FieldDetailDto fieldDetailDto);

    FieldDetailDto queryContent(Long id);

    int delete(Long id);

    TempQuDto getTempDetailResult(Long id);
}
