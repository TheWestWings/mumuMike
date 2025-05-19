package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.pojo.AjaxResult;
import com.mumuwest.mumumike.pojo.Message;
import com.mumuwest.mumumike.pojo.TableDataInfo;
import com.mumuwest.mumumike.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 创建信息
     * @param message
     * @return
     */
    @PostMapping
    public AjaxResult createMessage(@RequestBody Message message) {
        return AjaxResult.success(messageService.createMessage(message));
    }

    /**
     * 更新信息
     * @param message
     * @return
     */
    @PutMapping
    public AjaxResult updateMessage(Message message) {
        return AjaxResult.success(messageService.updateMessage(message));
    }

    /**
     * 删除信息
     * @param id
     * @return
     */
    @DeleteMapping
    public AjaxResult deleteMessage(Integer id) {
        return AjaxResult.success(messageService.deleteMessage(id));
    }

    /**
     * 根据Id查询信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public AjaxResult getMessageById(@PathVariable Integer id) {
        return AjaxResult.success(messageService.getMessageById(id));
    }

    /**
     * 根据userId查询信息列表
     * @param userId
     * @return
     */
    @GetMapping("/getMessageListByUserId/{userId}")
    public TableDataInfo getMessageListByUserId(@PathVariable Integer userId) {
        List<Message> messageList = messageService.getMessageListByUserId(userId);
        return new TableDataInfo(messageList, messageList.size());
    }
}
