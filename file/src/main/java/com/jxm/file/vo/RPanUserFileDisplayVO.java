package com.jxm.file.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.jxm.file.util.Date2StringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RPanUserFileDisplayVO implements Serializable {

    private static final long serialVersionUID = -8132138657557677897L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("主键")
    private Long fileId;

    /**
     * 父id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("父id")
    private Long parentId;

    /**
     * 父文件夹名称
     */
    @ApiModelProperty("父文件夹名称")
    private String parentFilename;


    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String filename;

    /**
     * 创建人
     */
    private String creatName;

    /**
     * 审核人
     */
    private String passUserName;

    /**
     * 文件夹标识 0 否 1 是
     */
    @ApiModelProperty("文件夹标识 0 否 1 是")
    private Integer folderFlag;

    @ApiModelProperty("下载时添加水印，0不需要，1需要")
    private Integer waterMaterFlag;
    /**
     * 文件大小
     */
    @ApiModelProperty("文件大小")
    private String fileSizeDesc;

    /**
     * 文件类型 文件类型（1 文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件）
     */
    @ApiModelProperty("文件类型 文件类型（1 文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件）")
    private Integer fileType;

    /**
     * 创建时间
     */
    @JsonSerialize(using = Date2StringSerializer.class)
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 审核时间
     */
    @JsonSerialize(using = Date2StringSerializer.class)
    @ApiModelProperty("审核时间")
    private Date passTime;

    /**
     * 更新时间
     */
    @JsonSerialize(using = Date2StringSerializer.class)
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
