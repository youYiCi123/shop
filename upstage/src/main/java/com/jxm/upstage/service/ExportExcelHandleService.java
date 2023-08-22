package com.jxm.upstage.service;


import com.jxm.upstage.dto.ExcelUser;

import java.util.List;

public interface ExportExcelHandleService {

    void saveUserInfo(List<ExcelUser> subExportExcelModels);
}
