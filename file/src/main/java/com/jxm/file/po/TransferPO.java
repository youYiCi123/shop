package com.jxm.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 转移文件PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "转移文件PO")
public class TransferPO implements Serializable {

    private static final long serialVersionUID = 5547494462940457810L;

    /**
     * 要转移的文件ID，多个用__,__隔开
     */
    @ApiModelProperty(value = "转移的文件ID，多个用__,__隔开", required = true)
    @NotBlank(message = "请选择要转移的文件")
    private String fileIds;

    /**
     * 要转移到的文件夹ID
     */
    @ApiModelProperty(value = "要转移到的文件夹ID", required = true)
    @NotNull(message = "请选择要转移到的文件夹")
    private Long targetParentId;

    @ApiModelProperty(value = "顶级文件夹id", required = true)
    @NotNull(message = "顶级文件夹id")
    private Long topFileId;

    public TransferPO() {
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

    public Long getTopFileId() {
        return topFileId;
    }

    public void setTopFileId(Long topFileId) {
        this.topFileId = topFileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferPO that = (TransferPO) o;
        return Objects.equals(fileIds, that.fileIds) &&
                Objects.equals(targetParentId, that.targetParentId) &&
                Objects.equals(topFileId, that.topFileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileIds, targetParentId, topFileId);
    }

    @Override
    public String toString() {
        return "TransferPO{" +
                "fileIds='" + fileIds + '\'' +
                ", targetParentId=" + targetParentId +
                ", topFileId=" + topFileId +
                '}';
    }
}
