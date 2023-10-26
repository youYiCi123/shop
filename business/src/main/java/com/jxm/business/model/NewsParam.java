package com.jxm.business.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class NewsParam {
    private Long id;
    private String theme;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long newsType;
    private String content;
}
