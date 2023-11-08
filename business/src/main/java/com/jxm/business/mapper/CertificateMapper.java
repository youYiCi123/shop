package com.jxm.business.mapper;

import com.jxm.business.model.CertificateParam;
import com.jxm.business.model.CustomParam;
import com.jxm.business.model.CustomSaleParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CertificateMapper {

    List<CertificateParam> getListBySearch(String lastDeclareStartDate,String lastDeclareEndDate,String firstRegisterStartDate,String firstRegisterEndDate, String keyword, Long category);

    CertificateParam queryContent(Long id);

    int add(CertificateParam certificateParam);

    int update(CertificateParam certificateParam);

    int delete(Long id);

    int deleteBatch(List<Long> idList);

    int saveBatch(@Param("list") List<CertificateParam> list);

}
