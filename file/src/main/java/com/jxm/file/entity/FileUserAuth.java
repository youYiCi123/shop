package com.jxm.file.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class FileUserAuth {
    private Integer id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fileId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    private String userName;
    private Integer fileManageFlag;
    private Integer moveFileFlag;
    private Integer uploadFileFlag;
}
