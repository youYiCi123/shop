package com.jxm.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(value = "文件分片合并PO")
public class FileChunkMergePO implements Serializable {

    private static final long serialVersionUID = 5594399779771184806L;

    @ApiModelProperty(value = "文件名称", required = true)
    @NotBlank(message = "文件名称不能为空")
    private String filename;

    @ApiModelProperty(value = "文件对应的唯一标识", required = true)
    @NotBlank(message = "文件对应的唯一标识不能为空")
    private String identifier;

    @ApiModelProperty(value = "文件大小", required = true)
    @NotNull(message = "文件大小不能为空")
    private Long totalSize;

    @ApiModelProperty(value = "父id", required = true)
    @NotNull(message = "父id不能为空")
    private Long parentId;

    @ApiModelProperty(value = "页面类型", required = true)
    @NotNull(message = "所在页面类型")
    private Integer pageType;

    @ApiModelProperty(value = "是否为团队文件", required = true)
    @NotNull(message = "团队文件标识")
    private String teamFlag;

    @ApiModelProperty(value = "是否添加水印", required = true)
    @NotNull(message = "水印标识")
    private String waterMarkFlag;

    public FileChunkMergePO() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getPageType() {
        return pageType;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
    }

    public String getWaterMarkFlag() {
        return waterMarkFlag;
    }

    public void setWaterMarkFlag(String waterMarkFlag) {
        this.waterMarkFlag = waterMarkFlag;
    }

    public String getTeamFlag() {
        return teamFlag;
    }

    public void setTeamFlag(String teamFlag) {
        this.teamFlag = teamFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileChunkMergePO that = (FileChunkMergePO) o;
        return Objects.equals(filename, that.filename) &&
                Objects.equals(identifier, that.identifier) &&
                Objects.equals(totalSize, that.totalSize) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(pageType, that.pageType) &&
                Objects.equals(teamFlag, that.teamFlag) &&
                Objects.equals(waterMarkFlag, that.waterMarkFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, identifier, totalSize, parentId, pageType, teamFlag, waterMarkFlag);
    }

    @Override
    public String toString() {
        return "FileChunkMergePO{" +
                "filename='" + filename + '\'' +
                ", identifier='" + identifier + '\'' +
                ", totalSize=" + totalSize +
                ", parentId=" + parentId +
                ", pageType=" + pageType +
                ", teamFlag='" + teamFlag + '\'' +
                ", waterMarkFlag='" + waterMarkFlag + '\'' +
                '}';
    }
}
