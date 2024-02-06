package com.jxm.business.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProcessDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String processName;

    private Date handTime;
    //创建人
    private Long handUserId;
    //创建人
    private String handUserName;
}
