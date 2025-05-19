package com.mumuwest.mumumike.service.impl;

import com.mumuwest.mumumike.pojo.Message;
import com.mumuwest.mumumike.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Override
    public int createMessage(Message message) {
        return 0;
    }

    @Override
    public int updateMessage(Message message) {
        return 0;
    }

    @Override
    public int deleteMessage(Integer id) {
        return 0;
    }

    @Override
    public Message getMessageById(Integer Id) {
        return null;
    }

    @Override
    public List<Message> getMessageList(Message message) {
        return List.of();
    }

    @Override
    public int updateMessageStatus(Message message) {
        return 0;
    }
}
