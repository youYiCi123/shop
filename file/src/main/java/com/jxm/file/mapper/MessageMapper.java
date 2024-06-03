package com.jxm.file.mapper;

import com.jxm.file.entity.Message;

import java.util.List;

public interface MessageMapper {

    int insert(Message message);

    //获取未读消息数量
    int getUnReadCount(Long remindId);

    //对单条数据已读
    int readSingleMessage(Integer id);

    List<Message> selectMessage(Long remindId,Integer readFlag);

    int readAll(Long remindId);

    int deleteReadAll(Long remindId);
}
