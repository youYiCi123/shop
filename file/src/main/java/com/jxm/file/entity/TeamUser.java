package com.jxm.file.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class TeamUser {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long teamId;
    private String notice;
    private Date updateTime;
}
