package com.jxm.business.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TempQuDetailReturnDto {
    private static final long serialVersionUID = 1L;

    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long quId;//题目id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tempId;//模板id
    private Integer quType;//题目类型

    private Integer rateValue;//评分值
    @JsonSerialize(using = ToStringSerializer.class)
    private Long radioValue;//单选值
    private List<String> checkValue;//复选值
    private String inputValue;//输入值

    @ApiModelProperty(value = "题目内容", required=true)
    private String content;

    @ApiModelProperty(value = "答案内容", required=true)
    List<OptionDto> answerList;
}
