package com.jxm.file.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class FileOperateLogDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private String userName;
    private String operate;
    private String extendField;
    private Long fileId;
    private String fileName;
    private Date createTime;
}
