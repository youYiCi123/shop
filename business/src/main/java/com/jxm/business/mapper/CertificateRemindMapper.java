package com.jxm.business.mapper;

import com.jxm.business.dto.CertificateBriefDto;
import com.jxm.business.model.CertificateRemindParam;

import java.util.List;

public interface CertificateRemindMapper {

    CertificateRemindParam query();

    int add(CertificateRemindParam certificateRemindParam);

    int update(CertificateRemindParam certificateRemindParam);

    List<CertificateBriefDto> getCertificateByNearDeadline(int days);
}
