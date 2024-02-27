package com.jxm.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class ProcessNodeConfig {
    private Long id;
    private String nodeName;
    private Integer type;
    private String priorityLevel;
    private String setType;
    private String selectMode;
    private String selectRange;
    private String examineMode;
    private String ccSelfSelectFlag;
    private List<ProcessCondition> conditionList;
    private List<ProcessNodeUser> nodeUserList;
    private ProcessNodeConfig childNode;
    private List<ProcessNodeConfig> conditionNodes;

    @JsonIgnore
    public ProcessNodeConfig getDeepestChildNode() {
        if (this.childNode == null) {
            return this;
        } else {
            return this.childNode.getDeepestChildNode();
        }
    }

}
