package com.jxm.business.mapper;

import com.jxm.business.dto.CertificateBriefDto;
import com.jxm.business.dto.SupplierBriefDto;
import com.jxm.business.model.RemindParam;

import java.util.List;

public interface RemindMapper {

    RemindParam queryCertificate();

    RemindParam querySupplier();

    int add(RemindParam remindParam);

    int update(RemindParam remindParam);

    List<CertificateBriefDto> getCertificateByNearDeadline(int days);

    List<SupplierBriefDto> getSupplierByNearDeadline(int days);
}
