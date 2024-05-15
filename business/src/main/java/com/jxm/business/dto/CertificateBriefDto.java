package com.jxm.business.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CertificateBriefDto {
    private String certificateName;//证书名称
    private Date lastDeclareTime;//下次申报日期
}
