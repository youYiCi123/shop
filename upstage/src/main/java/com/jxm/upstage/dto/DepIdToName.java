package com.jxm.upstage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class DepIdToName {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String depName;
}
