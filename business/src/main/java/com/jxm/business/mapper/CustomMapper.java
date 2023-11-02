package com.jxm.business.mapper;

import com.jxm.business.dto.CustomSalesParam;
import com.jxm.business.model.CustomParam;
import com.jxm.business.model.CustomPostParam;

import java.util.List;

public interface CustomMapper {

    List<CustomParam> getCustomBySearch(String keyword,Long salesPersonId);

    CustomParam queryCustomContent(Long id);

    int addCustom(CustomPostParam customPostParam);

    int updateCustom(CustomPostParam customPostParam);

    int delete(Long customId);

    int deleteBatchCustom(List<Long> idList);
}
