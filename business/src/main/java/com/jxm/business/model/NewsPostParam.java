package com.jxm.business.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NewsPostParam implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //新闻主题
    private String theme;
    //新闻类型
    @JsonSerialize(using = ToStringSerializer.class)
    private Long newsType;
    //创建日期
    private Date createdTime;
    //创建人
    private Long createdAdminId;
    //创建人
    private String createdAdminName;
    //修改日期
    private Date updateTime;
}