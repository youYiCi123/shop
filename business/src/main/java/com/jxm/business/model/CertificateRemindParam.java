package com.jxm.business.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class CertificateRemindParam {
    private Integer id;
    private Integer  forwardDays;
    //0全部   1邮件  2短信
    private Integer  sendType;
    private String  remindPersonEmail;
    private Date updateTime;
}
