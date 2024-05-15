package com.jxm.business.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProcessConditionDetail {
    private Integer columnId;
    private Integer showType;
    private String showName;
    private String columnType;
    private List<ProcessConditionChild> child;
}
