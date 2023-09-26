package com.jxm.upstage.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 用户文件信息实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
public class UserFile implements Serializable {

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
     * 文件夹标识 0 否 1 是
     */
    private Integer folderFlag;

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

    private String depId;
}