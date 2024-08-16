package com.jxm.file.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class MyUploadFileStatus {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fileId;
    private String fileName;
    private Date createTime;
    private Integer status;//状态 0 待审核  1 审核通过  2 审核未通过/已删除
    private String reason;
}
