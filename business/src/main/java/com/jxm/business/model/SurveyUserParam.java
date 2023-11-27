package com.jxm.business.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class SurveyUserParam {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long  id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long  userId;
    private String  userName;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long  tempId;
    private String  tempName;
    private Date createTime;
}
