package com.jxm.business.model;

import lombok.Data;

import java.util.Date;

@Data
public class SupplierParam {
    private Long id;
    //名称
    private String supplierName;
    //组织机构代码
    private String organizeStructureCode;
    //营业执照至
    private Date businessTime;
    //许可证/备案编号
    private String recordNumber;
    //许可证至
    private Date licenseTime;
    //产品
    private String product;
    //质保协议有效期至
    private Date qaAgreementTime;
    //经营授权有效期至
    private Date businessAuthTime;
    //法人授权有效期至
    private Date legalPersonAuthTime;
    //其它
    private String other;
}
