package com.jxm.business.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

@Data
public class ActiveSubmitDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String address;
    private String[] activityTime;
    private Long handlerUserId;
    private String[] depIds;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tempId;
    private String tempName;
    private Integer tempType;
    private TempValueSubmitSingerDto[] tempValueSubmitSingerDtos;
}
