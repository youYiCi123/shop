package com.jxm.file.controller;

import cn.hutool.json.JSONUtil;
import com.jxm.common.api.CommonResult;
import com.jxm.file.dto.UserDepDto;
import com.jxm.file.entity.Message;
import com.jxm.file.feign.UpstageService;
import com.jxm.file.service.MessageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;

@Controller
@Api(tags = "MessageController", description = "消息管理")
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private UpstageService upstageService;

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/getCountByUserId", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Integer> getCountByUserId() throws ParseException {
        String jsonStr = JSONUtil.toJsonStr(upstageService.getCurrentAdmin().getData());
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        int unReadMessageCount = messageService.getCountByUserId(userDepDto.getUserId());
        return CommonResult.success(unReadMessageCount);
    }

    @RequestMapping(value = "/selectByUserId", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Message>> selectByUserId(Long userId){
        List<Message> messages = messageService.selectByUserId(userId);
        return CommonResult.success(messages);
    }
}
