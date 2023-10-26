package com.jxm.business.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NewsHomeDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    //新闻主题
    private String theme;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long newsType;
    //创建日期
    private Date createdTime;
    //查看次数
    private Integer viewCount;
    //新闻内容
    private String content;
}
