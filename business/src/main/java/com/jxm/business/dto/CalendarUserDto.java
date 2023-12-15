package com.jxm.business.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class CalendarUserDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String title;
    private Date startDate;
    private Date endDate;
    private String level;
    private Integer isRemind;//是否提醒
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
}
