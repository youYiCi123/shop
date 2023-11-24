package com.jxm.business.dto;

import com.jxm.business.model.TempQuParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TempQuDetailDto extends TempQuParam {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题目内容", required=true)
    private String content;

    @ApiModelProperty(value = "答案内容", required=true)
    List<OptionDto> answerList;
}
