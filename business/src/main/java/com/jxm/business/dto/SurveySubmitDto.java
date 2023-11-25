package com.jxm.business.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SurveySubmitDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tempId;

    private String tempName;

    private Integer tempType;

    private TempValueSubmitSingerDto[] tempValueSubmitSingerDtos;
}
