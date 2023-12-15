package com.jxm.business.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CalendarUserParam {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long calendarId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    private String userName;
    private String userPhone;
    private String userEmail;
}
