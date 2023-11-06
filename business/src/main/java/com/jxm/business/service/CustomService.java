package com.jxm.business.service;

import com.jxm.business.dto.CustomSalesParam;
import com.jxm.business.model.CustomParam;
import com.jxm.business.model.CustomPostParam;
import com.jxm.business.model.CustomSaleParam;
import com.jxm.business.model.NewsParam;

import java.util.List;

public interface CustomService {

    List<CustomParam> getCustomBySearch(Integer pageNum, Integer pageSize, String keyword,Long salesPersonId);

    List<CustomParam> getCustomByNearDeadline();

    CustomParam queryCustomContent(Long id);

    int addCustom(CustomSalesParam customSalesParam);

    int updateCustom(CustomSalesParam customSalesParam);

    int delete(Long customId);

    int deleteBatchCustom(List<Long> idList);

    List<String> getAllCustom();

    int saveCustomBatch(List<CustomPostParam> customPostParamList);

    int saveCustomSaleBatch(List<CustomSaleParam> customSaleParams);
}
