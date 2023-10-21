package com.jxm.file.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 评论管理页面按博客title查询评论
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ArticleIdAndTitle {
	private Long id;
	private String title;
}
