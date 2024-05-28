package com.jxm.file.service.Impl;

import com.jxm.file.entity.Message;
import com.jxm.file.mapper.MessageMapper;
import com.jxm.file.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public int insert(Message message) {
        return messageMapper.insert(message);
    }

    @Override
    public int getCountByUserId(Long remindId) {
        return messageMapper.getCountByUserId(remindId);
    }

    @Override
    public List<Message> selectByUserId(Long remindId) {
        return messageMapper.selectByUserId(remindId);
    }

    @Override
    public int deleteAllByUserId(Long remindId) {
        return messageMapper.deleteAllByUserId(remindId);
    }
}
