package com.jxm.file.mapper;

import com.jxm.file.entity.Comment;
import com.jxm.file.vo.PageComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {
	List<Comment> getListByPageAndParentCommentId(Integer page, Long blogId, Long parentCommentId);

	List<Comment> getListByParentCommentId(Long parentCommentId);

	List<PageComment> getPageCommentListByPageAndParentCommentId(@Param("page") Integer page,@Param("jumpId") Long jumpId,@Param("parentCommentId") Long parentCommentId);

	Comment getCommentById(Long id);

	int getCommentCountByJumpId(Long articleId);

	int updateCommentPublishedById(Long commentId, Boolean published);

	int updateCommentNoticeById(Long commentId, Boolean notice);

	int deleteCommentById(Long commentId);

	int deleteCommentsByBlogId(Long blogId);

	int updateComment(Comment comment);

	int countComment();

	int saveComment(com.jxm.file.dto.Comment comment);
}
