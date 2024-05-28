package com.jxm.file.service;

import com.jxm.file.entity.Message;

import java.util.List;

public interface MessageService {

    int insert(Message message);

    int getCountByUserId(Long remindId);

    List<Message> selectByUserId(Long remindId);

    int deleteAllByUserId(Long remindId);
}
