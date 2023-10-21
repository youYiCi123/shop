package com.jxm.file.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Description: 评论DTO
 * @Author: Naccl
 * @Date: 2020-08-27
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Comment {
	private Long id;
	private String nickname;//昵称
	private String content;//评论内容
	private String avatar;//头像(图片路径)
	private Date createTime;//评论时间
	private Boolean published;//公开或隐藏
	private Boolean adminComment;//博主回复
	private Integer page;//0文件页评论，1公文页面评论
	private Long parentCommentId;//父评论id
	private Long jumpId;//所属的文件id或公文id
}
