package com.jxm.business.service;

import com.jxm.business.dto.CertificateBriefDto;
import com.jxm.business.dto.CustomSalesParam;
import com.jxm.business.model.CertificateParam;
import com.jxm.business.model.CertificateRemindParam;
import com.jxm.business.model.CustomParam;
import com.jxm.business.model.CustomPostParam;

import java.util.List;

public interface CertificateService {

    List<CertificateParam> getListBySearch(String[] lastDeclareTime, String[] firstRegisterTime, String keyword, Long category, Integer pageNum, Integer pageSize);

    CertificateParam queryContent(Long id);

    CertificateRemindParam getRemind();

    int setRemind(CertificateRemindParam certificateRemindParam);

    int add(CertificateParam certificateParam);

    int update(CertificateParam certificateParam);

    int delete(Long id);

    int deleteBatch(List<Long> idList);

    int saveCertificateBatch(List<CertificateParam> certificateParams);

    List<CertificateBriefDto> getCertificateByNearDeadline(int days);
}
