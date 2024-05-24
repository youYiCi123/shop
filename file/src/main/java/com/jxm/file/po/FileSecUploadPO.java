package com.jxm.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(value = "文件秒传PO")
public class FileSecUploadPO implements Serializable {

    private static final long serialVersionUID = 1883102378277804464L;
    //da
    @ApiModelProperty(value = "文件名称", required = true)
    @NotBlank(message = "文件名称不能为空")
    private String filename;

    @ApiModelProperty(value = "文件对应的唯一标识", required = true)
    @NotBlank(message = "文件对应的唯一标识不能为空")
    private String identifier;

    @ApiModelProperty(value = "父id", required = true)
    @NotNull(message = "父id不能为空")
    private Long parentId;

    @ApiModelProperty(value = "页面类型", required = true)
    @NotNull(message = "所在页面类型")
    private Integer pageType;

    public FileSecUploadPO() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileSecUploadPO that = (FileSecUploadPO) o;
        return Objects.equals(filename, that.filename) &&
                Objects.equals(identifier, that.identifier) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(pageType, that.pageType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, identifier, parentId, pageType);
    }

    @Override
    public String toString() {
        return "FileSecUploadPO{" +
                "filename='" + filename + '\'' +
                ", identifier='" + identifier + '\'' +
                ", parentId=" + parentId +
                ", pageType=" + pageType +
                '}';
    }
}
