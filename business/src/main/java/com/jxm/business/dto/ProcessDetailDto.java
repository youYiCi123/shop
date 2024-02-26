package com.jxm.business.dto;

import com.jxm.business.model.ProcessBriefDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProcessDetailDto extends ProcessBriefDto {
    private ProcessNodeConfig nodeConfig;
}
