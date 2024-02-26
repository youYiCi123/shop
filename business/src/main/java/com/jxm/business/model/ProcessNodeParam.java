package com.jxm.business.model;

import lombok.Data;

@Data
public class ProcessNodeParam {
    private Long id;
    private String nodeName;
    private Integer type;
    private String priorityLevel;
    private String setType;
    private String selectMode;
    private String selectRange;
    private String examineMode;
    private String ccSelfSelectFlag;
    private Long parentId;
    private Long processId;
}
