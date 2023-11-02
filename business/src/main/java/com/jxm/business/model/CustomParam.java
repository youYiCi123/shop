package com.jxm.business.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class CustomParam {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String customName;//客户名称
    private String creditCode;//统一信用代码
    private String address;//住所
    private String legalPerson;//法人
    private Integer businessTimeType;////营业执照截至方式
    private Date businessTime;//营业执照至
    private Date licenseTime;//许可证至
    private String contactName;//联系人姓名
    private String contactPhone;//联系人电话
    @JsonSerialize(using = ToStringSerializer.class)
    private Long salesPersonId;//业务员id
    private String salesPersonName;//业务员姓名
}
