package com.jxm.business.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

public class CustomSalesParam implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String customName;//客户名称
    private String creditCode;//统一信用代码
    private String address;//住所
    private String legalPerson;//法人
    private Integer businessTimeType;//营业执照截至方式
    private Date businessTime;//营业执照至
    private Date licenseTime;//许可证至
    private String contactName;//联系人姓名
    private String contactPhone;//联系人电话
    @JsonSerialize(using = ToStringSerializer.class)
    private Long salesPersonId;//业务员id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public Integer getBusinessTimeType() {
        return businessTimeType;
    }

    public void setBusinessTimeType(Integer businessTimeType) {
        this.businessTimeType = businessTimeType;
    }

    public Date getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(Date businessTime) {
        this.businessTime = businessTime;
    }

    public Date getLicenseTime() {
        return licenseTime;
    }

    public void setLicenseTime(Date licenseTime) {
        this.licenseTime = licenseTime;
    }

    public Long getSalesPersonId() {
        return salesPersonId;
    }

    public void setSalesPersonId(Long salesPersonId) {
        this.salesPersonId = salesPersonId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @Override
    public String toString() {
        return "CustomSalesParam{" +
                "id=" + id +
                ", customName='" + customName + '\'' +
                ", creditCode='" + creditCode + '\'' +
                ", address='" + address + '\'' +
                ", legalPerson='" + legalPerson + '\'' +
                ", businessTimeType=" + businessTimeType +
                ", businessTime='" + businessTime + '\'' +
                ", licenseTime=" + licenseTime +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", salesPersonId=" + salesPersonId +
                '}';
    }
}
