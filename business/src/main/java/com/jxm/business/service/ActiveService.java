package com.jxm.business.service;

import com.jxm.business.dto.ActiveSubmitDto;
import com.jxm.business.model.ActiveUserParam;

import java.text.ParseException;
import java.util.List;

public interface ActiveService {

    List<ActiveUserParam> getActiveBySearch(String startDate,String endDate,String keyword);

    int submitContent(ActiveSubmitDto activeSubmitDto) throws ParseException;

    int delete(Long id);
}
