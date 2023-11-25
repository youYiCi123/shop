package com.jxm.business.mapper;

import com.jxm.business.model.TempUserParam;


public interface TempUserMapper {

    int add(TempUserParam tempUserParam);

    TempUserParam selectByTempAndUser(Long tempId,Long userId);
}
