package com.jxm.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 文件基础设置PO
 */
@ApiModel(value = "文件基础设置PO")
public class BasicSetFilePO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件记录ID
     */
    @ApiModelProperty(value = "文件记录ID", required = true)
    @NotNull(message = "文件id不能为空")
    private Long fileId;

    /**
     * 水印标识位
     */
    @ApiModelProperty(value = "水印标识位", required = true)
    @NotNull(message = "添加水印标识位不为空")
    private Integer isWaterMater;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Integer getIsWaterMater() {
        return isWaterMater;
    }

    public void setIsWaterMater(Integer isWaterMater) {
        this.isWaterMater = isWaterMater;
    }

    @Override
    public String toString() {
        return "basicSetFilePO{" +
                "fileId=" + fileId +
                ", isWaterMater=" + isWaterMater +
                '}';
    }
}
