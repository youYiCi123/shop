package com.jxm.file.service;

import com.jxm.file.entity.Message;

import java.util.List;

public interface MessageService {

    int insert(Message message);

    int getUnReadCount(Long remindId);

    int readSingleMessage(Integer id);

    List<Message> selectMessage(Long remindId,Integer readFlag,Integer pageNum,Integer pageSize);

    int readAll(Long remindId);

    int deleteReadAll(Long remindId);
}
