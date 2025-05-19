package com.mumuwest.mumumike.mapper;

import com.mumuwest.mumumike.pojo.Message;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;


@Mapper
public interface MessageMapper {
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
     * @param userId
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
