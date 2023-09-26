package com.jxm.file.dto;


import lombok.Data;

@Data
public class DashboardUserFileParam {
    private Integer fileType;
    private Integer userId;
    private String userName;
}
