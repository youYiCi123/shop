package com.jxm.business.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class TempIdToName {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String title;
}
