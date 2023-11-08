package com.jxm.business.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class CertificateParam {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String certificateName;//证书名称
    private String norms;//规格
    private Integer category;//证书类别（1 I类；2 II类；3 III类 ）
    private String registerNumber;//注册证号
    private Date approvalTime;//核准日期
    private Date effectiveTime;//有效日期
    private Date lastDeclareTime;//下次申报日期
    private Date riskEvaluateTime;//风险再评价
    private Date firstRegisterTime;//首次注册时间
}
