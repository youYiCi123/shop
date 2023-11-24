package com.jxm.business.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TempValueSubmitSingerDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long tempId;

    private Long quId;

    private Integer quType;

    private Integer rateValue;

    private Long radioValue;

    private String[] checkValue;

    private String inputValue;

}
