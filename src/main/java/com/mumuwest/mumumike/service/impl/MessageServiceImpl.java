package com.mumuwest.mumumike.service.impl;

import com.mumuwest.mumumike.mapper.MessageMapper;
import com.mumuwest.mumumike.pojo.Message;
import com.mumuwest.mumumike.service.MessageService;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public int createMessage(Message message) {
        return messageMapper.createMessage(message);
    }

    @Override
    public int updateMessage(Message message) {
        return messageMapper.updateMessage(message);
    }

    @Override
    public int deleteMessage(Integer id) {
        return messageMapper.deleteMessage(id);
    }

    @Override
    public Message getMessageById(Integer Id) {
        return messageMapper.getMessageById(Id);
    }

    @Override
    public List<Message> getMessageListByUserId(Integer userId) {
        return messageMapper.getMessageListByUserId(userId);
    }

    @Override
    public int updateMessageStatus(Message message) {
        return messageMapper.updateMessageStatus(message);
    }
}
