package com.jxm.upstage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class DepParam {
    @NotEmpty
    @ApiModelProperty(value = "部门名")
    private String depName;

    @ApiModelProperty(value = "部门经理")
    private String leadName;

    @ApiModelProperty(value = "经理电话")
    private String leadPhone;

    @ApiModelProperty(value = "经理邮箱")
    private String leadEmail;

    @ApiModelProperty(value = "经理性别")
    private Integer leadSex;

    @ApiModelProperty(value = "备注")
    private String note;
}
