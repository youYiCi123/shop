package com.jxm.business.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessBriefDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String processName;
}
