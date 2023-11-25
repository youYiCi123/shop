package com.jxm.business.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActiveSubmitDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String address;
    private String[] activityTime;
    private String handlerUserId;
    private String[] depIds;
    private Long tempId;
    private String tempName;
    private Integer tempType;
    private TempValueSubmitSingerDto[] tempValueSubmitSingerDtos;
}
