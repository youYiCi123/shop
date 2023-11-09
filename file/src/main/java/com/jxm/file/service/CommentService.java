package com.jxm.file.service;

import com.jxm.file.entity.Comment;
import com.jxm.file.vo.PageComment;

import java.util.List;

public interface CommentService {
	List<Comment> getListByPageAndParentCommentId(Integer page, Long blogId, Long parentCommentId);

	List<PageComment> getPageCommentList(Integer page,Long articleId, Long parentCommentId,String loginUserName);

	int getCommentCountByJumpId(Long articleId);

	Comment getCommentById(Long id);

	void updateCommentPublishedById(Long commentId, Boolean published);

	void updateCommentNoticeById(Long commentId, Boolean notice);

	int deleteCommentById(Long commentId);

	void deleteCommentsByBlogId(Long blogId);

	void updateComment(Comment comment);

	void saveComment(com.jxm.file.dto.Comment comment);
}
