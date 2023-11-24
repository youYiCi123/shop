package com.jxm.business.model;

import lombok.Data;

import java.util.Date;

@Data
public class SurveyParam {

    private Long id;
    private Long userId;
    private String userName;
    private Long  tempId;
    private Long  quId;
    private Integer  quType;
    private Integer  rateValue;
    private Long  radioValue;
    private String  checkValue;
    private String  inputValue;
    private Date createTime;
}
