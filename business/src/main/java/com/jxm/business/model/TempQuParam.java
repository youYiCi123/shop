package com.jxm.business.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class TempQuParam {
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long quId;//题目id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tempId;//模板id
    private Integer quType;//题目类型
}
