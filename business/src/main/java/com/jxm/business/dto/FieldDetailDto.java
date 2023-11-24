package com.jxm.business.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FieldDetailDto  extends QuDto{

    @ApiModelProperty(value = "模板列表", required=true)
    private List<String> templates;

    @ApiModelProperty(value = "题库列表", required=true)
    private List<OptionDto> options;
}
