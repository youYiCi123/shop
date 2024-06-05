package com.jxm.file.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MyUploadFileStatus {
    private Long fileId;
    private String fileName;
    private Date createTime;
    private Integer status;//状态 0 待审核  1 审核通过  2 审核未通过/已删除
}
