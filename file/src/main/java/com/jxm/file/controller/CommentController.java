package com.jxm.file.controller;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import com.jxm.file.dto.Comment;
import com.jxm.file.dto.UserDepDto;
import com.jxm.file.feign.UpstageService;
import com.jxm.file.service.CommentService;
import com.jxm.file.service.IUserFileService;
import com.jxm.file.vo.PageComment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论
 */
@Controller
@Api(tags = "CommentController", description = "评论管理")
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UpstageService upstageService;
    @Autowired
    @Qualifier(value = "userFileService")
    private IUserFileService iUserFileService;

    /**
     * 根据页面分页查询评论列表
     */
    @RequestMapping(value = "/getComments", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<PageComment>> getComments(@RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "") Long jumpId,
                                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                                          @RequestParam(defaultValue = "10") Integer pageSize) throws ParseException {
        String jsonStr = JSONUtil.toJsonStr(upstageService.getCurrentAdmin().getData());
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        List<PageComment> pageCommentList = commentService.getPageCommentList(page,jumpId, -1L, userDepDto.getNickName(),pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(pageCommentList));
    }

    @ApiOperation("提交评论")
    @PostMapping("/postComment")
    @ResponseBody
    public CommonResult postComment(@RequestBody Comment comment) throws ParseException {
        comment.setPage(0);
        //评论内容合法性校验
        if (StringUtils.isEmpty(comment.getContent()) || comment.getContent().length() > 250 ||
                comment.getPage() == null || comment.getParentCommentId() == null) {
            return CommonResult.failed("参数有误");
        }
        //父评论
        com.jxm.file.entity.Comment parentComment = null;
        //对于有指定父评论的评论，应该以父评论为准，只判断页面可能会被绕过“评论开启状态检测”
        if (comment.getParentCommentId() != -1) {
            //当前评论为子评论
            parentComment = commentService.getCommentById(comment.getParentCommentId());
            Integer page = parentComment.getPage();
            Long articleId = page == 0 ? parentComment.getArticle().getId() : null;
            comment.setPage(page);
            comment.setJumpId(articleId);
        } else {
            //当前评论为顶级评论
            if (comment.getPage() != 0) {
                comment.setJumpId(null);
            }
        }
        String jsonStr = JSONUtil.toJsonStr(upstageService.getCurrentAdmin().getData());
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        //设置评论者信息
        Long authorId = iUserFileService.getUserByFileId(comment.getJumpId());
        if (userDepDto.getUserId().equals(authorId)) {
            comment.setAdminComment(true);
        } else {
            comment.setAdminComment(false);
        }
        comment.setPublished(false);
        comment.setNickname(userDepDto.getNickName());
        comment.setAvatar(userDepDto.getIcon());
        comment.setCreateTime(new Date());
        commentService.saveComment(comment);
        return CommonResult.success("评论成功");
    }


    @ApiOperation("删除指定评论")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        int count = commentService.deleteCommentById(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}