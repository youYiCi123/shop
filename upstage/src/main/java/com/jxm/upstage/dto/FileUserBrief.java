package com.jxm.upstage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class FileUserBrief implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 用户根目录ID
     */
    @ApiModelProperty("用户根目录ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long rootFileId;

    /**
     * 用户根目录文件名称
     */
    @ApiModelProperty("用户根目录文件名称")
    private String rootFilename;
}
