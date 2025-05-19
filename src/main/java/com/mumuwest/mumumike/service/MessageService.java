package com.mumuwest.mumumike.service;

import com.mumuwest.mumumike.pojo.Message;

import java.util.List;

public interface MessageService {

    /**
     * 添加信息
     * @param message
     * @return
     */
    public int createMessage(Message message);

    /**
     * 更新信息
     * @param message
     * @return
     */
    public int updateMessage(Message message);

    /**
     * 删除信息
     * @param id
     * @return
     */
    public int deleteMessage(Integer id);

    /**
     * 根据Id查询信息
     * @param Id
     * @return
     */
    public Message getMessageById(Integer Id);

    /**
     * 根据userId查询信息列表
     * @param message
     * @return
     */
    public List<Message> getMessageListByUserId(Integer userId);

    /**
     * 修改信息状态
     * @param message
     * @return
     */
    public int updateMessageStatus(Message message);
}
