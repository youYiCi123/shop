package com.jxm.file.service.Impl;

import com.github.pagehelper.PageHelper;
import com.jxm.file.entity.Comment;
import com.jxm.file.mapper.CommentMapper;
import com.jxm.file.service.CommentService;
import com.jxm.file.vo.PageComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 博客评论业务层实现
 */
@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentMapper commentMapper;

	@Override
	public List<Comment> getListByPageAndParentCommentId(Integer page, Long blogId, Long parentCommentId) {
		List<Comment> comments = commentMapper.getListByPageAndParentCommentId(page, blogId, parentCommentId);
		for (Comment c : comments) {
			//递归查询子评论及其子评论
			List<Comment> replyComments = getListByPageAndParentCommentId(page, blogId, c.getId());
			c.setReplyComments(replyComments);
		}
		return comments;
	}

	@Override
	public List<PageComment> getPageCommentList(Integer page,Long jumpId, Long parentCommentId,String loginUserName) {
//		PageHelper.startPage(pageNum, pageSize);
		List<PageComment> comments = getPageCommentListByPageAndParentCommentId(page,jumpId, parentCommentId,loginUserName);
		for (PageComment c : comments) {
			List<PageComment> tmpComments = new ArrayList<>();
			getReplyComments(tmpComments, c.getReplyComments());
			//对于两列评论来说，按时间顺序排列应该比树形更合理些
			//排序一下
			Comparator<PageComment> comparator = Comparator.comparing(PageComment::getCreateTime);
			tmpComments.sort(comparator);

			c.setReplyComments(tmpComments);
		}
		return comments;
	}

	@Override
	public int getCommentCountByJumpId(Long articleId) {
		return commentMapper.getCommentCountByJumpId(articleId);
	}

	@Override
	public Comment getCommentById(Long id) {
		Comment comment = commentMapper.getCommentById(id);
		if (comment == null) {
			throw new RuntimeException("评论不存在");
		}
		return comment;
	}

	/**
	 * 将所有子评论递归取出到一个List中
	 *
	 * @param comments
	 */
	private void getReplyComments(List<PageComment> tmpComments, List<PageComment> comments) {
		for (PageComment c : comments) {
			tmpComments.add(c);
			getReplyComments(tmpComments, c.getReplyComments());
		}
	}

	private List<PageComment> getPageCommentListByPageAndParentCommentId(Integer page,Long jumpId, Long parentCommentId,String loginUserName) {
		List<PageComment> comments = commentMapper.getPageCommentListByPageAndParentCommentId(page,jumpId, parentCommentId);
		for (PageComment c : comments) {
			if(c.getNickname().equals(loginUserName)){
				c.setMimeComment(true);
			}else{
				c.setMimeComment(false);
			}
			List<PageComment> replyComments = getPageCommentListByPageAndParentCommentId(page,jumpId, c.getId(),loginUserName);
			c.setReplyComments(replyComments);
		}
		return comments;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateCommentPublishedById(Long commentId, Boolean published) {
		//如果是隐藏评论，则所有子评论都要修改成隐藏状态
		if (!published) {
			List<Comment> comments = getAllReplyComments(commentId);
			for (Comment c : comments) {
				hideComment(c);
			}
		}

		if (commentMapper.updateCommentPublishedById(commentId, published) != 1) {
			throw new RuntimeException("操作失败");
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateCommentNoticeById(Long commentId, Boolean notice) {
		if (commentMapper.updateCommentNoticeById(commentId, notice) != 1) {
			throw new RuntimeException("操作失败");
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int deleteCommentById(Long commentId) {
		List<Comment> comments = getAllReplyComments(commentId);
		for (Comment c : comments) {
			delete(c);
		}
		return commentMapper.deleteCommentById(commentId);

	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteCommentsByBlogId(Long blogId) {
		commentMapper.deleteCommentsByBlogId(blogId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateComment(Comment comment) {
		if (commentMapper.updateComment(comment) != 1) {
			throw new RuntimeException("评论修改失败");
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override 
	public void saveComment(com.jxm.file.dto.Comment comment) {
		if (commentMapper.saveComment(comment) != 1) {
			throw new RuntimeException("评论失败");
		}
	}

	/**
	 * 递归删除子评论
	 *
	 * @param comment 需要删除子评论的父评论
	 */
	private void delete(Comment comment) {
		for (Comment c : comment.getReplyComments()) {
			delete(c);
		}
		if (commentMapper.deleteCommentById(comment.getId()) != 1) {
			throw new RuntimeException("评论删除失败");
		}
	}

	/**
	 * 递归隐藏子评论
	 *
	 * @param comment 需要隐藏子评论的父评论
	 */
	private void hideComment(Comment comment) {
		for (Comment c : comment.getReplyComments()) {
			hideComment(c);
		}
		if (commentMapper.updateCommentPublishedById(comment.getId(), false) != 1) {
			throw new RuntimeException("操作失败");
		}
	}

	/**
	 * 按id递归查询子评论
	 *
	 * @param parentCommentId 需要查询子评论的父评论id
	 * @return
	 */
	private List<Comment> getAllReplyComments(Long parentCommentId) {
		List<Comment> comments = commentMapper.getListByParentCommentId(parentCommentId);
		for (Comment c : comments) {
			List<Comment> replyComments = getAllReplyComments(c.getId());
			c.setReplyComments(replyComments);
		}
		return comments;
	}
}
