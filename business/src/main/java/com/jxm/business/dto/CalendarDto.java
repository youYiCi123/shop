package com.jxm.business.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CalendarDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String title;
    private String start;
    private String end;
    private String className;
}
