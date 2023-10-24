package com.jxm.file.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 用户文件信息实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class RPanUserFile implements Serializable {

    private static final long serialVersionUID = 781030562993386681L;

    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 物理文件id
     */
    private Long realFileId;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 是否通过审核
     */
    private Integer passFlag;

    /**
     * 文件夹标识 0 否 1 是
     */
    private Integer folderFlag;

    /**
     * 添加水印标识 0 否 1 是
     */
    private Integer waterMaterFlag;

    /**
     * 文件大小显示文案
     */
    private String fileSizeDesc;

    /**
     * 文件类型（1 文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件）
     */
    private Integer fileType;

    /**
     * 删除标识（0 否 1 是）
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    private Long depId;

    /**
     * 审核人id
     */
    private Long passUserId;

    /**
     * 审核人姓名
     */
    private String passUserName;

    /**
     * 审核时间
     */
    private Date passTime;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getRealFileId() {
        return realFileId;
    }

    public void setRealFileId(Long realFileId) {
        this.realFileId = realFileId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getPassFlag() {
        return passFlag;
    }

    public void setPassFlag(Integer passFlag) {
        this.passFlag = passFlag;
    }

    public Integer getFolderFlag() {
        return folderFlag;
    }

    public void setFolderFlag(Integer folderFlag) {
        this.folderFlag = folderFlag;
    }

    public Integer getWaterMaterFlag() {
        return waterMaterFlag;
    }

    public void setWaterMaterFlag(Integer waterMaterFlag) {
        this.waterMaterFlag = waterMaterFlag;
    }

    public String getFileSizeDesc() {
        return fileSizeDesc;
    }

    public void setFileSizeDesc(String fileSizeDesc) {
        this.fileSizeDesc = fileSizeDesc;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getDepId() {
        return depId;
    }

    public void setDepId(Long depId) {
        this.depId = depId;
    }

    public Long getPassUserId() {
        return passUserId;
    }

    public void setPassUserId(Long passUserId) {
        this.passUserId = passUserId;
    }

    public String getPassUserName() {
        return passUserName;
    }

    public void setPassUserName(String passUserName) {
        this.passUserName = passUserName;
    }

    public Date getPassTime() {
        return passTime;
    }

    public void setPassTime(Date passTime) {
        this.passTime = passTime;
    }

    @Override
    public String toString() {
        return "RPanUserFile{" +
                "fileId=" + fileId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", parentId=" + parentId +
                ", realFileId=" + realFileId +
                ", filename='" + filename + '\'' +
                ", passFlag=" + passFlag +
                ", folderFlag=" + folderFlag +
                ", waterMaterFlag=" + waterMaterFlag +
                ", fileSizeDesc='" + fileSizeDesc + '\'' +
                ", fileType=" + fileType +
                ", delFlag=" + delFlag +
                ", createUser=" + createUser +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                ", updateTime=" + updateTime +
                ", depId=" + depId +
                ", passUserId=" + passUserId +
                ", passUserName='" + passUserName + '\'' +
                ", passTime=" + passTime +
                '}';
    }
}