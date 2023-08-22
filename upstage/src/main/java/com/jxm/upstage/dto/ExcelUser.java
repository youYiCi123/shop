package com.jxm.upstage.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@ExcelIgnoreUnannotated
@Data
public class ExcelUser {
    @ExcelProperty(value = "姓名",index = 0)
    private String nickName;
    @ExcelProperty(value = "性别",index = 1)
    private String sex;
    @ExcelProperty(value = "邮箱",index = 2)
    private String email;
    @ExcelProperty(value = "手机号",index = 3)
    private String phoneNumber;
    @ExcelProperty(value = "职务",index = 4)
    private String duty;
}
