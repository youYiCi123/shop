package com.jxm.business.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUploadCountDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userName;
    private Integer uploadCount;
}
