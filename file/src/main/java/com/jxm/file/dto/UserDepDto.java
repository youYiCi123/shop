package com.jxm.file.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class UserDepDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long depId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    private String nickName;
}
