package com.jxm.business.dto;

import lombok.Data;

@Data
public class ProcessCondition {
    private Integer id;
    private Integer type;
    private String conditionEn;
    private String conditionCn;
    private String optType;
    private String zdy1;
    private String zdy2;
    private String opt1;
    private String opt2;
    private Integer columnId;
    private String columnDbname;
    private String columnType;
    private String showType;
    private String showName;
    private String fixedDownBoxValue;
}
