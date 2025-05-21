package com.mumuwest.mumumike.controller;

import com.mumuwest.mumumike.pojo.Information;
import com.mumuwest.mumumike.pojo.Message;
import com.mumuwest.mumumike.pojo.TableDataInfo;
import com.mumuwest.mumumike.pojo.VO.InformationVO;
import com.mumuwest.mumumike.service.InformationService;
import com.mumuwest.mumumike.service.MessageService;
import com.mumuwest.mumumike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/information")
public class InformationController {

    @Autowired
    private InformationService informationService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    // 添加信息
    @PostMapping
    public int createInformation(@RequestBody Information information) {
        return informationService.createInformation(information);
    }

    @DeleteMapping("/{id}")
    public int deleteInformation(@PathVariable("id") Integer id) {
        return informationService.deleteInformation(id);
    }

    @PutMapping
    public int updateInformation(@RequestBody Information information) {
        if(information.getStatus() == 1){
            Message message = new Message();
            Information informationById = informationService.getInformationById(information.getId());
            message.setUserId(informationById.getUserId());
            message.setTitle("您的留言《" + informationById.getTitle() + "》已被回复");
            message.setContent("回复内容：" + information.getReply());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = sdf.format(new Date());
            message.setCreateTime(formattedDateTime);
            messageService.createMessage(message);
            information.setUpdateTime(new Date());
        }
        return informationService.updateInformation(information);
    }

    @GetMapping("/{id}")
    public Information getInformationById(@PathVariable("id") Integer id) {
        return informationService.getInformationById(id);
    }

    @PostMapping("/getList")
    public TableDataInfo getInformationList(@RequestBody Information information) {
        List<Information> informationList = informationService.getInformationList(information);
        return new TableDataInfo(informationList, informationList.size());
    }

    @PostMapping("/getVOList")
    public TableDataInfo getInformationVOList(@RequestBody Information information) {
        List<Information> informationList = informationService.getInformationList(information);
        List<InformationVO> informationVOList = new ArrayList<>();
        for (Information info : informationList) {
            InformationVO informationVO = new InformationVO();
            informationVO.setId(info.getId());
            informationVO.setUserId(info.getUserId());
            informationVO.setTitle(info.getTitle());
            informationVO.setContent(info.getContent());
            informationVO.setReply(info.getReply());
            informationVO.setCreateTime(info.getCreateTime());
            informationVO.setUpdateTime(info.getUpdateTime());
            informationVO.setStatus(info.getStatus());
            informationVO.setUser(userService.getUserById(info.getUserId()));
            informationVOList.add(informationVO);
        }
        return new TableDataInfo(informationVOList, informationVOList.size());
    }
}
