package com.jxm.business.model;

import lombok.Data;

@Data
public class ProcessNodeUserParam {
    private Long nodeId;
    private Long value;//用户id
    private String Label;//用户名
}
