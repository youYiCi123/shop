package com.jxm.file.service.Impl;

import com.github.pagehelper.PageHelper;
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
    public int getUnReadCount(Long remindId) {
        return messageMapper.getUnReadCount(remindId);
    }

    @Override
    public int readSingleMessage(Integer id) {
        return messageMapper.readSingleMessage(id);
    }

    @Override
    public List<Message> selectMessage(Long remindId,Integer readFlag,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return messageMapper.selectMessage(remindId,readFlag);
    }

    @Override
    public int readAll(Long remindId) {
        return messageMapper.readAll(remindId);
    }

    @Override
    public int deleteReadAll(Long remindId) {
        return messageMapper.deleteReadAll(remindId);
    }
}
