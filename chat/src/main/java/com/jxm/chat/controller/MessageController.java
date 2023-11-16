package com.jxm.chat.controller;

import com.jxm.chat.feign.UpstageService;
import com.jxm.chat.model.Message;
import com.jxm.chat.service.MessageService;
import com.jxm.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 乐天
 */
@RestController
@RequestMapping("/chat/messages")
public class MessageController {

    @Resource
    private MessageService vimMessageService;

    @GetMapping
    public CommonResult list(String chatId, String fromId, String type, Long pageNum, Long pageSize) {
        pageNum = pageNum == null?1:pageNum;

        List<Message> messageVoList = vimMessageService.list(chatId,fromId,type,pageNum,pageSize);
        for (Message message : messageVoList) {
            message.setMine(fromId.equals(String.valueOf(message.getFromId())));
//            SysUser sysUser = iSysUserService.selectUserById(Long.parseLong(message.getFromId()));
//            message.setAvatar(sysUser.getAvatar()); todo
        }
        Map<String, Object> map = new HashMap<>();
        map.put("messageList", messageVoList);
        map.put("pageNo", pageNum);
        map.put("count", vimMessageService.count(chatId,fromId,type));
        map.put("pageSize", pageSize);
        return CommonResult.success(map);
    }
}
