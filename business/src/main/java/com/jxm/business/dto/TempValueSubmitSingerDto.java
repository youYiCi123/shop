package com.jxm.business.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TempValueSubmitSingerDto {

    private Long quId;

    private Integer quType;

    private Integer rateValue;

    private Long radioValue;

    private String[] checkValue;

    private String inputValue;

}
