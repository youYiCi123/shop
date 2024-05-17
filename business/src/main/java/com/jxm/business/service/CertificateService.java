package com.jxm.business.service;

import com.jxm.business.dto.CertificateBriefDto;
import com.jxm.business.model.CertificateParam;
import com.jxm.business.model.RemindParam;

import java.util.List;

public interface CertificateService {

    List<CertificateParam> getListBySearch(String[] lastDeclareTime, String[] firstRegisterTime, String keyword, Long category, Integer pageNum, Integer pageSize);

    CertificateParam queryContent(Long id);

    RemindParam getRemind();

    int setRemind(RemindParam remindParam);

    int add(CertificateParam certificateParam);

    int update(CertificateParam certificateParam);

    int delete(Long id);

    int deleteBatch(List<Long> idList);

    int saveCertificateBatch(List<CertificateParam> certificateParams);

    List<CertificateBriefDto> getCertificateByNearDeadline(int days);
}
