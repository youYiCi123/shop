package com.jxm.business.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@ExcelIgnoreUnannotated
@Data
public class ExcelCertificate {
    @ExcelProperty(value = "名称",index = 0)
    private String certificateName;
    @ExcelProperty(value = "规格",index = 1)
    private String norms;
    @ExcelProperty(value = "类别",index = 2)
    private String category;
    @ExcelProperty(value = "注册证号",index = 3)
    private String registerNumber;
    @ExcelProperty(value = "生产许可证号",index = 4)
    private String prodLicenseNumber;
    @ExcelProperty(value = "核准日期",index = 5)
    private String approvalTime;
    @ExcelProperty(value = "有效期",index = 6)
    private String effectiveTime;
    @ExcelProperty(value = "下次申报日期",index = 7)
    private String lastDeclareTime;
    @ExcelProperty(value = "风险再评价",index = 8)
    private String riskEvaluateTime;
    @ExcelProperty(value = "首次注册时间",index = 9)
    private String firstRegisterTime;
}
