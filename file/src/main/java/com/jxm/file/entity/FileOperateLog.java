package com.jxm.file.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class FileOperateLog {
    private Long id;
    private Long userId;
    private String operate;
    private Long fileId;
    private String fileName;
    private String extendField;
    private String reason;
    private Date createTime;
}
