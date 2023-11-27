package com.jxm.business.mapper;

import com.jxm.business.model.QuUserParam;

import java.util.List;

public interface QuUserMapper {

    int deleteByRelateId(Long relateId);

    int saveBatch(List<QuUserParam> list);

}
