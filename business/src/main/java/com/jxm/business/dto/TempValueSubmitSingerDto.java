package com.jxm.business.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

@Data
public class TempValueSubmitSingerDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long quId;

    private Integer quType;

    private Integer rateValue;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long radioValue;

    private String[] checkValue;

    private String inputValue;

}
