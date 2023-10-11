package com.jxm.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 复制文件PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "复制文件PO")
public class CopyPO implements Serializable {

    private static final long serialVersionUID = 1647689436377006254L;
//      private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页面类型", required = true)
    private Integer pageType;
    /**
     * 要复制的文件ID，多个用__,__隔开
     */
    @ApiModelProperty(value = "要复制的文件ID，多个用__,__隔开", required = true)
    @NotBlank(message = "请选择要复制的文件")
    private String fileIds;

    /**
     * 要复制到的文件夹ID
     */
    @ApiModelProperty(value = "要复制到的文件夹ID", required = true)
    @NotNull(message = "请选择要复制到的文件夹")
    private Long targetParentId;

    public CopyPO() {
    }

    public String getFileIds() {
        return fileIds;
    }

    public void setFileIds(String fileIds) {
        this.fileIds = fileIds;
    }

    public Long getTargetParentId() {
        return targetParentId;
    }

    public void setTargetParentId(Long targetParentId) {
        this.targetParentId = targetParentId;
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
        CopyPO copyPO = (CopyPO) o;
        return Objects.equals(pageType, copyPO.pageType) &&
                Objects.equals(fileIds, copyPO.fileIds) &&
                Objects.equals(targetParentId, copyPO.targetParentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageType, fileIds, targetParentId);
    }

    @Override
    public String toString() {
        return "CopyPO{" +
                "pageType=" + pageType +
                ", fileIds='" + fileIds + '\'' +
                ", targetParentId=" + targetParentId +
                '}';
    }
}
