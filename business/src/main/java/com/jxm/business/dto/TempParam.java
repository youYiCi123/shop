package com.jxm.business.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TempParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "模板名称", required=true)
    private String title;

    @ApiModelProperty(value = "模板类型", required=true)
    private Integer titleType;

    @ApiModelProperty(value = "模板备注", required=true)
    private String remark;

    @ApiModelProperty(value = "创建日期")
    private Date createTime;

}
