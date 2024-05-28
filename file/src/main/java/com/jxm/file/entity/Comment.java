package com.jxm.file.entity;

import com.jxm.file.vo.ArticleIdAndTitle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  文件评论
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Comment {
	private Long id;
	private Long userId;//用户id
	private String nickname;//昵称
	private String content;//评论内容
	private String avatar;//头像(图片路径)
	private Date createTime;//评论时间
	private Boolean published;//公开或隐藏
	private Boolean adminComment;//博主回复
	private Integer page;//0文件页评论，1公文页面评论
	private Long parentCommentId;//父评论id

	private ArticleIdAndTitle article;//所属的文章或文件
	private List<Comment> replyComments = new ArrayList<>();//回复该评论的评论
}
