package com.jxm.business.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class OptionDto {

    /**
     * 答案id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     *问题id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long quId;

    /**
     * 选项内容
     */
    private String content;
}
