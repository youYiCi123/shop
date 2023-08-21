package com.jxm.upstage.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleGroupCount implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long roleId;

    private Long count;
}
