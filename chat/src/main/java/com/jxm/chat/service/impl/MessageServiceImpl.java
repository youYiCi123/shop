package com.jxm.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxm.chat.model.Message;
import com.jxm.chat.service.MessageService;
import com.jxm.chat.utils.ChatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 消息处理：群聊消息在一个
 *
 * @author 乐天
 */
@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 存放私聊的已读消息
     * message-{minUserId}-{maxUserId}
     */
    private static final String FRIEND_TEMPLATE = "message-{}-{}";

    /**
     * 存放私聊的已读消息
     * message-{minUserId}-{maxUserId}
     */
    private static final String GROUP_TEMPLATE = "message-{}";

    /**
     * 存放群聊的最后一次查看消息时间
     * message-{userId：查看人}-{chatId：群id}
     */
    private static final String READ_TEMPLATE = "read-{}-{}";

    /**
     * 存放"私聊"的未读消息
     * message-{toUserId} 也是 chatId
     */
    private static final String UNREAD_TEMPLATE = "unread-message-{}";

    /**
     * 添加消息到redis 队列，有可能受到的是离线消息，所以要去删除下离线消息里面的记录
     *
     * @param message 消息
     * @param isRead  是否
     * @return boolean
     * @throws JsonProcessingException 抛出异常
     */
    @Override
    public boolean save(Message message, boolean isRead) throws Exception {
        //先保存消息
        String key = isRead ? getChatKey(message.getFromId(), message.getChatId(), message.getType()) : StrUtil.format(UNREAD_TEMPLATE, message.getChatId());
        boolean res = Boolean.TRUE.equals(redisTemplate.opsForZSet().add(key, new ObjectMapper().writeValueAsString(message), message.getTimestamp()));
        //如果是离线消息，去离线队列里处理
        if (message.isOffline()) {
            String unreadKey = StrUtil.format(UNREAD_TEMPLATE, message.getChatId());
            redisTemplate.opsForZSet().removeRangeByScore(unreadKey, message.getTimestamp(), message.getTimestamp());
        }
        return res;
    }


    /**
     * 查询消息
     *
     * @param chatId   聊天室id
     * @param fromId   发送人
     * @param type     聊天类型  私聊 群聊
     * @param pageNum  页码
     * @param pageSize 每页多少条
     * @return List
     */
    @Override
    public List<Message> list(String chatId, String fromId, String type, Long pageNum, Long pageSize) {
        //获取一个固定的key来存储聊天记录到redis
        String key = getChatKey(fromId, chatId, type);

        Set<String> set = redisTemplate.opsForZSet().reverseRange(key, pageNum - 1, pageSize * (pageNum) - 1);
        if (set != null) {
            List<Message> list = set.stream().map(this::toMessage).collect(Collectors.toList());
            Collections.reverse(list);
            //是否最新消息
            if (pageNum == 1) {
                //加上未读消息
                list.addAll(unreadList(chatId,fromId));
            }
            return list;
        }
        return new ArrayList<>();
    }

    /**
     * 读取未读消息
     * 未读消息只存私聊消息，群聊消息还在群列表里
     *
     * @param chatId 聊天室id = toUserId
     * @param fromId 发送人id
     * @return List
     */
    @Override
    public List<Message> unreadList(String chatId, String fromId) {
        String key = StrUtil.format(UNREAD_TEMPLATE, chatId);
        Set<String> set = redisTemplate.opsForZSet().range(key, 0, -1);
        if (set != null) {
            return set.stream().filter(str -> {
                try {
                    Message message = new ObjectMapper().readValue(str, Message.class);
                    //如果发送人为空，取出所有的未读消息
                    return StrUtil.isBlank(fromId) || message.getFromId().equals(fromId);
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                    return false;
                }

            }).map(this::toMessage).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    /**
     * 未读群消息，服务器时间是个很重要的数据，时间不对可能造成离线消息不能全部发送
     *
     * @param userId userId
     * @param chatId 群id
     * @return List
     */
    @Override
    public List<Message> unreadGroupList(String userId, String chatId) {
        String key = getReadKey(userId, chatId);
        String value = redisTemplate.opsForValue().get(key);
        long score = -1;
        if (value != null && StrUtil.isNotBlank(value)) {
            score = Long.parseLong(value);
        }
        Set<String> set = redisTemplate.opsForZSet().rangeByScore(chatId, score, System.currentTimeMillis());
        if (set != null) {
            return set.stream().map(this::toMessage).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    /**
     * 已读消息的条数
     *
     * @param chatId 聊天室id
     * @param formId 发送人
     * @param type   聊天室类型
     * @return 数量
     */
    @Override
    public Long count(String chatId, String formId, String type) {
        String key = getChatKey(formId, chatId, type);
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 读消息，并持久化到redis
     *
     * @param chatId    消息id
     * @param userId    消息读取人
     * @param type      类型
     * @param timestamp 系统时间
     */
    @Override
    public void read(String chatId, String userId, String type, long timestamp) {
        String key = getReadKey(userId, chatId);
        redisTemplate.opsForValue().set(key, String.valueOf(timestamp));
    }

    /**
     * json 转 message
     *
     * @param str str
     * @return Message;
     */
    private Message toMessage(String str) {
        try {
            Message message = new ObjectMapper().readValue(str, Message.class);
            message.setOffline(true);
            return message;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 获取一个固定的key来存储聊天记录到redis
     *
     * @param fromId 来源
     * @param chatId 聊天id
     * @param type   类型
     * @return chatId
     */
    private String getChatKey(String fromId, String chatId, String type) {
        if (ChatUtils.MESSAGE_TYPE_FRIEND.equals(type)) {
            if (Long.parseLong(fromId) < Long.parseLong(chatId)) {
                return StrUtil.format(FRIEND_TEMPLATE, fromId, chatId);
            } else {
                return StrUtil.format(FRIEND_TEMPLATE, chatId, fromId);
            }
        } else {
            return StrUtil.format(GROUP_TEMPLATE, chatId);
        }
    }

    /**
     * 获取一个固定的key来存储聊天记录读取的最后时间
     *
     * @param userId 来源
     * @param chatId 聊天id
     * @return chatId
     */
    private String getReadKey(String userId, String chatId) {
        return StrUtil.format(READ_TEMPLATE, userId, chatId);
    }

}
