package com.jxm.business.mapper;

import com.jxm.business.dto.CustomSalesParam;
import com.jxm.business.model.CustomParam;
import com.jxm.business.model.CustomPostParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomMapper {

    List<CustomParam> getCustomBySearch(String keyword,Long salesPersonId);

    List<CustomParam> getCustomByNearDeadline();

    CustomParam queryCustomContent(Long id);

    int addCustom(CustomPostParam customPostParam);

    int updateCustom(CustomPostParam customPostParam);

    int delete(Long customId);

    int deleteBatchCustom(List<Long> idList);

    List<String> getAllCustom();

    int saveBatch(@Param("list") List<CustomPostParam> list);
}
