package com.jxm.business.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProcessParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String processName;

    //创建日期
    private Date createdTime;
    //创建人
    private Long createdUserId;
    //创建人
    private String createdUserName;

    //修改日期
    private Date updateTime;
    //修改人
    private Long updateUserId;
    //修改人
    private String updateUserName;
}
