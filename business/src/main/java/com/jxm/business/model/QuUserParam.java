package com.jxm.business.model;

import lombok.Data;

import java.util.Date;

@Data
public class QuUserParam {

    private Long id;
    private Long userId;
    private Long relateId;
    private Long  quId;
    private Integer  quType;
    private Integer  rateValue;
    private Long  radioValue;
    private String  checkValue;
    private String  inputValue;
    private Date createTime;
}
