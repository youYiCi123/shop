package com.jxm.business.mapper;

import com.jxm.business.model.ActiveUserParam;

import java.util.List;

public interface ActiveUserMapper {

    int deleteById(Long id);

    ActiveUserParam getActiveById(Long id);

    List<ActiveUserParam> getActiveBySearch(String startDate, String endDate, String keyword);

    int add(ActiveUserParam activeUserParam);

}
