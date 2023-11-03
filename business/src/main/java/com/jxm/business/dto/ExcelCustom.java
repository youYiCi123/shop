package com.jxm.business.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@ExcelIgnoreUnannotated
@Data
public class ExcelCustom {
    @ExcelProperty(value = "客户名称",index = 0)
    private String customName;
    @ExcelProperty(value = "营业执照至",index = 1)
    private String businessTime;
    @ExcelProperty(value = "许可证至",index = 2)
    private String licenseTime;
    @ExcelProperty(value = "统一信用代码",index = 3)
    private String creditCode;
    @ExcelProperty(value = "业务员",index = 4)
    private String salesPersonName;
    @ExcelProperty(value = "地址",index = 5)
    private String address;
    @ExcelProperty(value = "电话",index = 6)
    private String contactPhone;
    @ExcelProperty(value = "联系人",index = 7)
    private String contactName;
    @ExcelProperty(value = "法人",index = 8)
    private String legalPerson;
}
