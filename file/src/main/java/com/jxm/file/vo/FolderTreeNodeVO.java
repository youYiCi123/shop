package com.jxm.file.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.collect.Lists;
import com.jxm.file.entity.RPanUserFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 文件夹信息返回实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "文件夹信息返回实体")
public class FolderTreeNodeVO implements Serializable {

    private static final long serialVersionUID = 3942552174021452303L;

    /**
     * 节点显示标题
     */
    @ApiModelProperty("节点显示标题")
    private String label;

    /**
     * 唯一标识
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("唯一标识")
    private Long id;

    /**
     * 子节点
     */
    @ApiModelProperty("子节点")
    private List<FolderTreeNodeVO> children;

    /**
     * 父文件夹ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("父文件夹ID")
    private Long parentId;

    @ApiModelProperty("文件夹类型")
    private Integer folderType;

    /**
     * 拼装树节点
     *
     * @param rPanUserFile
     * @return
     */
    public static FolderTreeNodeVO assembleFolderTreeNode(RPanUserFile rPanUserFile) {
        FolderTreeNodeVO folderTreeNode = new FolderTreeNodeVO();
        folderTreeNode.setLabel(rPanUserFile.getFilename());
        folderTreeNode.setId(rPanUserFile.getFileId());
        folderTreeNode.setFolderType(rPanUserFile.getFolderType());
        folderTreeNode.setChildren(Lists.newArrayList());
        folderTreeNode.setParentId(rPanUserFile.getParentId());
        return folderTreeNode;
    }

    public FolderTreeNodeVO() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<FolderTreeNodeVO> getChildren() {
        return children;
    }

    public void setChildren(List<FolderTreeNodeVO> children) {
        this.children = children;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getFolderType() {
        return folderType;
    }

    public void setFolderType(Integer folderType) {
        this.folderType = folderType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FolderTreeNodeVO that = (FolderTreeNodeVO) o;
        return Objects.equals(label, that.label) &&
                Objects.equals(id, that.id) &&
                Objects.equals(children, that.children) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(folderType, that.folderType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, id, children, parentId, folderType);
    }

    @Override
    public String toString() {
        return "FolderTreeNodeVO{" +
                "label='" + label + '\'' +
                ", id=" + id +
                ", children=" + children +
                ", parentId=" + parentId +
                ", folderType=" + folderType +
                '}';
    }
}
