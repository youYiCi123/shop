package com.jxm.business.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@ExcelIgnoreUnannotated
@Data
public class ExcelSupplier {
    @ExcelProperty(value = "名称",index = 0)
    private String supplierName;
    @ExcelProperty(value = "组织机构代码",index = 1)
    private String organizeStructureCode;
    @ExcelProperty(value = "营业执照至",index = 2)
    private String businessTime;
    @ExcelProperty(value = "许可证/备案编号",index = 3)
    private String recordNumber;
    @ExcelProperty(value = "许可证至",index = 4)
    private String licenseTime;
    @ExcelProperty(value = "产品",index = 5)
    private String product;
    @ExcelProperty(value = "质保协议有效期至",index = 6)
    private String qaAgreementTime;
    @ExcelProperty(value = "经营授权有效期至",index = 7)
    private String businessAuthTime;
    @ExcelProperty(value = "法人授权有效期至",index = 8)
    private String legalPersonAuthTime;
    @ExcelProperty(value = "其它",index = 9)
    private String other;
}
