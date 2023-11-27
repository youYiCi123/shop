package com.jxm.business.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class ActiveUserParam {
    @JsonSerialize(using = ToStringSerializer.class)
    private  Long  id;
    @JsonSerialize(using = ToStringSerializer.class)
    private  Long  userId;
    private  String  userName;
    private  String  activeName;
    private  String  activeAddress;
    private  Date  startTime;
    private  Date  endTime;
    @JsonSerialize(using = ToStringSerializer.class)
    private  Long  handlerUserId;
    private  String  depIds;
    @JsonSerialize(using = ToStringSerializer.class)
    private  Long  tempId;
    private String tempName;
    private Date createTime;
}
