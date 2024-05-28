package com.jxm.file.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private Integer id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long reminderId;
    private Date createDate;
    private String whoName;
    private String fileName;
    private Integer messageType;
}
