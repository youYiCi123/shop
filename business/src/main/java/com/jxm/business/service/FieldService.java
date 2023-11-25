package com.jxm.business.service;

import com.jxm.business.dto.FieldDetailDto;
import com.jxm.business.dto.QuDto;
import com.jxm.business.dto.TempQuDto;
import com.jxm.business.dto.TempQuReturnDto;

import java.text.ParseException;
import java.util.List;

public interface FieldService {

    List<QuDto> list(String keyword, Integer quType, Long tempId, Integer  pageSize, Integer  pageNum);

    int addContent(FieldDetailDto fieldDetailDto);

    int updateContent(FieldDetailDto fieldDetailDto);

    FieldDetailDto queryContent(Long id);

    int delete(Long id);

    TempQuReturnDto getTempDetailResult(Long id);

    TempQuReturnDto getTempUserDetailResult(Long tempId,Long userId);
}
