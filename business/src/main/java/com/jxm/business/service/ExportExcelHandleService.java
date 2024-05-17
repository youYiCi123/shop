package com.jxm.business.service;


import com.jxm.business.dto.ExcelCertificate;
import com.jxm.business.dto.ExcelCustom;
import com.jxm.business.dto.ExcelSupplier;

import java.util.List;

public interface ExportExcelHandleService {

    void saveCustomInfo(List<ExcelCustom> customModels);

    void saveCertificateInfo(List<ExcelCertificate> certificateModels);

    void saveSupplierInfo(List<ExcelSupplier> supplierModels);
}
