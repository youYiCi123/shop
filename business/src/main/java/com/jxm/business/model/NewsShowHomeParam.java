package com.jxm.business.model;

import lombok.Data;

/**
 * 首页展示内容
 */
@Data
public class NewsShowHomeParam {
    private Long id;
    private String theme;
    private String createdTime;
    private Integer newsType;
}
