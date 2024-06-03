package com.jxm.file.controller;

import cn.hutool.json.JSONUtil;
import com.jxm.common.api.CommonPage;
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
import org.springframework.web.bind.annotation.RequestParam;
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

    //获取未读消息的数量
    @RequestMapping(value = "/getUnReadCount", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Integer> getCountByUserId() throws ParseException {
        String jsonStr = JSONUtil.toJsonStr(upstageService.getCurrentAdmin().getData());
        UserDepDto userDepDto = JSONUtil.toBean(jsonStr, UserDepDto.class);
        int unReadMessageCount = messageService.getUnReadCount(userDepDto.getUserId());
        return CommonResult.success(unReadMessageCount);
    }

    //对单条数据已读
    @RequestMapping(value = "/readSingleMessage", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult readSingleMessage(Integer id){
        return CommonResult.success(messageService.readSingleMessage(id));
    }

    //获取消息 （已读或未读）
    @RequestMapping(value = "/selectMessage", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult selectMessage(@RequestParam(value = "userId") Long userId,
                                                      @RequestParam(value = "readFlag") Integer readFlag,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize){
        List<Message> messages = messageService.selectMessage(userId,readFlag,pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(messages));
    }

    //消息全部已读
    @RequestMapping(value = "/readAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult readAll(Long userId){
        return CommonResult.success(messageService.readAll(userId));
    }

    //清空已读消息
    @RequestMapping(value = "/deleteReadAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult deleteReadAll(Long userId){
        return CommonResult.success(messageService.deleteReadAll(userId));
    }

}
