package com.jxm.business.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TempQuReturnDto {

    private String name;
    private String address;
    private List<String> activityTime;
    private String handlerUserId;
    private List<String> depIds;

    @ApiModelProperty(value = "模板名称", required=true)
    private String tempName;

    @ApiModelProperty(value = "模板类型", required=true)
    private Integer tempType;

    @ApiModelProperty(value = "问题列表", required=true)
    private List<TempQuDetailReturnDto> quList;
}
