package com.jxm.business.mapper;

import com.jxm.business.dto.QuDto;

import java.util.List;

public interface QuMapper {

    int deleteById(Long id);

    int updateByQuId(QuDto quDto);

    QuDto getInfoById(Long quId);

    List<QuDto> selectByQuery(String keyword,Integer quType,Long tempId);

    int add(QuDto quDto);
}
