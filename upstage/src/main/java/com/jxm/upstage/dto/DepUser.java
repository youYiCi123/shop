package com.jxm.upstage.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class DepUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "姓名")
    private String nickName;

    @ApiModelProperty(value = "账号")
    private String username;

    @ApiModelProperty(value = "职务")
    private String duty;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    @Override
    public String toString() {
        return "DepUser{" +
                "nickName='" + nickName + '\'' +
                ", username='" + username + '\'' +
                ", duty='" + duty + '\'' +
                '}';
    }
}
